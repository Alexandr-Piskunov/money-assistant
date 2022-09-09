package ru.piskunov.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.piskunov.web.converter.CategoryTransactionModelToCategoryTransactionDTOConverter;
import ru.piskunov.web.entity.CategoryTransaction;
import ru.piskunov.web.repository.CategoryTransactionRepository;
import ru.piskunov.web.repository.UserRepository;
import ru.piskunov.web.service.dto.CategoryTransactionDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryTransactionService {
    private final CategoryTransactionRepository categoryTransactionRepository;
    private final UserRepository userRepository;
    private final CategoryTransactionModelToCategoryTransactionDTOConverter converter;

    public Optional<CategoryTransactionDTO> findById(Long id) {
        return categoryTransactionRepository.findById(id).map(converter::convert);
    }

    @Transactional
    public List<CategoryTransactionDTO> findAll(Long userId) {
        return categoryTransactionRepository.findAllByUserId(userId)
                .stream()
                .map(converter::convert).collect(Collectors.toList());
    }

    @Transactional
    public Optional<CategoryTransactionDTO> create(String categoryName, Long userId) {
        if (!categoryTransactionRepository.findByCategoryNameAndUserId(categoryName, userId).isPresent()) {
            return Optional.of(categoryTransactionRepository.save(new CategoryTransaction()
                    .setCategoryName(categoryName)
                    .setUser(userRepository.getById(userId)))).map(converter::convert);
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<CategoryTransactionDTO> rename(String newName, long id, long userId) {
        Optional<CategoryTransaction> check = categoryTransactionRepository.findByIdAndUserId(id, userId);
        if (check.isPresent()) {
            if (!categoryTransactionRepository.findByCategoryNameAndUserId(newName, userId).isPresent()) {
                return Optional.of(categoryTransactionRepository.saveAndFlush(check.get().setCategoryName(newName)))
                        .map(converter::convert);
            }
        }
        return Optional.empty();
    }

    @Transactional
    public boolean deleteCategory(Long id, Long userId) {
        if (categoryTransactionRepository.findByIdAndUserId(id, userId).isPresent()) {
            categoryTransactionRepository.deleteByIdAndUserId(id, userId);
            return true;
        }
        return false;
    }
}
