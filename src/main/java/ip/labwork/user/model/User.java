package ip.labwork.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true, length = 64)
    @NotBlank
    @Size(min = 3, max = 64)
    private String login;
    @Column(nullable = false, length = 64)
    @NotBlank
    @Size(min = 6, max = 64)
    private String password;
    private UserRole role;

    public User() {
    }

    public User(String login, String password) {
        this(login, password, UserRole.USER);
    }

    public User(String login, String password, UserRole role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }
}