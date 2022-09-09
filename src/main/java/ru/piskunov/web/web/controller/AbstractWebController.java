package ru.piskunov.web.web.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.piskunov.web.security.CustomUserDetails;

public class AbstractWebController {
    protected CustomUserDetails currentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
