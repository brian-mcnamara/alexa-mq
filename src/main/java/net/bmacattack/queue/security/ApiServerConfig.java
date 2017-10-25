package net.bmacattack.queue.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("api");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(request -> {
            boolean pathMatches = API_PATH_MATCHER.matches(request);
            return pathMatches && request.getHeader("authorization").startsWith(OAuth2AccessToken.BEARER_TYPE);
        }).authorizeRequests().anyRequest().fullyAuthenticated()
                .and()
                .csrf().disable();
    }
}
