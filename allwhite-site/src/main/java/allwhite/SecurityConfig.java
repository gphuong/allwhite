package allwhite;

import allwhite.team.MemberProfile;
import allwhite.team.support.SignInService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.github.api.GitHub;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityConfig {
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
}

