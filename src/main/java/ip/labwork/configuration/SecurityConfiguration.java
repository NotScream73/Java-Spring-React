package ip.labwork.configuration;

import ip.labwork.user.controller.UserSignupMvcController;
import ip.labwork.user.model.UserRole;
import ip.labwork.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
    private final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);
    private static final String LOGIN_URL = "/login";
    private final UserService userService;
    public SecurityConfiguration(UserService userService) {
        this.userService = userService;
        createAdminOnStartup();
    }

    private void createAdminOnStartup() {
        final String admin = "admin";
        if (userService.findByLogin(admin) == null) {
            log.info("Admin user successfully created");
            userService.createUser(admin, admin, admin, UserRole.ADMIN);
        }
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin().and()
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers(UserSignupMvcController.SIGNUP_URL).permitAll()
                .requestMatchers(HttpMethod.GET, LOGIN_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage(LOGIN_URL).permitAll()
                .and()
                .logout().permitAll();
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoderConfiguration bCryptPasswordEncoder)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder.createPasswordEncoder())
                .and()
                .build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/css/**")
                .requestMatchers("/js/**")
                .requestMatchers("/templates/**")
                .requestMatchers("/webjars/**");
    }
}