package org.bytesync.hotelmanagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final SecurityExceptionHandler securityExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(c -> new CorsConfiguration().setAllowedOrigins(List.of("http://localhost:5173", "*")))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
//                                .requestMatchers("/users/api/signin", "/users/api/register", "/users/api/refresh").permitAll()
//
//                                .requestMatchers(HttpMethod.POST, "/reservations/api").hasAnyRole("ADMIN", "RECEPTION")
//                                .requestMatchers(HttpMethod.PUT, "/reservations/api/checkout/**").hasAnyRole("ADMIN", "RECEPTION")
//                                .requestMatchers(HttpMethod.PUT, "/reservations/api/cancel/**").hasAnyRole("ADMIN", "RECEPTION")
//                                .requestMatchers(HttpMethod.PUT, "/reservations/api/*/change-room/**").hasAnyRole("ADMIN", "RECEPTION")
//                                .requestMatchers(HttpMethod.PUT, "/reservations/api/update/**").hasAnyRole("ADMIN", "RECEPTION")
//                                .requestMatchers(HttpMethod.PUT, "/reservations/api/extend-hours/**").hasAnyRole("ADMIN", "RECEPTION")
//                                .requestMatchers(HttpMethod.PUT, "/reservations/api/extend-days/**").hasAnyRole("ADMIN", "RECEPTION")
//                                .requestMatchers(HttpMethod.DELETE, "/reservations/api/delete/**").hasAnyRole("ADMIN")
//
//                                .requestMatchers(HttpMethod.POST, "/guests/api/register").hasAnyRole("ADMIN", "RECEPTION")
//                                .requestMatchers(HttpMethod.PUT, "/guests/api/update/**").hasAnyRole("ADMIN", "RECEPTION")
//                                .requestMatchers(HttpMethod.PUT, "/guests/api/change-status/**").hasAnyRole("ADMIN", "RECEPTION")
//                                .requestMatchers(HttpMethod.DELETE, "/guests/api/delete/*").hasAnyRole("ADMIN")
//                                .requestMatchers(HttpMethod.DELETE, "/guests/api/delete/relation/**").hasAnyRole("ADMIN", "RECEPTION")
//
//                                .requestMatchers(HttpMethod.PATCH, "/rooms/api/change-price/**").hasAnyRole("ADMIN")
//
//                                .requestMatchers(HttpMethod.POST, "/users/api/register").hasAnyRole("ADMIN")
//                                .requestMatchers(HttpMethod.PUT, "/users/api/update/**").hasAnyRole("ADMIN")
//                                .requestMatchers(HttpMethod.PATCH, "/users/api/enable/**").hasAnyRole("ADMIN")
//                                .requestMatchers(HttpMethod.PATCH, "/users/api/disable/**").hasAnyRole("ADMIN")
//                                .requestMatchers(HttpMethod.DELETE, "/users/api/delete/**").hasAnyRole("ADMIN")
//
//                                .requestMatchers(HttpMethod.POST, "/finance/api/vouchers").hasAnyRole("ADMIN", "RECEPTION", "MANAGER")
//                                .requestMatchers(HttpMethod.POST, "/finance/api/vouchers/selected").hasAnyRole("ADMIN", "RECEPTION", "MANAGER")
                                .anyRequest().permitAll()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        http.exceptionHandling(exception -> {
            exception.authenticationEntryPoint(securityExceptionHandler);
            exception.accessDeniedHandler(securityExceptionHandler);
                }
        );

        return http.build();
    }

}
