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
import ru.piskunov.web.service.ReportCategoryService;
import ru.piskunov.web.service.dto.CategoryTransactionDTO;
import ru.piskunov.web.service.dto.ReportCategoryDTO;
import ru.piskunov.web.service.dto.UserDTO;

import java.sql.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportCategoryWebController.class)
@Import({SecurityConfiguration.class, MockSecurityConfiguration.class})
@RunWith(SpringRunner.class)
public class ReportCategoryWebControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReportCategoryService reportCategoryService;

    @MockBean
    CategoryTransactionService categoryTransactionService;

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void getReportCategoryCreatePlus_reportFound() throws Exception {
        when(categoryTransactionService.findById(1L)).thenReturn(Optional.of(new CategoryTransactionDTO()
                .setCategoryName("test")
                .setId(1L)
                .setUserDTO(new UserDTO()
                        .setUserName("alex")
                        .setEmail("alex@yandex.ru"))));
        when(reportCategoryService.createReportPlus(1L, 1L, Date.valueOf("2022-05-20"), Date.valueOf("2022-05-21"))).thenReturn(Optional.of(new ReportCategoryDTO()
                .setName("test")
                .setAmount(222L)));
        mockMvc.perform(get("/report-category/create-plus/").param("id", "1")
                        .param("start", "2022-05-20")
                        .param("end", "2022-05-21"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("amount", 222L))
                .andExpect(model().attribute("name", "test"))
                .andExpect(view().name("reportCategoryCreatePlus"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void getReportCategoryCreatePlus_reportNotFound() throws Exception {
        Date startDate = Date.valueOf("2022-05-20");
        Date endDate = Date.valueOf("2022-05-21");
        when(categoryTransactionService.findById(1L)).thenReturn(Optional.of(new CategoryTransactionDTO()
                .setCategoryName("test")
                .setId(1L)
                .setUserDTO(new UserDTO()
                        .setUserName("alex")
                        .setEmail("alex@yandex.ru"))));
        when(reportCategoryService.createReportPlus(1L, 1L, startDate, endDate)).thenReturn(Optional.empty());
        mockMvc.perform(get("/report-category/create-plus/").param("id", "1")
                        .param("start", "2022-05-20")
                        .param("end", "2022-05-21"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("amount", 0))
                .andExpect(model().attribute("name", "test"))
                .andExpect(view().name("reportCategoryCreatePlus"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void getReportCategoryCreatePlus_categoryNotFound() throws Exception {
        Date startDate = Date.valueOf("2022-05-20");
        Date endDate = Date.valueOf("2022-05-21");
        when(categoryTransactionService.findById(2L)).thenReturn(Optional.empty());
        when(reportCategoryService.createReportPlus(1L, 2L, startDate, endDate)).thenReturn(Optional.empty());
        mockMvc.perform(get("/report-category/create-plus/").param("id", "2")
                        .param("start", "2022-05-20")
                        .param("end", "2022-05-21"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("amount", 0))
                .andExpect(model().attribute("name", "Произошла ошибка"))
                .andExpect(view().name("reportCategoryCreatePlus"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void getReportCategoryCreateMinus_reportFound() throws Exception {
        when(categoryTransactionService.findById(1L)).thenReturn(Optional.of(new CategoryTransactionDTO()
                .setCategoryName("test")
                .setId(1L)
                .setUserDTO(new UserDTO()
                        .setUserName("alex")
                        .setEmail("alex@yandex.ru"))));
        when(reportCategoryService.createReportMinus(1L, 1L, Date.valueOf("2022-05-20"), Date.valueOf("2022-05-21"))).thenReturn(Optional.of(new ReportCategoryDTO()
                .setName("test")
                .setAmount(222L)));
        mockMvc.perform(get("/report-category/create-minus/").param("id", "1")
                        .param("start", "2022-05-20")
                        .param("end", "2022-05-21"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("amount", 222L))
                .andExpect(model().attribute("name", "test"))
                .andExpect(view().name("reportCategoryCreateMinus"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void getReportCategoryCreateMinus_reportNotFound() throws Exception {
        when(categoryTransactionService.findById(1L)).thenReturn(Optional.of(new CategoryTransactionDTO()
                .setCategoryName("test")
                .setId(1L)
                .setUserDTO(new UserDTO()
                        .setUserName("alex")
                        .setEmail("alex@yandex.ru"))));
        when(reportCategoryService.createReportMinus(1L, 1L, Date.valueOf("2022-05-20"), Date.valueOf("2022-05-21"))).thenReturn(Optional.empty());
        mockMvc.perform(get("/report-category/create-minus/").param("id", "1")
                        .param("start", "2022-05-20")
                        .param("end", "2022-05-21"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("amount", 0))
                .andExpect(model().attribute("name", "test"))
                .andExpect(view().name("reportCategoryCreateMinus"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void getReportCategoryCreateMinus_categoryNotFound() throws Exception {
        when(categoryTransactionService.findById(2L)).thenReturn(Optional.empty());
        when(reportCategoryService.createReportMinus(1L, 2L, Date.valueOf("2022-05-20"), Date.valueOf("2022-05-21"))).thenReturn(Optional.empty());
        mockMvc.perform(get("/report-category/create-minus/").param("id", "2")
                        .param("start", "2022-05-20")
                        .param("end", "2022-05-21"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("amount", 0))
                .andExpect(model().attribute("name", "Произошла ошибка"))
                .andExpect(view().name("reportCategoryCreateMinus"));
    }
}