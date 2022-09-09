package ru.piskunov.web.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.piskunov.web.converter.ReportCategoryModelToReportCategoryDTOConverter;
import ru.piskunov.web.dao.ReportCategoryDao;
import ru.piskunov.web.dao.ReportCategoryModel;
import ru.piskunov.web.service.dto.ReportCategoryDTO;

import java.sql.Date;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReportCategoryModelServiceTest {

    @InjectMocks
    ReportCategoryService subj;

    @Mock
    ReportCategoryDao reportCategoryDao;

    @Mock
    ReportCategoryModelToReportCategoryDTOConverter converter;

    @Test
    public void createReportMinus_ReportNotFound() {
        when(reportCategoryDao.createReportMinus(1L, 1L, Date.valueOf("2022-03-27"), Date.valueOf("2022-03-30"))).thenReturn(Optional.empty());

        assertFalse(subj.createReportMinus(1L, 1L, Date.valueOf("2022-03-27"), Date.valueOf("2022-03-30")).isPresent());

        verify(reportCategoryDao, times(1)).createReportMinus(1L, 1L, Date.valueOf("2022-03-27"), Date.valueOf("2022-03-30"));
        verifyNoInteractions(converter);
    }

    @Test
    public void createReportPlus_ReportNotFound() {
        when(reportCategoryDao.createReportPlus(1L, 1L, Date.valueOf("2022-03-27"), Date.valueOf("2022-03-30"))).thenReturn(Optional.empty());

        assertFalse(subj.createReportPlus(1L, 1L, Date.valueOf("2022-03-27"), Date.valueOf("2022-03-30")).isPresent());

        verify(reportCategoryDao, times(1)).createReportPlus(1L, 1L, Date.valueOf("2022-03-27"), Date.valueOf("2022-03-30"));
        verifyNoInteractions(converter);
    }

    @Test
    public void createReportPlus_ReportFound() {
        ReportCategoryModel reportCategoryModel = new ReportCategoryModel()
                .setName("ReportName")
                .setAmount(20000L);
        when(reportCategoryDao.createReportPlus(1L, 1L, Date.valueOf("2022-03-27"), Date.valueOf("2022-03-30"))).thenReturn(Optional.of(reportCategoryModel));
        ReportCategoryDTO reportCategoryDTO = new ReportCategoryDTO();
        when(converter.convert(reportCategoryModel)).thenReturn(reportCategoryDTO);
        Optional<ReportCategoryDTO> dto = subj.createReportPlus(1L, 1L, Date.valueOf("2022-03-27"), Date.valueOf("2022-03-30"));

        assertTrue(dto.isPresent());
        assertEquals(reportCategoryDTO, dto.get());

        verify(reportCategoryDao, times(1)).createReportPlus(1L, 1L, Date.valueOf("2022-03-27"), Date.valueOf("2022-03-30"));
        verify(converter, times(1)).convert(reportCategoryModel);
    }

    @Test
    public void createReportMinus_ReportFound() {
        ReportCategoryModel reportCategoryModel = new ReportCategoryModel()
                .setName("ReportName")
                .setAmount(20000L);
        when(reportCategoryDao.createReportMinus(1L, 1L, Date.valueOf("2022-03-27"), Date.valueOf("2022-03-30"))).thenReturn(Optional.of(reportCategoryModel));
        ReportCategoryDTO reportCategoryDTO = new ReportCategoryDTO();
        when(converter.convert(reportCategoryModel)).thenReturn(reportCategoryDTO);
        Optional<ReportCategoryDTO> dto = subj.createReportMinus(1L, 1L, Date.valueOf("2022-03-27"), Date.valueOf("2022-03-30"));

        assertTrue(dto.isPresent());
        assertEquals(reportCategoryDTO, dto.get());

        verify(reportCategoryDao, times(1)).createReportMinus(1L, 1L, Date.valueOf("2022-03-27"), Date.valueOf("2022-03-30"));
        verify(converter, times(1)).convert(reportCategoryModel);
    }
}