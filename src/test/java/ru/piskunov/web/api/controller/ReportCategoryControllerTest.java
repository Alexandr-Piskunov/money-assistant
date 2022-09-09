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
import ru.piskunov.web.service.ReportCategoryService;
import ru.piskunov.web.service.dto.ReportCategoryDTO;

import java.sql.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportCategoryController.class)
@Import({SecurityConfiguration.class, MockSecurityConfiguration.class})
@RunWith(SpringRunner.class)
public class ReportCategoryControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReportCategoryService reportCategoryService;

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void createReportPlus_reportIsOk() throws Exception {
        Date startDate = Date.valueOf("2022-05-20");
        Date endDate = Date.valueOf("2022-05-21");
        ReportCategoryDTO reportCategoryDTO = new ReportCategoryDTO()
                .setName("test")
                .setAmount(222L);
        when(reportCategoryService.createReportPlus(1L, 1L, startDate, endDate)).thenReturn(Optional.of(reportCategoryDTO));
        mockMvc.perform(post("/api/report-category/create-plus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"startDate\": \"2022-05-20\",\n" +
                                "  \"endDate\": \"2022-05-21\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"name\": \"test\",\n" +
                        "  \"amount\": 222\n" +
                        "}"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void createReportPlus_reportIsBad() throws Exception {
        Date startDate = Date.valueOf("2022-05-20");
        Date endDate = Date.valueOf("2022-05-21");
        when(reportCategoryService.createReportPlus(1L, 1L, startDate, endDate)).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/report-category/create-plus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"startDate\": \"2022-05-20\",\n" +
                                "  \"endDate\": \"2022-05-21\"\n" +
                                "}"))
                .andExpect(status().is(400));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void createReportMinus_reportIsOk() throws Exception {
        Date startDate = Date.valueOf("2022-05-20");
        Date endDate = Date.valueOf("2022-05-21");
        ReportCategoryDTO reportCategoryDTO = new ReportCategoryDTO()
                .setName("test")
                .setAmount(222L);
        when(reportCategoryService.createReportMinus(1L, 1L, startDate, endDate)).thenReturn(Optional.of(reportCategoryDTO));
        mockMvc.perform(post("/api/report-category/create-minus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"startDate\": \"2022-05-20\",\n" +
                                "  \"endDate\": \"2022-05-21\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"name\": \"test\",\n" +
                        "  \"amount\": 222\n" +
                        "}"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void createReportMinus_reportIsBad() throws Exception {
        Date startDate = Date.valueOf("2022-05-20");
        Date endDate = Date.valueOf("2022-05-21");
        when(reportCategoryService.createReportMinus(1L, 1L, startDate, endDate)).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/report-category/create-minus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"startDate\": \"2022-05-20\",\n" +
                                "  \"endDate\": \"2022-05-21\"\n" +
                                "}"))
                .andExpect(status().is(400));
    }
}