package net.bmacattack.queue.web.dto;

import javax.validation.constraints.NotNull;

public class MinimalOauthClient {
    @NotNull
    protected String clientId;

    @NotNull
    protected String redirectUrls;

    @NotNull
    protected String scopes;


    public String getClientId() {
        return clientId;
    }
}
