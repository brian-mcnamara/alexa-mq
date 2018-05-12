package net.bmacattack.queue.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bmacattack.queue.persistence.model.User;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
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
}
