package ru.kata.spring.boot_security.demo.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.exception.UserAlreadyExistsException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Lazy
    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id);
    }


    @Override
    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByEmail(user.getUsername());
        if (userFromDB != null) {
            throw new UserAlreadyExistsException("User Already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean saveUser(User user, String role) {
        User userFromDB = userRepository.findByEmail(user.getUsername());
        if (userFromDB != null) {
            throw new UserAlreadyExistsException("User Already exist");
        }
        if (role.equals("ADMIN")) {
            user.setRoles(Set.of(new Role(1L, "ROLE_ADMIN"), new Role(2L, "ROLE_USER")));
        } else {
            user.setRoles(Collections.singleton(new Role(2L, "ROLE_USER")));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public void removeUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateUser(User user, String role) {
        User userBas = getUserById(user.getId());
        if(!userBas.getPassword().equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if(!userBas.getEmail().equals(user.getEmail())){
            throw new UserAlreadyExistsException("User Already exist");
        }
        if (role.equals("ADMIN")) {
            user.setRoles(Set.of(new Role(1L, "ROLE_ADMIN"), new Role(2L, "ROLE_USER")));
        } else if (role.equals("USER")){
            user.setRoles(Collections.singleton(new Role(2L, "ROLE_USER")));
        } else {
            user.setRoles(userBas.getRoles());
        }
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.getUserDetails();
    }

    public User loadUserByUserEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
    public void saveRole(Role role) {
        roleRepository.save(role);
    }
}
