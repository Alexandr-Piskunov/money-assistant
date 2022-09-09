package ru.piskunov.web.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AccountDTO {
    private Long id;
    private String accountName;
    private Long balance;
    private UserDTO userDTO;
}
