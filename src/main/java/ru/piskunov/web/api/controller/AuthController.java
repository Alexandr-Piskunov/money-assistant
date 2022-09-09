package ru.piskunov.web.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.piskunov.web.api.json.AuthRequest;
import ru.piskunov.web.api.json.AuthResponse;
import ru.piskunov.web.service.AuthService;
import ru.piskunov.web.service.dto.UserDTO;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController extends AbstractApiController {
    private final AuthService authService;

    @GetMapping("/personal-area")
    public ResponseEntity<AuthResponse> getPersonalArea() {
        Optional<UserDTO> user = authService.findById(currentUser().getId());
        return ok(new AuthResponse(user.get().getId(), user.get().getEmail(), user.get().getUserName()));
    }

    @PostMapping("/registration")
    public ResponseEntity<AuthResponse> registration(@RequestBody @Valid AuthRequest authRequest) {
        return authService.registration(authRequest.getEmail(), authRequest.getPassword(), authRequest.getUserName())
                .map(userDTO -> ok(new AuthResponse(userDTO.getId(), userDTO.getEmail(), userDTO.getUserName())))
                .orElseGet(() -> status(HttpStatus.BAD_REQUEST).build());
    }
}