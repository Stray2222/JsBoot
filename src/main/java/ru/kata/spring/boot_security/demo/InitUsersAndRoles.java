package ru.kata.spring.boot_security.demo;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Component
public class InitUsersAndRoles {
    UserService userService;

    public InitUsersAndRoles(UserService userService) {
        this.userService = userService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {

        if (userService.getAllRoles().size() < 1) {
            System.out.println("I am running........");
            Role admin = new Role(1L, "ROLE_ADMIN");
            Role user = new Role(2L, "ROLE_USER");
            userService.saveRole(admin);
            userService.saveRole(user);
            Set<Role> roles = new HashSet<>();

            roles.add(user);
            User newUser = new User(1L, "USER", "Katya", 20, "USER@MAIL.RU", "USER", "USER", roles);

            roles = new HashSet<>();
            roles.add(user);
            roles.add(admin);
            User newAdmin = new User(2L, "ADMIN", "Misha", 20, "ADMIN@MAIL.RU", "ADMIN", "ADMIN", roles);


            userService.saveUser(newUser);
            userService.saveUser(newAdmin);

            System.out.println("hello, I have just create few users: \n" +
                    "username: USER@MAIL.RU      password: USER \n" +
                    "username: ADMIN@MAIL.RU      password: ADMIN ");
        }
    }
}
