package ru.piskunov.web.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.piskunov.web.service.CategoryTransactionService;
import ru.piskunov.web.service.ReportCategoryService;
import ru.piskunov.web.service.dto.CategoryTransactionDTO;
import ru.piskunov.web.service.dto.ReportCategoryDTO;
import ru.piskunov.web.web.form.ReportCategoryForm;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class ReportCategoryWebController extends AbstractWebController {
    private final ReportCategoryService reportCategoryService;
    private final CategoryTransactionService categoryTransactionService;
    private Date startDateDefault = Date.valueOf(LocalDate.now().minusMonths(1));
    private Date endDateDefault = Date.valueOf(LocalDate.now());

    @GetMapping("/report-category/create-plus/")
    public String getReportCategoryCreatePlus(@RequestParam(required = false) Long id,
                                              @RequestParam(required = false) String start,
                                              @RequestParam(required = false) String end,
                                              Model model) {
        model.addAttribute("form", new ReportCategoryForm())
                .addAttribute("id", id);

        Optional<CategoryTransactionDTO> category = categoryTransactionService.findById(id);
        if (category.isPresent()) {
            Date startDate = (start != null && !start.isEmpty()) ? Date.valueOf(start) : startDateDefault;
            Date endDate = (end != null && !end.isEmpty()) ? Date.valueOf(end) : endDateDefault;
            Optional<ReportCategoryDTO> reportPlus = reportCategoryService.createReportPlus(currentUser().getId(), (Long) model.getAttribute("id"), startDate, endDate);
            if (reportPlus.isPresent()) {
                model.addAttribute("amount", reportPlus.get().getAmount())
                        .addAttribute("name", reportPlus.get().getName());
            } else {
                model.addAttribute("amount", 0)
                        .addAttribute("name", category.get().getCategoryName());
            }
        } else {
            model.addAttribute("amount", 0)
                    .addAttribute("name", "Произошла ошибка");
        }
        return "reportCategoryCreatePlus";
    }

    @GetMapping("/report-category/create-minus/")
    public String getReportCategoryCreateMinus(@RequestParam(required = false) Long id,
                                               @RequestParam(required = false) String start,
                                               @RequestParam(required = false) String end,
                                               Model model) {
        model.addAttribute("form", new ReportCategoryForm())
                .addAttribute("id", id);

        Optional<CategoryTransactionDTO> category = categoryTransactionService.findById(id);
        if (category.isPresent()) {
            Date startDate = (start != null && !start.isEmpty()) ? Date.valueOf(start) : startDateDefault;
            Date endDate = (end != null && !end.isEmpty()) ? Date.valueOf(end) : endDateDefault;
            Optional<ReportCategoryDTO> reportMinus = reportCategoryService.createReportMinus(currentUser().getId(), (Long) model.getAttribute("id"), startDate, endDate);
            if (reportMinus.isPresent()) {
                model.addAttribute("amount", reportMinus.get().getAmount())
                        .addAttribute("name", reportMinus.get().getName());
            } else {
                model.addAttribute("amount", 0)
                        .addAttribute("name",  category.get().getCategoryName());
            }
        } else {
            model.addAttribute("amount", 0)
                    .addAttribute("name", "Произошла ошибка");
        }
        return "reportCategoryCreateMinus";
    }
}
