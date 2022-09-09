package ru.piskunov.web.api.json;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AccountRequest {
    private Long id;

    @NotNull
    private String name;
}
