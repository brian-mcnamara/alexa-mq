package net.bmacattack.queue.security;

import org.springframework.security.core.GrantedAuthority;

public class SimpleSerilizedGrantedAuthority implements GrantedAuthority {
    private String authority;

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleSerilizedGrantedAuthority that = (SimpleSerilizedGrantedAuthority) o;

        return authority != null ? authority.equals(that.authority) : that.authority == null;
    }

    @Override
    public int hashCode() {
        return authority != null ? authority.hashCode() : 0;
    }
}
