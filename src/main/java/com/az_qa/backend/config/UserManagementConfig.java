package com.az_qa.backend.config;

import com.az_qa.backend.security.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Clase de configuración para la seguridad y la gestión de accesos de la aplicación.
 * Define las reglas de autenticación, autorización basada en roles para los endpoints
 * de la API, manejo de excepciones de seguridad (401 y 403) y la inyección del filtro JWT.
 *
 * @author az_qa
 * @version 1.0
 */
@Configuration
public class UserManagementConfig {

  /**
   * Define el codificador de contraseñas utilizando el algoritmo BCrypt.
   * Utiliza una instancia fuerte de {@link SecureRandom} para garantizar la generación
   * segura de la sal (salt) con un factor de fuerza de 4.
   *
   * @return Una instancia de {@link PasswordEncoder} basada en BCrypt.
   * @throws NoSuchAlgorithmException Si el algoritmo de seguridad solicitado no está disponible
   * en el entorno al intentar obtener la instancia fuerte de SecureRandom.
   */
  @Bean
  public PasswordEncoder passwordEncoder() throws NoSuchAlgorithmException {
    SecureRandom secureRandom = SecureRandom.getInstanceStrong();
    return new BCryptPasswordEncoder(4, secureRandom);
  }

  /**
   * Configura la cadena de filtros de seguridad (SecurityFilterChain) de la aplicación.
   * Esta configuración establece las siguientes políticas:
   * Desactiva la autenticación HTTP Básica y la protección CSRF (diseñado para APIs REST).
   * Habilita la configuración CORS por defecto.
   * Establece una política de sesión sin estado (STATELESS).
   * Define el control de acceso detallado por roles (ADMIN, DEV, READ_ONLY) para los endpoints de la API.
   * Personaliza el manejo de respuestas para errores de autenticación (401) y acceso denegado (403).
   * Registra el filtro personalizado {@link JwtFilter} antes del filtro de autenticación por defecto.
   *
   * @param http El objeto {@link HttpSecurity} que permite configurar la seguridad web.
   * @param jwtFilter El filtro personalizado para interceptar y validar los tokens JWT.
   * @return El {@link SecurityFilterChain} configurado y construido.
   * @throws Exception Si ocurre algún error durante el proceso de configuración o construcción.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter)
      throws Exception {

    http.httpBasic(basic -> basic.disable())
        .cors(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/docs",
                        "/docs/**",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-ui",
                        "/swagger-ui/**",
                        "/swagger-ui.html")
                    .permitAll()

                    // AUTH PÚBLICO
                    .requestMatchers(HttpMethod.POST, "/api/v1/authentify")
                    .permitAll()

                    // GET - ADMIN + DEV + READ_ONLY
                    .requestMatchers(HttpMethod.GET, "/api/v1/services/**")
                    .hasAnyRole("ADMIN", "DEV", "READ_ONLY")
                    .requestMatchers(HttpMethod.GET, "/api/v1/test-cases/**")
                    .hasAnyRole("ADMIN", "DEV", "READ_ONLY")
                    .requestMatchers(HttpMethod.GET, "/api/v1/features/**")
                    .hasAnyRole("ADMIN", "DEV", "READ_ONLY")
                    .requestMatchers(HttpMethod.GET, "/api/v1/releases/**")
                    .hasAnyRole("ADMIN", "DEV", "READ_ONLY")

                    // POST - CREACIÓN ADMIN + DEV
                    .requestMatchers(HttpMethod.POST, "/api/v1/services/**")
                    .hasAnyRole("ADMIN", "DEV")
                    .requestMatchers(HttpMethod.POST, "/api/v1/test-cases/**")
                    .hasAnyRole("ADMIN", "DEV")
                    .requestMatchers(HttpMethod.POST, "/api/v1/features/**")
                    .hasAnyRole("ADMIN", "DEV")
                    .requestMatchers(HttpMethod.POST, "/api/v1/releases/**")
                    .hasAnyRole("ADMIN", "DEV")

                    // PUT - MODIFICACIÓN
                    .requestMatchers(HttpMethod.PUT, "/api/v1/services/**")
                    .hasAnyRole("ADMIN", "DEV")
                    .requestMatchers(HttpMethod.PUT, "/api/v1/test-cases/**")
                    .hasAnyRole("ADMIN", "DEV")
                    .requestMatchers(HttpMethod.PUT, "/api/v1/features/**")
                    .hasAnyRole("ADMIN", "DEV")
                    .requestMatchers(HttpMethod.PUT, "/api/v1/releases/**")
                    .hasAnyRole("ADMIN", "DEV")

                    // DELETE - SOLO ADMIN
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/services/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/test-cases/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/features/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/releases/**")
                    .hasRole("ADMIN")

                    // USERS - SOLO ADMIN
                    .requestMatchers("/api/v1/users/**")
                    .hasRole("ADMIN")

                    // ROLES - SOLO ADMIN
                    .requestMatchers("/api/v1/roles/**")
                    .hasRole("ADMIN")

                    // REPORTES - SOLO ADMIN
                    .requestMatchers("/api/reports/**")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            exception ->
                exception
                    .authenticationEntryPoint(
                        (request, response, authException) -> {
                          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                          response.setContentType("application/json");
                          response.setCharacterEncoding("UTF-8");

                          response
                              .getWriter()
                              .write(
                                  """
                  {
                    "status": 401,
                    "error": "Unauthorized",
                    "message": "Token inválido o ausente."
                  }
                  """);
                        })
                    .accessDeniedHandler(
                        (request, response, accessDeniedException) -> {
                          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                          response.setContentType("application/json");
                          response.setCharacterEncoding("UTF-8");

                          response
                              .getWriter()
                              .write(
                                  """
                  {
                    "status": 403,
                    "error": "Forbidden",
                    "message": "No tienes permisos suficientes para acceder a este recurso."
                  }
                  """);
                        }))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  /**
   * Expone el gestor de autenticación por defecto de Spring Security como un Bean.
   * Este componente es esencial para procesar las solicitudes de autenticación en los
   * controladores o servicios correspondientes.
   *
   * @param config La configuración de autenticación global proporcionada por Spring.
   * @return El {@link AuthenticationManager} configurado.
   * @throws Exception Si ocurre un error al recuperar el manejador de autenticación.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {

    return config.getAuthenticationManager();
  }
}
