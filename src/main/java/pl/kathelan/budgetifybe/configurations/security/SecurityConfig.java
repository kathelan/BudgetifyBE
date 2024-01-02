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
        // In-memory authentication to set up a user
        auth.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER");
    }

    @Bean
    DefaultSecurityFilterChain springSecurity(HttpSecurity http) throws Exception {
        http
                // CSRF (Cross-Site Request Forgery) protection
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))

                // Session management configuration
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                // Login form configuration
                .formLogin(formLogin -> formLogin
                        .loginPage("/login") // URL of the login page
                        .defaultSuccessUrl("/", true) // URL to redirect after successful login
                        .failureUrl("/login-error") // URL to redirect in case of login error
                        .permitAll() // Allow access to the login page for everyone
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/login")) // Custom page in case of access denied

                // Logout configuration
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // URL to redirect after logout
                        .invalidateHttpSession(true) // Invalidate session on logout
                        .clearAuthentication(true) // Clear authentication on logout
                        .permitAll() // Allow logout for everyone
                )

                // Endpoint security configuration
                .authorizeRequests(auth -> auth
                        .requestMatchers("/login", "/register").permitAll() // Allow access without authentication to certain URLs
                        .anyRequest().authenticated() // Other requests require authentication
                );

        return http.build();
    }
}