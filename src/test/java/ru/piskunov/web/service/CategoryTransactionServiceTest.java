package ru.piskunov.web.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.piskunov.web.converter.CategoryTransactionModelToCategoryTransactionDTOConverter;
import ru.piskunov.web.entity.CategoryTransaction;
import ru.piskunov.web.entity.User;
import ru.piskunov.web.repository.CategoryTransactionRepository;
import ru.piskunov.web.repository.UserRepository;
import ru.piskunov.web.service.dto.CategoryTransactionDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CategoryTransactionServiceTest {

    @InjectMocks
    CategoryTransactionService subj;

    @Mock
    CategoryTransactionRepository categoryTransactionRepository;

    @Mock
    CategoryTransactionModelToCategoryTransactionDTOConverter converter;

    @Mock
    UserRepository userRepository;

    @Test
    public void getById_CategoryFound() {
        CategoryTransaction categoryTransaction = new CategoryTransaction().setId(1L);
        when(categoryTransactionRepository.findById(1L)).thenReturn(Optional.of(categoryTransaction));
        CategoryTransactionDTO categoryTransactionDTO = new CategoryTransactionDTO()
                .setId(1L);
        when(converter.convert(categoryTransaction)).thenReturn(categoryTransactionDTO);

        Optional<CategoryTransactionDTO> one = subj.findById(1L);

        assertTrue(one.isPresent());
        assertEquals(categoryTransactionDTO, one.get());

        verify(converter, times(1)).convert(categoryTransaction);
        verify(categoryTransactionRepository, times(1)).findById(1L);
        verifyNoInteractions(userRepository);
    }

    @Test
    public void getById_CategoryNotFound() {
        when(categoryTransactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertFalse(subj.findById(1L).isPresent());

        verify(categoryTransactionRepository, times(1)).findById(1L);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(converter);
    }

    @Test
    public void findAll_CategoryFound() {
        List<CategoryTransaction> categoryTransactions = new ArrayList<>();
        User user = new User().setId(1L);
        categoryTransactions.add(new CategoryTransaction()
                .setUser(user)
                .setCategoryName("test")
                .setId(0L));
        when(categoryTransactionRepository.findAllByUserId(1L)).thenReturn(categoryTransactions);
        CategoryTransactionDTO categoryTransactionDTO = new CategoryTransactionDTO();
        List<CategoryTransactionDTO> categoryTransactionDTOS = new ArrayList<>();
        when(converter.convert(categoryTransactions.get(0))).thenReturn(categoryTransactionDTO);
        categoryTransactionDTOS.add(categoryTransactionDTO);

        assertEquals(categoryTransactionDTOS, subj.findAll(1L));

        verify(categoryTransactionRepository, times(1)).findAllByUserId(1L);
        verify(converter, times(1)).convert(categoryTransactions.get(0));
        verifyNoInteractions(userRepository);
    }

    @Test
    public void findAll_CategoryNotFound() {
        List<CategoryTransaction> categoryTransactions = new ArrayList<>();
        when(categoryTransactionRepository.findAllByUserId(1L)).thenReturn(categoryTransactions);
        List<CategoryTransactionDTO> categoryTransactionDTOS = new ArrayList<>();
        List<CategoryTransactionDTO> all = subj.findAll(1L);

        assertEquals(categoryTransactionDTOS, all);

        verify(categoryTransactionRepository, times(1)).findAllByUserId(1L);
        verifyNoInteractions(converter);
        verifyNoInteractions(userRepository);
    }

    @Test
    public void create_CategoryNotFound() {
        CategoryTransaction categoryTransaction = new CategoryTransaction();
        when(categoryTransactionRepository.findByCategoryNameAndUserId("newCategory", 1L)).thenReturn(Optional.of(categoryTransaction));

        assertFalse(subj.create("newCategory", 1L).isPresent());

        verify(categoryTransactionRepository, times(1)).findByCategoryNameAndUserId("newCategory", 1L);
        verifyNoInteractions(converter);
    }

    @Test
    public void create_CategoryFound() {
        User user = new User().setId(1L);
        when(categoryTransactionRepository.findByCategoryNameAndUserId("newCategory", 1L)).thenReturn(Optional.empty());
        when(userRepository.getById(1L)).thenReturn(user);
        CategoryTransaction categoryTransaction = new CategoryTransaction()
                .setCategoryName("newCategory")
                .setUser(user);
        when(categoryTransactionRepository.save(new CategoryTransaction()
                .setCategoryName("newCategory")
                .setUser(user))).thenReturn(categoryTransaction);
        CategoryTransactionDTO transactionDTO = new CategoryTransactionDTO();
        when(converter.convert(categoryTransaction)).thenReturn(transactionDTO);
        Optional<CategoryTransactionDTO> newCategory = subj.create("newCategory", 1L);

        assertTrue(newCategory.isPresent());
        assertEquals(transactionDTO, newCategory.get());

        verify(categoryTransactionRepository, times(1)).findByCategoryNameAndUserId("newCategory", 1L);
        verify(categoryTransactionRepository, times(1)).save(new CategoryTransaction()
                .setCategoryName("newCategory")
                .setUser(user));
        verify(userRepository, times(1)).getById(1L);
        verify(converter, times(1)).convert(categoryTransaction);
    }

    @Test
    public void rename_CategoryNotFound() {
        when(categoryTransactionRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        assertFalse(subj.rename("newName", 1, 1).isPresent());

        verify(categoryTransactionRepository, times(1)).findByIdAndUserId(1L, 1L);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(converter);

    }

    @Test
    public void rename_CategoryFound() {
        User user = new User().setId(1L);
        CategoryTransaction categoryTransaction = new CategoryTransaction()
                .setId(1L)
                .setCategoryName("newName")
                .setUser(user);
        when(categoryTransactionRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(categoryTransaction));
        when(categoryTransactionRepository.saveAndFlush(new CategoryTransaction()
                .setId(1L)
                .setCategoryName("newName")
                .setUser(user))).thenReturn(categoryTransaction);
        CategoryTransactionDTO transactionDTO = new CategoryTransactionDTO();
        when(converter.convert(categoryTransaction)).thenReturn(transactionDTO);
        Optional<CategoryTransactionDTO> newCategory = subj.rename("newName", 1, 1);

        assertTrue(newCategory.isPresent());
        assertEquals(transactionDTO, newCategory.get());

        verify(categoryTransactionRepository, times(1)).findByIdAndUserId(1L, 1L);
        verify(categoryTransactionRepository, times(1)).saveAndFlush(new CategoryTransaction()
                .setId(1L)
                .setCategoryName("newName")
                .setUser(user));
        verify(converter, times(1)).convert(categoryTransaction);
        verifyNoInteractions(userRepository);
    }

    @Test
    public void deleteCategory_CategoryFound() {
        CategoryTransaction categoryTransaction = new CategoryTransaction();
        when(categoryTransactionRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(categoryTransaction));

        assertTrue(subj.deleteCategory(1L, 1L));

        verify(categoryTransactionRepository, times(1)).findByIdAndUserId(1L, 1L);
        verifyNoInteractions(converter);
        verifyNoInteractions(userRepository);
    }

    @Test
    public void deleteCategory_CategoryNotFound() {
        when(categoryTransactionRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        assertFalse(subj.deleteCategory(1L, 1L));

        verify(categoryTransactionRepository, times(1)).findByIdAndUserId(1L, 1L);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(userRepository);
    }
}