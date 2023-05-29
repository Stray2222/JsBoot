package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;


import java.util.List;


public interface UserService extends UserDetailsService {
    List<User> getAllUsers();

    List<Role> getAllRoles();

    User getUserById(int id);

    boolean saveUser(User user);

    boolean saveUser(User user, String role);

    public void saveRole(Role role);

    void removeUser(int id);

    void updateUser(User user, String role);

    User loadUserByUserEmail(String email);
}