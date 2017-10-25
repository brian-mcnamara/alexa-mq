package net.bmacattack.queue.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Configuration
@EnableResourceServer
@Order(5)
public class tokenBaseApiServerConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserTokenAuthentication userTokenAuthentication;

    public tokenBaseApiServerConfig() {
        super(true);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userTokenAuthentication);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.servletApi().and()
                .securityContext()
                .and()
                .requestMatcher(ApiServerConfig.API_PATH_MATCHER).httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(userTokenAuthentication).authorizeRequests().anyRequest().fullyAuthenticated();

    }
}
