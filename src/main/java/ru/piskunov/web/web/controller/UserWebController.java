package ru.piskunov.web.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.piskunov.web.service.AuthService;
import ru.piskunov.web.service.dto.UserDTO;
import ru.piskunov.web.web.form.RegistrationForm;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class UserWebController extends AbstractWebController {
    private final AuthService authService;

    @GetMapping("/personal-area")
    public String index(Model model) {
        Optional<UserDTO> user = authService.findById(currentUser().getId());
        if (user.isPresent()) {
            model.addAttribute("userId", user.get().getId());
            model.addAttribute("email", user.get().getEmail());
            model.addAttribute("name", user.get().getUserName());
        }
        return "personal-area";
    }

    @GetMapping("/login-form")
    public String getLogin() {
        return "login-form";
    }


    @GetMapping("/registration")
    public String getRegistration(Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "registration";
    }

    @PostMapping("/registration")
    public String postRegistration(@ModelAttribute("form") @Valid RegistrationForm form,
                                   BindingResult result,
                                   Model model) {
        if (!result.hasErrors()) {
            if (authService.registration(form.getEmail(),
                    form.getPassword(), form.getName()).isPresent()) {
                return "redirect:/login-form";
            }
            result.addError(new FieldError("form", "email", "Уже зарегистрирован в системе"));
        }
        model.addAttribute("form", form);
        return "registration";
    }
}
