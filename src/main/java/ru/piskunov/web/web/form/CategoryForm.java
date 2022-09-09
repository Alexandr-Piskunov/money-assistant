package ru.piskunov.web.web.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CategoryForm {
    private Long id;

    @NotEmpty
    private String name;
    private Long userId;
}
