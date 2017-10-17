package net.bmacattack.queue.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.stereotype.Component;

@Component("authenticationProvider")
public class UserAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    public UserAuthenticationProvider(CustomUserDetailService userDetailService) {
        setUserDetailsService(userDetailService);
    }
}
