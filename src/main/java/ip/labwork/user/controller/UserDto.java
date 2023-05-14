package ip.labwork.user.controller;

import ip.labwork.user.model.User;
import ip.labwork.user.model.UserRole;
import jakarta.validation.constraints.NotEmpty;

public class UserDto {
    private long id;
    @NotEmpty
    private String login;
    @NotEmpty
    private String password;
    private String passwordConfirm;
    private UserRole role;

    public UserDto() {
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.role = user.getRole();
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public UserRole getRole() {
        return role;
    }
}
