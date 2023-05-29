package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @GetMapping
    public String showAdminRootPage(Model model) {
        model.addAttribute("user", userService.getAllUsers());
        model.addAttribute("user1", new User());
        return "admin";
    }


    @PostMapping("/new")
    public String createNewUser(@ModelAttribute("user1") @Valid User user,
                                BindingResult bindingResult, @ModelAttribute("role") @Valid String role) {
        if (bindingResult.hasErrors())
            return "admin";
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user, role);
        return "redirect:/admin/";
    }


    @GetMapping("/{id}/edit")
    public String edit(ModelMap model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.getUserById(id));
        return "edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult, @ModelAttribute("role") String role) {
        if (bindingResult.hasErrors())
            return "edit";
        userService.updateUser(user, role);
        return "redirect:/admin/";
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@ModelAttribute("user") User user, @PathVariable("id") int id) {
        userService.removeUser(id);
        return "redirect:/admin/";
    }
}
