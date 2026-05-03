package com.cne_project.cne_project.config.auth;

import com.cne_project.cne_project.config.exception.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.OffsetDateTime;
import java.util.List;


@Configuration
public class AuthSecurityConfig {

    final private UserDetailsService userDetailsService;
    final private ObjectMapper objectMapper;
    final private JwtFilter jwtFilter;

    @Value("${FRONTEND_URL:http://localhost:3000}")
    private String frontendUrl;

    public AuthSecurityConfig(UserDetailsService userDetailsService,
                              ObjectMapper objectMapper,
                              JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .sessionFixation().none()
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(request -> {
                            String uri = request.getRequestURI();
                            if (uri == null) return false;

                            return uri.endsWith("api/auth/register") ||
                                    uri.endsWith("api/auth/login") ||
                                    uri.contains("swagger-ui") ||
                                    uri.contains("api-docs") ||
                                    uri.contains("webjars") ||
                                    uri.endsWith("error");
                        }).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedEntryPoint())
                )
                .build();
    }

    /**
     * Configures the authentication provider of Spring Security
     * Uses UserDetailsService to fetch data
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder()); // Cost factor 12
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     *  Creates an AuthenticationManager bean
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> {
            ApiErrorResponse error = ApiErrorResponse.builder()
                    .timestamp(OffsetDateTime.now())
                    .status(HttpServletResponse.SC_UNAUTHORIZED)
                    .error("Missing authentication header")
                    .message(authException.getMessage())
                    .path(request.getRequestURI())
                    .build();

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(error));
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Set to your React development server origin
        configuration.setAllowedOrigins(List.of(
            "http://localhost:3000", frontendUrl
        ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}