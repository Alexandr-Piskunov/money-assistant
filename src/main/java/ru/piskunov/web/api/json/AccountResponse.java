package ru.piskunov.web.api.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.piskunov.web.service.dto.UserDTO;

@Data
@AllArgsConstructor
public class AccountResponse {
    private Long id;
    private String accountName;
    private Long balance;
    private UserDTO userDTO;
}
