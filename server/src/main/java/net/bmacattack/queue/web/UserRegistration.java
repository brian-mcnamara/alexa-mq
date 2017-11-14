package net.bmacattack.queue.web;

import net.bmacattack.queue.web.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class UserRegistration {

    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
    @ResponseBody
    public String registerUserAccoun(@Valid final UserDto accountDto, final HttpServletRequest request) {
        return "";
    }
}
