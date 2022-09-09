package ru.piskunov.web.api.json;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReportCategoryRequest {

    @NotNull
    private Long id;

    @NotNull
    private String startDate;

    @NotNull
    private String endDate;
}
