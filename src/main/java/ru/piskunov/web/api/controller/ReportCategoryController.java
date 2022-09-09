package ru.piskunov.web.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.piskunov.web.api.json.ReportCategoryRequest;
import ru.piskunov.web.api.json.ReportCategoryResponse;
import ru.piskunov.web.service.ReportCategoryService;

import javax.validation.Valid;
import java.sql.Date;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportCategoryController extends AbstractApiController {
    private final ReportCategoryService reportCategoryService;

    @PostMapping("/report-category/create-plus")
    public ResponseEntity<ReportCategoryResponse> createReportPlus(@RequestBody @Valid ReportCategoryRequest request) {
        return reportCategoryService.createReportPlus(currentUser().getId(), request.getId(),
                        Date.valueOf(request.getStartDate()), Date.valueOf(request.getEndDate()))
                .map(reportCategoryDTO -> ok(new ReportCategoryResponse(reportCategoryDTO.getName(), reportCategoryDTO.getAmount())))
                .orElseGet(() -> status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/report-category/create-minus")
    public ResponseEntity<ReportCategoryResponse> createReportMinus(@RequestBody @Valid ReportCategoryRequest request) {
        return reportCategoryService.createReportMinus(currentUser().getId(), request.getId(),
                        Date.valueOf(request.getStartDate()), Date.valueOf(request.getEndDate()))
                .map(reportCategoryDTO -> ok(new ReportCategoryResponse(reportCategoryDTO.getName(), reportCategoryDTO.getAmount())))
                .orElseGet(() -> status(HttpStatus.BAD_REQUEST).build());
    }
}
