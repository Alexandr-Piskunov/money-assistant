package ru.piskunov.web.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.piskunov.web.service.CategoryTransactionService;
import ru.piskunov.web.service.dto.CategoryTransactionDTO;
import ru.piskunov.web.web.form.CategoryForm;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class CategoryTransactionWebController extends AbstractWebController {
    private final CategoryTransactionService categoryTransactionService;

    @GetMapping("/category")
    public String findAll(Model model) {
        model.addAttribute("category", categoryTransactionService.findAll(currentUser().getId())
                .stream()
                .sorted(Comparator.comparing(CategoryTransactionDTO::getId))
                .collect(Collectors.toList()));
        return "category";
    }

    @GetMapping("/category/create")
    public String getCategoryCreate(Model model) {
        model.addAttribute("form", new CategoryForm());
        return "categoryCreate";
    }

    @PostMapping("/category/create")
    public String postCategoryCreate(@ModelAttribute("form") @Valid CategoryForm form,
                                     BindingResult result,
                                     Model model) {
        if (!result.hasErrors()) {
            if (categoryTransactionService.create(form.getName(), currentUser().getId()).isPresent()) {
                return "redirect:/category";
            }
            result.addError(new FieldError("form", "name", "Неверно указано имя категории или уже сть категория с таким именем"));
        }
        model.addAttribute("form", form);
        return "categoryCreate";
    }

    @GetMapping("/category/rename/{id}")
    public String getCategoryRename(Model model, @PathVariable Long id) {
        model.addAttribute("form", new CategoryForm())
                .addAttribute("id", id);
        return "categoryRename";
    }

    @PostMapping("/category/rename/{id}")
    public String postCategoryRename(@ModelAttribute("form") @PathVariable Long id,
                                     @Valid CategoryForm form,
                                     BindingResult result,
                                     Model model) {
        model.addAttribute("id", id);
        if (!result.hasErrors()) {
            if (categoryTransactionService.rename(form.getName(), form.getId(), currentUser().getId()).isPresent()) {
                return "redirect:/category";
            }
            result.addError(new FieldError("form", "name", "Неверное имя категории или такая категория уже существует"));
        }
        model.addAttribute("form", form);
        return "categoryRename";
    }

    @PostMapping("/category/delete/{id}")
    public String postCategoryDelete(@ModelAttribute("form") CategoryForm form) {
        if (categoryTransactionService.deleteCategory(form.getId(), currentUser().getId())) {
            return "redirect:/category";
        }
        return "category";
    }
}
