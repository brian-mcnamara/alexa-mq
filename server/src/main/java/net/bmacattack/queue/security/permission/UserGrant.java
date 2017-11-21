package net.bmacattack.queue.security.permission;

import org.springframework.security.core.GrantedAuthority;

public class UserGrant implements GrantedAuthority {
    private String role;

    public UserGrant(String role) {
        this.role = "ROLE_" + role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
