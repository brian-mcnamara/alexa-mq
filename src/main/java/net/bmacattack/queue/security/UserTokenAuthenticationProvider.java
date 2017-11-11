package net.bmacattack.queue.security;

import net.bmacattack.queue.persistence.dao.UserRepository;
import net.bmacattack.queue.persistence.model.User;
import net.bmacattack.queue.persistence.model.UserAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collections;

@Component
public class UserTokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        Object token = authentication.getCredentials();
        User user = userRepository.getUserByUsername(username);
        if (user != null) {
            //TODO tokens ever return null?
            UserAccessToken userToken = user.getAccessTokens().stream()
                    .filter(uat -> uat.getAccessToken().equals(token)).findFirst().orElse(null);
            if (userToken != null) {
                return new UserTokenAuthentication(username, authentication.getCredentials(), userToken.getRights());
            }
        }
        throw new BadCredentialsException("Invalid basic authentication");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
