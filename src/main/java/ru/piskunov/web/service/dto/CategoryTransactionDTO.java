package ru.piskunov.web.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.piskunov.web.service.dto.UserDTO;

@Data
@Accessors(chain = true)
public class CategoryTransactionDTO {
    private Long id;
    private UserDTO userDTO;
    private String categoryName;
}
