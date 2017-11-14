package net.bmacattack.queue.security;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;

import java.util.regex.Pattern;


public class JWTAuthenticationProvider extends OAuth2AuthenticationManager {
    static final Pattern JWT_PATTERN = Pattern.compile("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$");

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getPrincipal() instanceof String) {
            String token = (String) authentication.getPrincipal();
            if (JWT_PATTERN.matcher(token).matches()) {
                String user = Jwts.parser().setSigningKey("TODO".getBytes())
                        .parseClaimsJws((String) authentication.getPrincipal())
                        .getBody().getSubject();
                return new UsernamePasswordAuthenticationToken(user, null, null);
            }
        }
        return super.authenticate(authentication);
    }
}
