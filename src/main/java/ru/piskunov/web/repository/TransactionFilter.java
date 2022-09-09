package ru.piskunov.web.repository;

import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Date;

@Data
@Accessors(chain = true)
public class TransactionFilter {
    private Long userId;
    private Date startDate;
    private Date endDate;
}
