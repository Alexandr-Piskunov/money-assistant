package ru.piskunov.web.api.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.piskunov.web.security.CustomUserDetails;

public class AbstractApiController {

    protected CustomUserDetails currentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
