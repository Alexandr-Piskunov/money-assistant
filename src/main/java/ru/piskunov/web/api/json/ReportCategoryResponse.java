package ru.piskunov.web.api.json;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportCategoryResponse {
    private String name;
    private Long amount;
}
