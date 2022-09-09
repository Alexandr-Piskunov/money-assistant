package ru.piskunov.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@RequiredArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority {
    private final static String PREFIX = "ROLE_";
    private final UserRole userRole;

    @Override
    public String getAuthority() {
        return PREFIX + userRole.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomGrantedAuthority that = (CustomGrantedAuthority) o;
        return userRole == that.userRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userRole);
    }
}
