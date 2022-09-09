package ru.piskunov.web;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.piskunov.web.service.*;
import ru.piskunov.web.service.dto.CategoryTransactionDTO;
import ru.piskunov.web.service.dto.ReportCategoryDTO;
import ru.piskunov.web.service.dto.TransactionDTO;
import ru.piskunov.web.service.dto.UserDTO;

import java.sql.Date;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class ConsoleApplication implements CommandLineRunner {
    private final AuthService authService;
    private final AccountService accountService;
    private final CategoryTransactionService categoryTransactionService;
    private final ReportCategoryService reportCategoryService;
    private final TransactionService transactionService;

    @Override
    public void run(String... args) throws Exception {

        String starMenu = request("Здравствуйте, для авторизации - нажмите 1, для регистрации нового пользователя - нажмите 2");
        if (Integer.parseInt(starMenu) == 1) {
            while (true) {
                String email = request("Введите email");
                String password = request("Введите пароль");
                Optional<UserDTO> auth1 = authService.auth(email, password);
                if (auth1.isPresent()) {
                    System.out.println("Привет " + auth1.get().getUserName() + "! Вот список всех доступных счетов:");
                    accountService.findAll(auth1.get().getId()).forEach(System.out::println);
                    String userMenu = request("Что бы зарегистрировать новый нажми 1 " +
                            "\nЕсли нужно удалить счет - смело нажимай 2" +
                            "\nЧто бы посмотреть список всех категорий транзакций нажми 3" +
                            "\nЧто бы добавить новою категорию для транзакций нажми 4" +
                            "\nЧто бы добавить новую транзакцию нажми 5");
                    if (Integer.parseInt(userMenu) == 1) {
                        String nameNewAccount = request("Введите имя нового счета");
                        if (accountService.create(auth1.get().getId(), nameNewAccount).isPresent()) {
                            System.out.println("Успешно зарегистрирован новый счет: " + nameNewAccount);
                        } else {
                            System.out.println("Не удалось добавить новый счет");
                        }
                    } else if (Integer.parseInt(userMenu) == 2) {
                        String deleteAccount = request("Для удаления, введи ID счета (находится слева от названия счета)");
                        System.out.println(accountService.delete((long) Integer.parseInt(deleteAccount), auth1.get().getId()) ? "Счет удален успешно" : "Неудалось удалить счет");
                    } else if (Integer.parseInt(userMenu) == 3) {
                        categoryTransactionService.findAll(auth1.get().getId()).forEach(System.out::println);
                        String categoryTranzMenu = request("Что бы удалить категорию нажми 1\nЧто бы переименовать категорию нажми 2" +
                                "\nЧто бы посмотреть отчет по категории нажмите 3");
                        if (Integer.parseInt(categoryTranzMenu) == 1) {
                            String deleteCategoryId = request("Для удаления, введи ID (находится слева от названия нужной категории)");
                            System.out.println(categoryTransactionService.deleteCategory((long) Integer.parseInt(deleteCategoryId), auth1.get().getId()) ? "Удаление прошло успешно" : "Удалить неудалось, введен некорректный ID");
                        } else if (Integer.parseInt(categoryTranzMenu) == 2) {
                            String newName = request("Введи новое название категории");
                            long idRename = Long.parseLong(request("Введи ID категории которую нужно переименовать (находится слева от названия нужной категории)"));
                            Optional<CategoryTransactionDTO> categoryTransactionDTO = categoryTransactionService.rename(newName, idRename, auth1.get().getId());
                            if (categoryTransactionDTO.isPresent()) {
                                System.out.println(categoryTransactionDTO.get());
                            } else {
                                System.out.println("Не удалось переименовать категорию");
                            }
                        } else if (Integer.parseInt(categoryTranzMenu) == 3) {
                            String condition = request("Для получения отчета о доходах нажмите 1, если нужны расходы то - 2");
                            String categoryId = request("Введите ID категории");
                            String fromDate = request("Введите начало промежутка");
                            String endDate = request("Введите конец промежутка");

                            if (Integer.parseInt(condition) == 1) {
                                Optional<ReportCategoryDTO> revenue = reportCategoryService.createReportPlus(auth1.get().getId(), Long.parseLong(categoryId), Date.valueOf(fromDate), Date.valueOf(endDate));
                                if (revenue.isPresent()) {
                                    System.out.println(revenue.get());
                                } else {
                                    System.out.println("Не удалось");
                                }
                                return;
                            } else if (Integer.parseInt(condition) == 2) {
                                Optional<ReportCategoryDTO> revenue = reportCategoryService.createReportMinus(auth1.get().getId(), Long.parseLong(categoryId), Date.valueOf(fromDate), Date.valueOf(endDate));
                                if (revenue.isPresent()) {
                                    System.out.println(revenue.get());
                                } else {
                                    System.out.println("Не удалось");
                                }
                                return;
                            }
                        }
                    } else if (Integer.parseInt(userMenu) == 4) {
                        String newCategory = request("Введите имя для новой категории");
                        Optional<CategoryTransactionDTO> categoryTransactionDTO = categoryTransactionService.create(newCategory, auth1.get().getId());
                        categoryTransactionDTO.ifPresent(System.out::println);
                    } else if (Integer.parseInt(userMenu) == 5) {
                        String fromAccount = request("Введите id счета с которого перевести деньги");
                        String toAccount = request("Введите id счета на которой перевести");
                        String amount = request("Введите сумму");
                        String[] categoryid = request("Введите категории через запятую").split("\\,");
                        Optional<TransactionDTO> transactionDTO = transactionService.create(auth1.get().getId(), Long.parseLong(amount),
                                fromAccount.isEmpty() ? null : Long.parseLong(fromAccount),
                                toAccount.isEmpty() ? null : Long.parseLong(toAccount), Arrays.stream(categoryid).map(Long::valueOf).collect(Collectors.toList()));
                        if (transactionDTO.isPresent()) {
                            System.out.println(transactionDTO.get());
                        } else {
                            System.out.println("Не удалось добавить транзакцию");
                        }
                    }
                } else {
                    System.out.println("Не удалось авторизоваться, введен неверный email или пароль");
                }
            }
        } else {
            String email = request("Введите email");
            String password = request("Ведите пароль");
            String userName = request("Введите имя пользователя");
            Optional<UserDTO> registration = authService.registration(email, password, userName);
            if (registration.isPresent()) {
                UserDTO newUser = registration.get();
                System.out.println(newUser);
            } else {
                System.out.println("Не удалось зарегистрировать пользователя, этот email уже используется");
            }
        }
    }

    static String request(String value) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(value);
        return scanner.nextLine();
    }
}
