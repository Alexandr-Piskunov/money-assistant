package ru.piskunov.web.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.piskunov.web.api.json.AccountDeleteResponse;
import ru.piskunov.web.api.json.AccountRequest;
import ru.piskunov.web.api.json.AccountResponse;
import ru.piskunov.web.service.AccountService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController extends AbstractApiController {
    private final AccountService accountService;

    @GetMapping("/account")
    public List<ResponseEntity<AccountResponse>> getAllAccountByUserId() {
        return accountService.findAll(currentUser().getId()).stream()
                .map(accountDTO -> ok(new AccountResponse(accountDTO.getId(), accountDTO.getAccountName(), accountDTO.getBalance(), accountDTO.getUserDTO())))
                .collect(Collectors.toList());
    }

    @PostMapping("/account/create")
    public ResponseEntity<AccountResponse> create(@RequestBody @Valid AccountRequest accountRequest) {
        return accountService.create(currentUser().getId(), accountRequest.getName())
                .map(dto -> ok(new AccountResponse(dto.getId(), dto.getAccountName(), dto.getBalance(), dto.getUserDTO())))
                .orElseGet(() -> status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/account/rename")
    public ResponseEntity<AccountResponse> rename(@RequestBody @Valid AccountRequest accountRequest) {
        return accountService.rename(accountRequest.getName(), accountRequest.getId(), currentUser().getId())
                .map(accountDTO -> ok(new AccountResponse(accountDTO.getId(), accountDTO.getAccountName(),
                        accountDTO.getBalance(), accountDTO.getUserDTO())))
                .orElseGet(() -> status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/account/delete")
    public ResponseEntity<AccountDeleteResponse> delete(@RequestBody AccountRequest accountRequest) {
        return ok(new AccountDeleteResponse(accountService.delete(accountRequest.getId(), currentUser().getId())));
    }
}

