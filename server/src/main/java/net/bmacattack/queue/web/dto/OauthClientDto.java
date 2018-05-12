package net.bmacattack.queue.web.dto;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class OauthClientDto extends MinimalOauthClient implements ClientDetails {
    @NotNull
    private String resourceIds;

    @NotNull
    private String clientSecret;

    @NotNull
    private String grantTypes;

    @Override
    public Set<String> getResourceIds() {
        return Sets.newHashSet(resourceIds.split(","));
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public boolean isScoped() {
        return false;
    }

    @Override
    public Set<String> getScope() {
        return Sets.newHashSet(scopes.split(","));
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return Sets.newHashSet(grantTypes.split(","));
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return Sets.newHashSet(redirectUrls.split(","));
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return 7 * 60 * 60 * 24;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return 30 * 60 * 60 * 24;
    }

    @Override
    public boolean isAutoApprove(String s) {
        return false;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return Collections.emptyMap();
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setGrantTypes(String grantTypes) {
        this.grantTypes = grantTypes;
    }

    public void setRedirectUrls(String redirectUrls) {
        this.redirectUrls = redirectUrls;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }
}
