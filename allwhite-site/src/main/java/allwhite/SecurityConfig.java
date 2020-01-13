package allwhite;

import allwhite.team.MemberProfile;
import allwhite.team.support.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.header.writers.HstsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.connect.GitHubConnectionFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
class SecurityConfig {
    static final String SIGNIN_SUCCESS_PATH = "/signin/success";
    static class SecuritycontextAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

        protected SecuritycontextAuthenticationFilter(String defaultFilterProcessesUrl) {
            super(defaultFilterProcessesUrl);
            setAuthenticationManager(authentication -> {
                throw new IllegalStateException("Unexpected call for AuthenticationManager");
            });
        }

        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
                throws AuthenticationException, IOException, ServletException {
            return SecurityContextHolder.getContext().getAuthentication();
        }
    }

    static class GithubAuthenticationSigninAdapter implements SignInAdapter {
        private String path;
        private final SignInService signInService;

        public GithubAuthenticationSigninAdapter(String path, SignInService signInService) {
            this.path = path;
            this.signInService = signInService;
        }

        @Override
        public String signIn(String githubId, Connection<?> connection, NativeWebRequest request) {
            GitHub gitHub = (GitHub) connection.getApi();
            String githubUsername = connection.getDisplayName();

            try {
                if (!signInService.isSpringMember(githubUsername, gitHub)) {
                    throw new BadCredentialsException("User not member of required org");
                }
                MemberProfile member = signInService.getOrCreateMemberProfile(new Long(githubId), gitHub);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        member.getId(), member.getGithubUsername(),
                        AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return path;
            } catch (RestClientException ex) {
                throw new BadCredentialsException("User not member of required org", ex);
            }
        }
    }

    @Configuration
    @Order(Ordered.LOWEST_PRECEDENCE - 90)
    protected static class AdminAuthenticationConfig extends WebSecurityConfigurerAdapter implements EnvironmentAware {
        @Autowired
        private SignInService signInService;

        private Environment environment;

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            configureHeaders(http.headers());
            http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                    .and().requestMatchers().antMatchers("/admin/**", "/signout").and()
                    .addFilterAfter(new OncePerRequestFilter() {
                        @Override
                        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                            if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof Long)) {
                                throw new BadCredentialsException("Not a github user!");
                            }
                            filterChain.doFilter(request, response);
                        }
                    }, ExceptionTranslationFilter.class);
            http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/sigout"))
                    .logoutSuccessUrl("/").and().authorizeRequests().anyRequest()
                    .authenticated();
            if (isForceHttps()) {
                http.requiresChannel().anyRequest().requiresSecure();
            }
        }

        private AuthenticationEntryPoint authenticationEntryPoint() {
            LoginUrlAuthenticationEntryPoint entryPoint = new LoginUrlAuthenticationEntryPoint("/signin");
            entryPoint.setForceHttps(isForceHttps());
            return entryPoint;
        }

        private boolean isForceHttps() {
            return !environment.acceptsProfiles(AllwhiteProfiles.STANDALONE);
        }

        @Bean
        public ProviderSignInController providerSignInController(GitHubConnectionFactory connectionFactory,
                                                                 ConnectionFactoryRegistry registry,
                                                                 InMemoryUsersConnectionRepository repository) {
            registry.addConnectionFactory(connectionFactory);
            repository.setConnectionSignUp(new RemoteUsernameConnectionSignUp());
            ProviderSignInController controller = new ProviderSignInController(registry, repository, new GithubAuthenticationSigninAdapter(
                    SIGNIN_SUCCESS_PATH, signInService));
            controller.setSignInUrl("/signin?error=access_denied");
            return controller;
        }

        @Bean
        public ConnectionFactoryRegistry connectionFactoryRegistry() {
            return new ConnectionFactoryRegistry();
        }

        @Bean
        public InMemoryUsersConnectionRepository inMemoryUsersConnectionRepository(ConnectionFactoryRegistry registry) {
            return new InMemoryUsersConnectionRepository(registry);
        }
    }

    static class RemoteUsernameConnectionSignUp implements ConnectionSignUp {

        @Override
        public String execute(Connection<?> connection) {
            return connection.getKey().getProviderUserId() != null ? connection.getKey().getProviderUserId() : null;
        }
    }

    private static void configureHeaders(HeadersConfigurer<?> headers) {
        HstsHeaderWriter writer = new HstsHeaderWriter(false);
        writer.setRequestMatcher(AnyRequestMatcher.INSTANCE);
        headers.contentTypeOptions().and().xssProtection()
                .and().cacheControl().and().addHeaderWriter(writer).frameOptions();
    }
}

