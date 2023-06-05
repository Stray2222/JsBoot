package ru.kata.spring.boot_security.demo.models;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Data
@Entity
@Table(name = "t_role")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "role")
    private String role;

    public Role() {
    }

    public Role(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    public String getAuthority() {
        return getRole();
    }
}
