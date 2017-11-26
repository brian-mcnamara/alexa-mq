package net.bmacattack.queue.security;

import net.bmacattack.queue.persistence.RoleEnum;
import net.bmacattack.queue.security.filters.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(10)
public class WebServerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    @Qualifier("jwtSecret")
    private byte[] jwtSecret;

    @Value("${disableCSRT:false}")
    private Boolean disableCSRT;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.authenticationProvider(userAuthenticationProvider);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean()
            throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/login", "/register", "/index.html", "/static/**", "/logout", "favicon.ico", "/").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/admin/**/*").hasAuthority(RoleEnum.ADMIN.toString())
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtSecret))
                .formLogin().permitAll();
    }
}
