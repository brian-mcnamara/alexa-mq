package net.bmacattack.queue.web;

import net.bmacattack.queue.web.dto.OauthClientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@Secured("AUTHORITY_ADMIN")
public class OAuth2Client {

    @Autowired
    private ClientRegistrationService clientDetailsService;

    @RequestMapping(value = "/admin/oauth/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String registerOauthClient(@Valid OauthClientDto clientDetails, final HttpServletRequest request) {
        clientDetailsService.addClientDetails(clientDetails);
        return "success";
    }

    @RequestMapping(value = "/admin/oauth/clients", method = RequestMethod.GET)
    public String getRegisteredClients() {
        List<ClientDetails> clientDetailsList = clientDetailsService.listClientDetails();
        return "";
    }
}
