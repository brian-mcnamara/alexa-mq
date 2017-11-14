package net.bmacattack.queue.web.dto;

import java.util.Set;

public class TokenDto {
    private String name;
    //TODO this should be a set of a enum or something
    private String scopes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }
}
