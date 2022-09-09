package ru.piskunov.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.piskunov.web.dao.ReportCategoryModel;
import ru.piskunov.web.service.dto.ReportCategoryDTO;

@Component
public class ReportCategoryModelToReportCategoryDTOConverter implements Converter<ReportCategoryModel, ReportCategoryDTO> {

    @Override
    public ReportCategoryDTO convert(ReportCategoryModel source) {
        ReportCategoryDTO reportCategoryDTO = new ReportCategoryDTO();
        reportCategoryDTO.setName(source.getName());
        reportCategoryDTO.setAmount(source.getAmount());
        return reportCategoryDTO;
    }
}
