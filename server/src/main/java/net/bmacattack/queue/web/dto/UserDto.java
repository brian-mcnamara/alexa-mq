package net.bmacattack.queue.web.dto;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDto {
    @NotNull
    @Size(min = 1)
    private String firstName;

    @NotNull
    @Size(min = 1)
    private String lastName;

    @Size(min = 1)
    @NotNull
    private String password;

    @NotNull
    @Size(min = 1)
    private String matchingPassword;

    @Email
    @NotNull
    @Size(min = 1)
    private String email;
}
