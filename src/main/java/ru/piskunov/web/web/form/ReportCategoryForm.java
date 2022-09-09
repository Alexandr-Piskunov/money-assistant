package ru.piskunov.web.web.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ReportCategoryForm {
    private Long id;
    private String name;
    private Long amount;

    @NotEmpty
    private String startDate;

    @NotEmpty
    private String endDate;
}
