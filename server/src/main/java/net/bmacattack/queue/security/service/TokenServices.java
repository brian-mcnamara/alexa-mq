package net.bmacattack.queue.security.service;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TokenServices extends DefaultTokenServices {
    private static final Logger LOGGER = Logger.getLogger(TokenServices.class.getName());

    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        OAuth2AccessToken token = super.createAccessToken(authentication);
        LOGGER.log(Level.INFO, "Refresh token: " + token.getRefreshToken().getValue() + " used for client " + authentication.getOAuth2Request().getClientId());
        return token;
    }

    @Override
    public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest) throws AuthenticationException {
        try {
            return super.refreshAccessToken(refreshTokenValue, tokenRequest);
        } catch (AuthenticationException e) {
            LOGGER.log(Level.INFO, e.getMessage());
            throw e;
        }
    }
}
