package net.bmacattack.queue.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.bmacattack.queue.security.filters.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Controller
public class WebLoginEndpoint {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier("jwtSecret")
    private byte[] jwtSecret;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public void login(@RequestBody LoginDTO login, HttpServletResponse response) {
        String username = login.getUsername();
        String password = login.getPassword();
        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }

        try {
           Authentication authResult =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, null));
            User user = (User)authResult.getPrincipal();
            String token = Jwts.builder().setSubject(
                    user.getUsername())
                    .setExpiration(Date.from(LocalDateTime.now().plusHours(2).atZone(ZoneId.systemDefault()).toInstant()))
                    .setAudience(objectMapper.writeValueAsString(user.getAuthorities()))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();
            response.addHeader(JWTAuthenticationFilter.AUTHORIZATION_HEADER, token);

        } catch (AuthenticationException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } catch (JsonProcessingException e) {

        }
    }

    private static class LoginDTO {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
