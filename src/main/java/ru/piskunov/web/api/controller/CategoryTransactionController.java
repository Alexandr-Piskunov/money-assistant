package ru.piskunov.web.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.piskunov.web.api.json.CategoryTransactionDeleteResponse;
import ru.piskunov.web.api.json.CategoryTransactionRequest;
import ru.piskunov.web.api.json.CategoryTransactionResponse;
import ru.piskunov.web.service.CategoryTransactionService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryTransactionController extends AbstractApiController {
    private final CategoryTransactionService categoryTransactionService;

    @GetMapping("/category")
    public List<ResponseEntity<CategoryTransactionResponse>> getAllCategoryByUserId() {
        return categoryTransactionService.findAll(currentUser().getId()).stream().map(categoryTransactionDTO -> ok(new CategoryTransactionResponse(categoryTransactionDTO.getId(),
                categoryTransactionDTO.getUserDTO(), categoryTransactionDTO.getCategoryName()))).collect(Collectors.toList());
    }

    @PostMapping("/category/create")
    public ResponseEntity<CategoryTransactionResponse> create(@RequestBody CategoryTransactionRequest categoryTransactionRequest) {
        return categoryTransactionService.create(categoryTransactionRequest.getCategoryName(), currentUser().getId())
                .map(transactionDTO -> ResponseEntity.ok(new CategoryTransactionResponse(transactionDTO.getId(),
                        transactionDTO.getUserDTO(), transactionDTO.getCategoryName())))
                .orElseGet(() -> status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/category/rename")
    public ResponseEntity<CategoryTransactionResponse> rename(@RequestBody CategoryTransactionRequest categoryTransactionRequest) {
        return categoryTransactionService.rename(categoryTransactionRequest.getCategoryName(), categoryTransactionRequest.getId(), currentUser().getId())
                .map(categoryTransactionDTO -> ok(new CategoryTransactionResponse(categoryTransactionDTO.getId(),
                        categoryTransactionDTO.getUserDTO(), categoryTransactionDTO.getCategoryName())))
                .orElseGet(() -> status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/category/delete")
    public ResponseEntity<CategoryTransactionDeleteResponse> delete(@RequestBody CategoryTransactionRequest categoryTransactionRequest) {
        return ok(new CategoryTransactionDeleteResponse(categoryTransactionService.deleteCategory(categoryTransactionRequest.getId(), currentUser().getId())));
    }
}
