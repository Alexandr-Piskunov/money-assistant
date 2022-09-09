package ru.piskunov.web.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.piskunov.web.entity.Account;
import ru.piskunov.web.service.dto.AccountDTO;

@Component
@RequiredArgsConstructor
public class AccountModelToAccountDTOConverter implements Converter<Account, AccountDTO> {
    private final UserModelToUserDTOConverter converter;

    @Override
    public AccountDTO convert(Account source) {
        if (source == null) {
            return null;
        }
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(source.getId());
        accountDTO.setAccountName(source.getAccountName());
        accountDTO.setBalance(source.getBalance());
        accountDTO.setUserDTO(converter.convert(source.getUser()));
        return accountDTO;
    }
}
