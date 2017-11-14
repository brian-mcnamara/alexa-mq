package net.bmacattack.queue.web;

import net.bmacattack.queue.persistence.dao.UserRepository;
import net.bmacattack.queue.persistence.model.User;
import net.bmacattack.queue.persistence.model.UserAccessToken;
import net.bmacattack.queue.web.dto.TokenDto;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

@Controller
public class UserTokenApi {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/api/tokens", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<UserAccessToken> getUserTokens(Principal principal) {
        String username = principal.getName();
        User user = userRepository.getUserByUsername(username);
        if (user != null) {
            return user.getAccessTokens();
        }
        return Collections.emptyList();
    }

    @RequestMapping(value = "/api/tokens/remove/{id}", method = RequestMethod.POST)
    public ResponseEntity deleteToken(@PathVariable("id") String tokenName, Principal principal) {
        String username = principal.getName();
        User user = userRepository.getUserByUsername(username);
        if (user != null) {
            UserAccessToken token = user.getAccessTokens().stream().filter(uat -> uat.getName().equals(tokenName)).findFirst().orElse(null);
            if (token != null) {
                user.deleteAccessToken(token);
                userRepository.save(user);
                return new ResponseEntity(HttpStatus.OK);
            }
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/api/tokens/create", method = RequestMethod.POST)
    @ResponseBody
    public String createToken(@RequestBody final TokenDto token, HttpServletResponse response, Principal principal) throws IOException {
        String username = principal.getName();
        User user = userRepository.getUserByUsername(username);
        if (user != null && token != null) {
            if (token.getName() == null || token.getScopes() == null) {
                //todo fix this
                return null;
            }
            SecureRandom sr = new SecureRandom();
            byte bytes[] = new byte[20];
            sr.nextBytes(bytes);
            String newToken = new String(Base64.encodeBase64(bytes));
            UserAccessToken uat = new UserAccessToken(token.getName(), newToken ,
                    token.getScopes());
            user.addAccessToken(uat);
            userRepository.save(user);
            response.setStatus(HttpStatus.CREATED.value());
            return newToken;
        }
        response.sendError(HttpStatus.NOT_MODIFIED.value());
        return null;
    }
}
