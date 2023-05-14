package ip.labwork.configuration;

import ip.labwork.configuration.jwt.JwtFilter;
import ip.labwork.user.controller.UserController;
import ip.labwork.user.model.UserRole;
import ip.labwork.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {
    private final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    public static final String SPA_URL_MASK = "/{path:[^\\.]*}";

    private final UserService userService;
    private final JwtFilter jwtFilter;

    public SecurityConfiguration(UserService userService) {
        this.userService = userService;
        this.jwtFilter = new JwtFilter(userService);
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
        log.info("Creating security configuration");
        http.cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/", SPA_URL_MASK).permitAll()
                .requestMatchers(HttpMethod.POST, UserController.URL_SIGNUP).permitAll()
                .requestMatchers(HttpMethod.POST, UserController.URL_LOGIN).permitAll()
                .requestMatchers(HttpMethod.GET, "/users/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/h2-console").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .anonymous();
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoderConfiguration bCryptPasswordEncoder)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder.passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(HttpMethod.OPTIONS, "/**")
                .requestMatchers("/*.js")
                .requestMatchers("/*.png")
                .requestMatchers("/*.jpg")
                .requestMatchers("/*.html")
                .requestMatchers("/*.css")
                .requestMatchers("/assets/**")
                .requestMatchers("/favicon.ico")
                .requestMatchers("/.js", "/.css")
                .requestMatchers("/swagger-ui/index.html")
                .requestMatchers("/webjars/**")
                .requestMatchers("/swagger-resources/**")
                .requestMatchers("/v3/api-docs/**");
    }
}
