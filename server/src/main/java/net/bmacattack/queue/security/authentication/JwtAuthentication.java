package net.bmacattack.queue.security.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthentication extends UsernamePasswordAuthenticationToken {
    public JwtAuthentication(String username, Collection<? extends GrantedAuthority> authorities) {
        super(username, null, authorities);
    }
}
