package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();

    List<User> getAllRoles();

    void saveRole(Role role);

    User getUserById(long id);
    boolean saveUser(User user);

    boolean saveUser(User user, String role);

    void removeUser(long id);

    void updateUser(User user, String role);

    User loadUserByUserEmail(String email);
}











