package ru.piskunov.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.piskunov.web.entity.CategoryTransaction;

import java.util.List;
import java.util.Optional;

public interface CategoryTransactionRepository extends JpaRepository<CategoryTransaction, Long> {

    List<CategoryTransaction> findAllByUserId(Long userId);

    Optional<CategoryTransaction> findByCategoryNameAndUserId(String categoryName, Long userId);

    Optional<CategoryTransaction> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);
}
