package ru.piskunov.web.api.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.piskunov.web.service.dto.UserDTO;

@Data
@AllArgsConstructor
public class CategoryTransactionResponse {
    private Long id;
    private UserDTO userDTO;
    private String categoryName;
}
