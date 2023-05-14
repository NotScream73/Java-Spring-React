package ip.labwork.user.controller;

import ip.labwork.user.model.UserRole;
import jakarta.validation.constraints.NotEmpty;

public class UserInfoDto {
    @NotEmpty
    private String token;
    @NotEmpty
    private String login;
    @NotEmpty
    private UserRole role;

    public UserInfoDto(String token, String login, UserRole role) {
        this.token = token;
        this.login = login;
        this.role = role;
    }

    public UserInfoDto() {
    }

    public String getToken() {
        return token;
    }

    public String getLogin() {
        return login;
    }

    public UserRole getRole() {
        return role;
    }
}
