package ru.piskunov.web.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.piskunov.web.service.AccountService;
import ru.piskunov.web.service.CategoryTransactionService;
import ru.piskunov.web.service.TransactionService;
import ru.piskunov.web.web.form.TransactionForm;

import java.util.Arrays;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class TransactionWebController extends AbstractWebController {
    private final TransactionService transactionService;
    private final AccountService accountService;
    private final CategoryTransactionService categoryTransactionService;

    @GetMapping("/transaction/create")
    public String getTransactionCreate(@RequestParam(required = false) Long fromAccountId,
                                       @RequestParam(required = false) Long toAccountId,
                                       @RequestParam(required = false) Long amount,
                                       @RequestParam(required = false) String[] categoryId,
                                       Model model) {
        if (categoryId != null && amount != null) {
            if (amount.compareTo(0L) >= 0) {
                if (transactionService.create(currentUser().getId(), amount, fromAccountId, toAccountId,
                        Arrays.stream(categoryId).map(Long::valueOf).collect(Collectors.toList())).isPresent()) {
                    return "redirect:/account";
                } else {
                    model.addAttribute("error", "Произошла ошибка");
                }
            } else {
                model.addAttribute("badAmount", "Неверная сумма");
            }
        } else {
            model.addAttribute("check", "Напоминание");
        }
        model.addAttribute("form", new TransactionForm())
                .addAttribute("accounts", accountService.findAll(currentUser().getId()))
                .addAttribute("categories", categoryTransactionService.findAll(currentUser().getId()));
        return "transactionCreate";
    }
}
