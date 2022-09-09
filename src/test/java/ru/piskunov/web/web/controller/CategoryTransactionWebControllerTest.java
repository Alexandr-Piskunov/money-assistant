package ru.piskunov.web.web.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.piskunov.web.MockSecurityConfiguration;
import ru.piskunov.web.config.SecurityConfiguration;
import ru.piskunov.web.service.CategoryTransactionService;
import ru.piskunov.web.service.dto.CategoryTransactionDTO;
import ru.piskunov.web.service.dto.UserDTO;
import ru.piskunov.web.web.form.CategoryForm;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryTransactionWebController.class)
@Import({SecurityConfiguration.class, MockSecurityConfiguration.class})
@RunWith(SpringRunner.class)
public class CategoryTransactionWebControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CategoryTransactionService categoryTransactionService;

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void findAll() throws Exception {
        UserDTO userDTO = new UserDTO()
                .setId(1L)
                .setUserName("alex")
                .setEmail("alex@yandex.ru");
        List<CategoryTransactionDTO> categoryTransactionDTOS = asList(new CategoryTransactionDTO()
                .setId(1L)
                .setCategoryName("name1")
                .setUserDTO(userDTO), new CategoryTransactionDTO()
                .setId(2L)
                .setCategoryName("name2")
                .setUserDTO(userDTO));
        when(categoryTransactionService.findAll(1L)).thenReturn(categoryTransactionDTOS);
        mockMvc.perform(get("/category"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("category", categoryTransactionDTOS))
                .andExpect(view().name("category"));
    }

    @Test
    public void getCategoryCreate() throws Exception {
        mockMvc.perform(get("/category/create"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("form", new CategoryForm()))
                .andExpect(view().name("categoryCreate"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void postCategoryCreate_categoryFound() throws Exception {
        when(categoryTransactionService.create("name", 1L)).thenReturn(Optional.of(new CategoryTransactionDTO()
                .setId(1L)
                .setCategoryName("name")
                .setUserDTO(new UserDTO()
                        .setId(1L)
                        .setUserName("alex")
                        .setEmail("alex@yandex.ru"))));
        mockMvc.perform(post("/category/create").param("name", "name"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/category"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void postCategoryCreate_categoryNotFound() throws Exception {
        when(categoryTransactionService.create("name", 1L)).thenReturn(Optional.empty());
        mockMvc.perform(post("/category/create").param("name", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("categoryCreate"));
    }

    @Test
    public void getCategoryRename() throws Exception {
        mockMvc.perform(get("/category/rename/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(model().attribute("form", new CategoryForm()))
                .andExpect(model().attribute("id", 1L))
                .andExpect(view().name("categoryRename"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void postCategoryRename_categoryName_isOk() throws Exception {
        when(categoryTransactionService.rename("newname", 1L, 1L)).thenReturn(Optional.of(new CategoryTransactionDTO()
                .setId(1L)
                .setCategoryName("newname")
                .setUserDTO(new UserDTO()
                        .setId(1L)
                        .setUserName("alex")
                        .setEmail("alex@yandex.ru"))));
        mockMvc.perform(post("/category/rename/{id}", 1L).param("name", "newname"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/category"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void postCategoryRename_categoryName_isBad() throws Exception {
        when(categoryTransactionService.rename("newname", 1L, 1L)).thenReturn(Optional.empty());
        mockMvc.perform(post("/category/rename/{id}", 1L).param("name", "newname"))
                .andExpect(status().isOk())
                .andExpect(view().name("categoryRename"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void postCategoryDelete() throws Exception {
        when(categoryTransactionService.deleteCategory(1L, 1L)).thenReturn(true);
        mockMvc.perform(post("/category/delete/{id}", 1L))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/category"));
    }
}