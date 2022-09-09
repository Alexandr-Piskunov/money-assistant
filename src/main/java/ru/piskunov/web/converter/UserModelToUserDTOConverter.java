package ru.piskunov.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.piskunov.web.entity.User;
import ru.piskunov.web.service.dto.UserDTO;

@Component
public class UserModelToUserDTOConverter implements Converter<User, UserDTO> {

    @Override
    public UserDTO convert(User source) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(source.getId());
        userDTO.setEmail(source.getEmail());
        userDTO.setUserName(source.getUserName());
        return userDTO;
    }
}
