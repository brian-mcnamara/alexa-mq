package net.bmacattack.queue.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component("preAuthProvider")
public class CustomPreAuthProvider extends PreAuthenticatedAuthenticationProvider {

    @Autowired
    public CustomPreAuthProvider(AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> userService) {
        setPreAuthenticatedUserDetailsService(userService);
    }

}
