package net.bmacattack.queue.web;

import net.bmacattack.queue.persistence.RoleEnum;
import net.bmacattack.queue.persistence.dao.UserRepository;
import net.bmacattack.queue.persistence.model.User;
import net.bmacattack.queue.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class UserRegistration {

    @Autowired
    private UserRepository repository;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUserAccoun(@Valid @RequestBody final UserDto accountDto) {
        User user = new User(accountDto.getEmail(), accountDto.getEmail(), accountDto.getPassword(), RoleEnum.USER);
        repository.save(user);
    }
}
