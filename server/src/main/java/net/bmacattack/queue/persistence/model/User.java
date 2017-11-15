package net.bmacattack.queue.persistence.model;

import net.bmacattack.queue.persistence.RoleEnum;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private RoleEnum role;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="accessTokens", joinColumns=@JoinColumn(name="user_id"))
    private List<UserAccessToken> accessTokens;

    public User() {
    }

    public User(String username, String email, String password, RoleEnum role) {

        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.accessTokens = new ArrayList<>();
    }

    public List<UserAccessToken> getAccessTokens() {
        return accessTokens;
    }

    public void deleteAccessToken(UserAccessToken accessToken) {
        accessTokens.remove(accessToken);
    }

    public void addAccessToken(UserAccessToken token) {
        accessTokens.add(token);
    }

    public void setAccessTokens(List<UserAccessToken> accessTokens) {
        this.accessTokens = accessTokens;
    }

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

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
