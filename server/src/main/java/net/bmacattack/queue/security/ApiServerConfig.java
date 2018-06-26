package net.bmacattack.queue.security;

import net.bmacattack.queue.access.ScopeBasedPermissionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableResourceServer
@Order(1)
public class ApiServerConfig extends ResourceServerConfigurerAdapter {
    static final AntPathRequestMatcher API_PATH_MATCHER = new AntPathRequestMatcher("/api/**");

    @Autowired
    private UserTokenAuthenticationProvider userTokenAuthenticationProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier("jwtSecret")
    private byte[] jwtSecret;


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("api");
        resources.authenticationManager(new JWTAuthenticationProvider(jwtSecret));
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/index.html", "/static/**", "/api/login").permitAll()
                .and()
                .requestMatcher(request -> {
                    boolean pathMatches = API_PATH_MATCHER.matches(request);
                    return pathMatches && request.getHeader("authorization").startsWith(OAuth2AccessToken.BEARER_TYPE);
                })
                .authorizeRequests()
                .and()
                .requestMatcher(ApiServerConfig.API_PATH_MATCHER).httpBasic().authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        if (authException instanceof InternalAuthenticationServiceException) {
                            response.sendError(500);
                        } else {
                            response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value());
                        }
                    }
                })
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(userTokenAuthenticationProvider).authorizeRequests().anyRequest().fullyAuthenticated();
    }

    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class ResourceSecurityServerConfig
            extends GlobalMethodSecurityConfiguration {

        @Override
        protected MethodSecurityExpressionHandler createExpressionHandler() {
            DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
            expressionHandler.setPermissionEvaluator(new ScopeBasedPermissionEvaluator());
            return expressionHandler;
        }
    }
}
