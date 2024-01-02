package pl.kathelan.budgetifybe.configurations.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER");
    }

    @Bean
    DefaultSecurityFilterChain springSecurity(HttpSecurity http) throws Exception {
        http
                // Ochrona przed CSRF (Cross-Site Request Forgery)
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))

                // Zabezpieczenia dotyczące sesji
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                // Konfiguracja formularza logowania
                .formLogin(formLogin -> formLogin
                        .loginPage("/login") // URL strony logowania
                        .defaultSuccessUrl("/", true) // URL przekierowania po pomyślnym logowaniu
                        .failureUrl("/login-error") // URL przekierowania w przypadku błędu logowania
                        .permitAll() // Dostęp do strony logowania dla wszystkich
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/login"))

                // Konfiguracja wylogowania
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // URL przekierowania po wylogowaniu
                        .invalidateHttpSession(true) // Unieważnij sesję przy wylogowaniu
                        .clearAuthentication(true) // Wyczyść uwierzytelnianie przy wylogowaniu
                        .permitAll() // Dostęp do wylogowania dla wszystkich
                )

                // Zabezpieczenia endpointów
                .authorizeRequests(auth -> auth
                        .requestMatchers( "/login", "/register").permitAll() // Dostęp do wybranych stron bez uwierzytelniania
                        .anyRequest().authenticated() // Wszystkie inne żądania wymagają uwierzytelnienia
                );

        return http.build();
    }


}
