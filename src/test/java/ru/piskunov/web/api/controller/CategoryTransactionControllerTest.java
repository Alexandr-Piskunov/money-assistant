package ru.piskunov.web.api.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.piskunov.web.MockSecurityConfiguration;
import ru.piskunov.web.config.SecurityConfiguration;
import ru.piskunov.web.service.CategoryTransactionService;
import ru.piskunov.web.service.dto.CategoryTransactionDTO;
import ru.piskunov.web.service.dto.UserDTO;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryTransactionController.class)
@Import({SecurityConfiguration.class, MockSecurityConfiguration.class})
@RunWith(SpringRunner.class)
public class CategoryTransactionControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CategoryTransactionService categoryTransactionService;

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void getAllCategoryByUserId() throws Exception {
        List<CategoryTransactionDTO> list = asList(new CategoryTransactionDTO()
                .setId(1L)
                .setCategoryName("test")
                .setUserDTO(new UserDTO()
                        .setUserName("alex")
                        .setEmail("alex@yandex.ru")
                        .setId(1L)));
        when(categoryTransactionService.findAll(1L)).thenReturn(list);
        mockMvc.perform(get("/api/category"))
                .andExpect(status().isOk())
                .andExpect(content().json("[\n" +
                        "  {\n" +
                        "    \"headers\": {},\n" +
                        "    \"body\": {\n" +
                        "      \"id\": 1,\n" +
                        "      \"userDTO\": {\n" +
                        "        \"id\": 1,\n" +
                        "        \"email\": \"alex@yandex.ru\",\n" +
                        "        \"userName\": \"alex\"\n" +
                        "      },\n" +
                        "      \"categoryName\": \"test\"\n" +
                        "    },\n" +
                        "    \"statusCode\": \"OK\",\n" +
                        "    \"statusCodeValue\": 200\n" +
                        "  " +
                        "}\n" +
                        "]"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void create_categoryFound() throws Exception {
        when(categoryTransactionService.create("newname", 1L)).thenReturn(Optional.of(new CategoryTransactionDTO()
                .setId(1L)
                .setCategoryName("newname")
                .setUserDTO(new UserDTO()
                        .setUserName("alex")
                        .setEmail("alex@yandex.ru")
                        .setId(1L))));
        mockMvc.perform(post("/api/category/create").
                        contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryName\": \"newname\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"id\": 1,\n" +
                        "  \"categoryName\": \"newname\",\n" +
                        "  \"userDTO\": {\n" +
                        "    \"id\": 1,\n" +
                        "    \"email\": \"alex@yandex.ru\",\n" +
                        "    \"userName\": \"alex\"\n" +
                        "  }\n" +
                        "}"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void create_categoryNotFound() throws Exception {
        when(categoryTransactionService.create("newname", 1L)).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/category/create").
                        contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryName\": \"newname\"}"))
                .andExpect(status().is(400));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void rename_categoryFound() throws Exception {
        when(categoryTransactionService.rename("newname", 1L, 1L)).thenReturn(Optional.of(
                new CategoryTransactionDTO()
                        .setId(1L)
                        .setCategoryName("newname")
                        .setUserDTO(new UserDTO()
                                .setUserName("alex")
                                .setEmail("alex@yandex.ru")
                                .setId(1L))));
        mockMvc.perform(post("/api/category/rename")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"categoryName\": \"newname\"" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"id\": 1,\n" +
                        "  \"categoryName\": \"newname\",\n" +
                        "  \"userDTO\": {\n" +
                        "    \"id\": 1,\n" +
                        "    \"email\": \"alex@yandex.ru\",\n" +
                        "    \"userName\": \"alex\"\n" +
                        "  }\n" +
                        "}"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void rename_categoryNotFound() throws Exception {
        when(categoryTransactionService.rename("newname", 1L, 1L)).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/category/rename")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"categoryName\": \"newname\"" +
                                "}"))
                .andExpect(status().is(400));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void delete_isTrue() throws Exception {
        when(categoryTransactionService.deleteCategory(1L, 1L)).thenReturn(true);
        mockMvc.perform(post("/api/category/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 1\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"result\": true\n" +
                        "}"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void delete_isFalse() throws Exception {
        when(categoryTransactionService.deleteCategory(1L, 1L)).thenReturn(false);
        mockMvc.perform(post("/api/category/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 1\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"result\": false\n" +
                        "}"));
    }
}