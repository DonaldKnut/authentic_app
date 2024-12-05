package org.euaggelion.theauthenticapp.authenticate;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityFilterChainConfig {

    private final JwtFilterConfiguration jwtFilterConfiguration;
    private final CustomUserDetailsService customUserDetailsService; // Inject CustomUserDetailsService

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Set session management to stateless (for JWT-based authentication)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configure authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/auth/signup", "/api/auth/login",
                                "/api/users/all", "/v3/api-docs", "/swagger-resources/**", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html")
                        .permitAll()
                        .requestMatchers(GET, "/api/auth/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers(GET, "/api/user/**").hasRole("CUSTOMER")
                        .requestMatchers(GET, "/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(POST, "/api/**").authenticated()
                        .anyRequest().permitAll()
                )

                // Add custom JWT filter before authentication processing
                .addFilterBefore(jwtFilterConfiguration, BasicAuthenticationFilter.class)

                // Disable CSRF using the older API
                .csrf().disable()

                // Enable CORS configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    // Define AuthenticationProvider using CustomUserDetailsService and PasswordEncoder
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());  // Use BCryptPasswordEncoder
        return authProvider;
    }

    // CORS configuration bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8080"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Define AuthenticationManager bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
