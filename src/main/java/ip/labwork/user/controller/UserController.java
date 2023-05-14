package ip.labwork.user.controller;

import ip.labwork.user.model.User;
import ip.labwork.user.model.UserRole;
import ip.labwork.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

@RestController
public class UserController {
    public static final String URL_LOGIN = "/jwt/login";
    public static final String URL_SIGNUP = "/jwt/signup";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(URL_LOGIN)
    public String login(@RequestBody @Valid UserDto userDto) {
        return userService.loginAndGetToken(userDto);
    }

    @PostMapping(URL_SIGNUP)
    public UserInfoDto signup(@RequestBody @Valid UserDto userDto) {
        return userService.signupAndGetToken(userDto);
    }

    @GetMapping("/users/{login}")
    public UserDetails getCurrentUser(@PathVariable String login) {
        try {
            return userService.loadUserByUsername(login);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/user")
    public String findUser(@RequestParam("token") String token) {
        UserDetails userDetails = userService.loadUserByToken(token);
        User user = userService.findByLogin(userDetails.getUsername());
        return user.getRole().toString();
    }

    @GetMapping("/users")
    @Secured({UserRole.AsString.ADMIN})
    public UsersPageDTO getUsers(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "5") int size) {
        final Page<UserDto> users = userService.findAllPages(page, size)
                .map(UserDto::new);
        final int totalPages = users.getTotalPages();
        final List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .toList();
        return new UsersPageDTO(users, pageNumbers, totalPages);
    }
}
