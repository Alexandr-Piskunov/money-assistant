package ru.piskunov.web.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.piskunov.web.security.UserRole;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;

import static java.util.Collections.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_name")
    private String userName;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role")
    @Column(name = "role")
    private Set<UserRole> roles = emptySet();
}
