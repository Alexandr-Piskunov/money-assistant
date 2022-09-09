package ru.piskunov.web.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.piskunov.web.api.json.TransactionRequest;
import ru.piskunov.web.api.json.TransactionResponse;
import ru.piskunov.web.service.TransactionService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TransactionController extends AbstractApiController {
    private final TransactionService transactionService;

    @PostMapping("/transaction/create")
    public ResponseEntity<TransactionResponse> create(@RequestBody @Valid TransactionRequest transactionRequest) {
        return transactionService.create(currentUser().getId(), transactionRequest.getAmount(), transactionRequest.getFromAccountId(), transactionRequest.getToAccountId(),
                        Arrays.stream(transactionRequest.getCategoryTransactionId().split("\\,")).map(Long::valueOf).collect(Collectors.toList()))
                .map(dto -> ResponseEntity.ok(new TransactionResponse(dto.getId(),
                        dto.getAmount(),
                        dto.getFromAccount(),
                        dto.getToAccount(),
                        dto.getDateAndTime(),
                        dto.getCategoryTransactionDTOS()))).orElseGet(() -> status(HttpStatus.BAD_REQUEST).build());
    }
}
