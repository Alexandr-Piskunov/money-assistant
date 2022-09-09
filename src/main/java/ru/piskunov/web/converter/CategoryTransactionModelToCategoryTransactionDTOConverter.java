package ru.piskunov.web.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.piskunov.web.entity.CategoryTransaction;
import ru.piskunov.web.service.dto.CategoryTransactionDTO;

@Component
@RequiredArgsConstructor
public class CategoryTransactionModelToCategoryTransactionDTOConverter implements Converter<CategoryTransaction, CategoryTransactionDTO> {
    private final UserModelToUserDTOConverter converter;

    @Override
    public CategoryTransactionDTO convert(CategoryTransaction source) {
        CategoryTransactionDTO categoryTransactionDTO = new CategoryTransactionDTO();
        categoryTransactionDTO.setId(source.getId());
        categoryTransactionDTO.setCategoryName(source.getCategoryName());
        categoryTransactionDTO.setUserDTO(converter.convert(source.getUser()));
        return categoryTransactionDTO;
    }
}
