package net.bmacattack.queue.persistence.model;

import net.bmacattack.queue.persistence.PrivilegeEnum;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String email;

    @Column(length = 60)
    private String password;

    @Enumerated(EnumType.STRING)
    private PrivilegeEnum role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PrivilegeEnum getRole() {
        return role;
    }

    public void setRole(PrivilegeEnum role) {
        this.role = role;
    }
}
