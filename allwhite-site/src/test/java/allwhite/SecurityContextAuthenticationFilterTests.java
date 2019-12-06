package allwhite;

import allwhite.SecurityConfig.SecuritycontextAuthenticationFilter;
import org.junit.After;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextAuthenticationFilterTests {
    private SecuritycontextAuthenticationFilter filter = new SecuritycontextAuthenticationFilter("/foo");

    @After
    public void clean() {
        SecurityContextHolder.clearContext();
    }


}
