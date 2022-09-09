package ru.piskunov.web.api.json;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class AuthRequest {
    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String userName;
}
