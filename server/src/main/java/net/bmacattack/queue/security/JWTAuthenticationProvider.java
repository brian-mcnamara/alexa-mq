package net.bmacattack.queue.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import io.jsonwebtoken.*;
import net.bmacattack.queue.security.authentication.JwtAuthentication;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;

import java.io.IOException;
import java.util.regex.Pattern;


public class JWTAuthenticationProvider extends OAuth2AuthenticationManager {
    static final Pattern JWT_PATTERN = Pattern.compile("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$");
    private final byte[] jwtSecret;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JWTAuthenticationProvider(byte[] jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getPrincipal() instanceof String) {
            String token = (String) authentication.getPrincipal();
            if (JWT_PATTERN.matcher(token).matches()) {
                try {
                    Jws<Claims> jwt = Jwts.parser().setSigningKey(jwtSecret)
                            .parseClaimsJws((String) authentication.getPrincipal());
                    String user = jwt.getBody().getSubject();
                    String authorities = jwt.getBody().getAudience();
                    return new JwtAuthentication(user, Lists.newArrayList(objectMapper.readValue(authorities, SimpleSerilizedGrantedAuthority[].class)));
                } catch (IOException | JwtException e) {
                    throw new AuthenticationServiceException("Corrupt or expired JWT token", e);
                }
            }
        }
        return super.authenticate(authentication);
    }
}
