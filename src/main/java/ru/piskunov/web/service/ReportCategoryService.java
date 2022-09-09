package ru.piskunov.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.piskunov.web.converter.ReportCategoryModelToReportCategoryDTOConverter;
import ru.piskunov.web.dao.ReportCategoryDao;
import ru.piskunov.web.service.dto.ReportCategoryDTO;

import java.sql.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportCategoryService {
    private final ReportCategoryDao reportCategoryDao;
    private final ReportCategoryModelToReportCategoryDTOConverter converter;

    @Transactional
    public Optional<ReportCategoryDTO> createReportPlus(Long userId, Long categoryId, Date startDate, Date endDate) {
        return reportCategoryDao.createReportPlus(userId, categoryId, startDate, endDate).map(converter::convert);
    }

    @Transactional
    public Optional<ReportCategoryDTO> createReportMinus(Long userId, Long categoryId, Date startDate, Date endDate) {
        return reportCategoryDao.createReportMinus(userId, categoryId, startDate, endDate).map(converter::convert);
    }

}
