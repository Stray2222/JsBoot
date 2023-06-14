package ru.kata.spring.boot_security.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.exception.UserAlreadyExistsException;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    public AdminController( UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showAdminRootPage(Model model, Principal principal) {
        model.addAttribute("user", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        model.addAttribute("userAuthorities", userService.loadUserByUserEmail(principal.getName()));
        return "admin";
    }

    @ExceptionHandler()
    public ResponseEntity<String> handle(UserAlreadyExistsException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.PAYMENT_REQUIRED);
    }

    @PostMapping("/new")
    public String createNewUser(@ModelAttribute("user1") @Valid User newUser,
                                BindingResult bindingResult, @ModelAttribute("role") @Valid String role) {
        if (bindingResult.hasErrors())
            return "admin";
        userService.saveUser(newUser, role);
        return "redirect:/admin/";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult, @ModelAttribute("role") String role) {
        if (bindingResult.hasErrors())
            return "admin";
        userService.updateUser(user, role);
        return "redirect:/admin/";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteUser(@ModelAttribute("user") User deletedUser, @PathVariable("id") long id) {
        userService.removeUser(id);
        return "redirect:/admin/";
    }
}