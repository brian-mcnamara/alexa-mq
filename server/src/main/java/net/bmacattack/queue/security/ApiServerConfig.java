package net.bmacattack.queue.security;

import net.bmacattack.queue.access.ScopeBasedPermissionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
        http.authorizeRequests().antMatchers("/index.html", "/static/**").permitAll()
                .and()
                .requestMatcher(request -> {
                    boolean pathMatches = API_PATH_MATCHER.matches(request);
                    return pathMatches && request.getHeader("authorization").startsWith(OAuth2AccessToken.BEARER_TYPE);
                })
                .authorizeRequests()
                .and()
                .requestMatcher(ApiServerConfig.API_PATH_MATCHER).httpBasic()
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
