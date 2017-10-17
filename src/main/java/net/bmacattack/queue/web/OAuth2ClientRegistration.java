package net.bmacattack.queue.web;

import net.bmacattack.queue.web.dto.OauthClientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class OAuth2ClientRegistration {

    @Autowired
    private JdbcClientDetailsService clientDetailsService;

    @RequestMapping(value = "/admin/oauth/registration", method = RequestMethod.POST)
    @Secured("AUTHORITY_ADMIN")
    public String registerOauthClient(@Valid OauthClientDto clientDetails, final HttpServletRequest request) {
        clientDetailsService.addClientDetails(clientDetails);
        return "success";
    }
}
