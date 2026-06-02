package com.az_qa.backend.config;

import com.az_qa.backend.security.JwtFilter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class UserManagementConfig {
  @Bean
  public PasswordEncoder passwordEncoder() throws NoSuchAlgorithmException {
    SecureRandom s = SecureRandom.getInstanceStrong();
    return new BCryptPasswordEncoder(4, s);
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring()
        .requestMatchers(
            "/docs",
            "/docs/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui",
            "/swagger-ui/**",
            "/swagger-ui.html");
  }

  @Bean // frijol magico madre mia
  public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter)
      throws Exception {

    http.httpBasic(basic -> basic.disable());

    http.cors(Customizer.withDefaults());

    http.csrf(csrf -> csrf.disable());

    http.authorizeHttpRequests(
        c -> c.requestMatchers("/api/v1/authentify")
            .permitAll()
            // MODIFICAR CAMINOS Y PERMISOS SEGUN NECESIDAD

            .requestMatchers("/api/v1/test-cases/1")
            .hasAuthority("ADMIN")
            .requestMatchers("/api/v1/test-cases")
            .hasAuthority("ADMIN")
            .requestMatchers("/api/v1/test-cases")
            .hasAuthority("READ_ONLY")
            .requestMatchers("/api/v1/services/**")
            .hasAuthority("ADMIN")
            .anyRequest()
            .authenticated())
        // no queremos crear una sesion que se conserver entre una llamada a la api y
        // otra, ergo:
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // agregamos el filtro JwtFilter a la cadena de filtros de nuestras peticiones a
        // la api!!!
        // Asi debes logearte afuerzas
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}
