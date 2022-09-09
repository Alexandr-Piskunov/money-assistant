package ru.piskunov.web.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ReportCategoryModel {
    private String name;
    private Long amount;
}
