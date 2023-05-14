package ip.labwork.user.service;

import ip.labwork.configuration.jwt.JwtException;
import ip.labwork.configuration.jwt.JwtProvider;
import ip.labwork.user.controller.UserDto;
import ip.labwork.user.controller.UserInfoDto;
import ip.labwork.user.model.User;
import ip.labwork.user.model.UserRole;
import ip.labwork.user.repository.UserRepository;
import ip.labwork.util.validation.ValidationException;
import ip.labwork.util.validation.ValidatorUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidatorUtil validatorUtil;
    private final JwtProvider jwtProvider;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ValidatorUtil validatorUtil,
                       JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validatorUtil = validatorUtil;
        this.jwtProvider = jwtProvider;
    }

    public Page<User> findAllPages(int page, int size) {
        return userRepository.findAll(PageRequest.of(page - 1, size, Sort.by("id").ascending()));
    }

    public User findByLogin(String login) {
        return userRepository.findOneByLoginIgnoreCase(login);
    }

    public User createUser(String login, String password, String passwordConfirm) {
        return createUser(login, password, passwordConfirm, UserRole.USER);
    }

    public User createUser(String login, String password, String passwordConfirm, UserRole role) {
        if (findByLogin(login) != null) {
            throw new UserExistsException(login);
        }
        final User user = new User(login, passwordEncoder.encode(password), role);
        validatorUtil.validate(user);
        if (!Objects.equals(password, passwordConfirm)) {
            throw new ValidationException("Passwords not equals");
        }
        return userRepository.save(user);
    }

    public String loginAndGetToken(UserDto userDto) {
        final User user = findByLogin(userDto.getLogin());
        if (user == null) {
            throw new UserNotFoundException(userDto.getLogin());
        }
        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new UserNotFoundException(user.getLogin());
        }
        return jwtProvider.generateToken(user.getLogin());
    }

    public UserInfoDto signupAndGetToken(UserDto userDto) {
        final User user = createUser(userDto.getLogin(), userDto.getPassword(), userDto.getPasswordConfirm(), UserRole.USER);
        return new UserInfoDto(jwtProvider.generateToken(user.getLogin()), user.getLogin(), UserRole.USER);
    }

    public UserDetails loadUserByToken(String token) throws UsernameNotFoundException {
        if (!jwtProvider.isTokenValid(token)) {
            throw new JwtException("Bad token");
        }
        final String userLogin = jwtProvider.getLoginFromToken(token)
                .orElseThrow(() -> new JwtException("Token is not contain Login"));
        return loadUserByUsername(userLogin);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User userEntity = findByLogin(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(
                userEntity.getLogin(), userEntity.getPassword(), Collections.singleton(userEntity.getRole()));
    }
}