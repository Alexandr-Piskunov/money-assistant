package ru.piskunov.web.api.json;

import lombok.Data;

@Data
public class CategoryTransactionRequest {
    private Long id;
    private String categoryName;
    private Long userId;
}
