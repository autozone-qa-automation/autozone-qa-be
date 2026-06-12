package com.az_qa.backend.security;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

public class JwtServiceTest {

  private JwtService jwtService;

  @BeforeEach
  void setUp() {

    jwtService = new JwtService();

    ReflectionTestUtils.setField(
        jwtService,
        "secretoJwt",
        "0c3182f619255159f8743a4ab949e4c402822b9cf434bdaff72f5bd171eb4a47");
  }

  @Test
  @DisplayName("generarToken: Debe generar un token válido")
  public void generarToken_ReturnsToken() {

    String token = jwtService.generarToken("test@test.com");

    assertNotNull(token);

    assertFalse(token.isEmpty());
  }

  @Test
  @DisplayName("extraerMailUsuario: Debe extraer correctamente el correo del token")
  public void extraerMailUsuario_ReturnsEmail() {

    String token = jwtService.generarToken("test@test.com");

    String email = jwtService.extraerMailUsuario(token);

    assertEquals("test@test.com", email);
  }

  @Test
  @DisplayName("validarToken: Debe retornar true cuando el usuario coincide")
  public void validarToken_ReturnsTrue() {

    String token = jwtService.generarToken("test@test.com");

    UserDetails user = new User("test@test.com", "password", new ArrayList<>());

    assertTrue(jwtService.validarToken(token, user));
  }

  @Test
  @DisplayName("validarToken: Debe retornar false cuando el usuario no coincide")
  public void validarToken_ReturnsFalse() {

    String token = jwtService.generarToken("test@test.com");

    UserDetails user = new User("otro@test.com", "password", new ArrayList<>());

    assertFalse(jwtService.validarToken(token, user));
  }

  @Test
  @DisplayName("haExpiradoToken: Un token recién generado no debe estar expirado")
  public void haExpiradoToken_ReturnsFalse() {

    String token = jwtService.generarToken("test@test.com");

    assertFalse(jwtService.haExpiradoToken(token));
  }
}
