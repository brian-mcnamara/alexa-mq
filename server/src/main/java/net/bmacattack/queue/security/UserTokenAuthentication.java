package net.bmacattack.queue.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Set;


public class UserTokenAuthentication extends UsernamePasswordAuthenticationToken {

    private Set<String> priviledges;

    public UserTokenAuthentication(Object principal, Object credentials, Set<String> priviledges) {
        super(principal, credentials, null);
        this.priviledges = priviledges;
    }

    public Set<String> getScope() {
        return priviledges;
    }
}
