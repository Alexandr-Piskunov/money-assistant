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
import ru.piskunov.web.service.AccountService;
import ru.piskunov.web.service.dto.AccountDTO;
import ru.piskunov.web.web.form.AccountForm;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class AccountWebController extends AbstractWebController {
    private final AccountService accountService;

    @GetMapping("/account")
    public String findAll(Model model) {
        model.addAttribute("account", accountService.findAll(currentUser().getId()).stream()
                .sorted(Comparator.comparing(AccountDTO::getId))
                .collect(Collectors.toList()));
        return "account";
    }

    @GetMapping("/account/create")
    public String getAccountCreate(Model model) {
        model.addAttribute("form", new AccountForm());
        return "accountCreate";
    }

    @PostMapping("/account/create")
    public String postAccountCreate(@ModelAttribute("form") @Valid AccountForm form,
                                    BindingResult result,
                                    Model model) {
        if (!result.hasErrors()) {
            if (accountService.create(currentUser().getId(), form.getName()).isPresent()) {
                return "redirect:/account";
            }
            result.addError(new FieldError("form", "name", "Имя счета указано неверно, или уже имеется счет с таким именем"));
        }
        model.addAttribute("form", form);
        return "accountCreate";
    }

    @GetMapping("/account/rename/{id}")
    public String getAccountRename(Model model, @PathVariable Long id) {
        model.addAttribute("form", new AccountForm())
                .addAttribute("id", id);
        return "accountRename";
    }

    @PostMapping("/account/rename/{id}")
    public String postAccountRename(@ModelAttribute("form") @PathVariable Long id,
                                    @Valid AccountForm form,
                                    BindingResult result,
                                    Model model) {
        model.addAttribute("id", id);
        if (!result.hasErrors()) {
            if (accountService.rename(form.getName(), form.getId(), currentUser().getId()).isPresent()) {
                return "redirect:/account";
            }
            result.addError(new FieldError("form", "name", "Неверное имя счета или такой счет уже существует"));
        }
        model.addAttribute("form", form);
        return "accountRename";
    }

    @PostMapping("/account/delete/{id}")
    public String postAccountDelete(@ModelAttribute("form") AccountForm form) {
        if (accountService.delete(form.getId(), currentUser().getId())) {
            return "redirect:/account";
        }
        return "account";
    }
}
