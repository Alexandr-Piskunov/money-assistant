package ru.piskunov.web.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ReportCategoryDTO {
    private String name;
    private Long amount;
}
