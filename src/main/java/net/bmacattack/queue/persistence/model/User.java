package net.bmacattack.queue.persistence.model;

import net.bmacattack.queue.persistence.PrivilegeEnum;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="accessTokens", joinColumns=@JoinColumn(name="user_id"))
    private List<UserAccessToken> accessTokens;

    public List<UserAccessToken> getAccessTokens() {
        return accessTokens;
    }

    public void deleteAccessToken(String accessToken) {
        //todo
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

    public PrivilegeEnum getRole() {
        return role;
    }

    public void setRole(PrivilegeEnum role) {
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
