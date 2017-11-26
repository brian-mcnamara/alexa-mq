package net.bmacattack.queue.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.bmacattack.queue.persistence.model.User;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    static final String AUTHORIZATION_HEADER = "Authorization";
    private AuthenticationManager authenticationManager;
    private final byte[] jwtSecret;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, byte[] jwtSecret) {
        this.authenticationManager = authenticationManager;
        this.jwtSecret = jwtSecret;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AuthenticationException exception;
        try {
            return super.attemptAuthentication(request, response);
        } catch (AuthenticationException e) {
            exception = e;
        }
        try {
            if (request.getContentType().startsWith(MediaType.APPLICATION_JSON.toString())) {
                User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
                return authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                user.getPassword(), null));
            }
        } catch (IOException e) {
            //todo log unsuccessful authentication
        }
        throw exception;
    }

    @Override
    protected AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)authResult.getPrincipal();
        String token = Jwts.builder().setSubject(
                user.getUsername())
                .setExpiration(Date.from(LocalDateTime.now().plusHours(2).atZone(ZoneId.systemDefault()).toInstant()))
                .setAudience(objectMapper.writeValueAsString(user.getAuthorities()))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        response.addHeader(AUTHORIZATION_HEADER, token);
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
