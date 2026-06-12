/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.az_qa.backend.entity.RoleEntity;
import com.az_qa.backend.entity.UserEntity;
import com.az_qa.backend.enumeration.UserRole;
import com.az_qa.backend.repository.UsersRepository;
import com.az_qa.backend.security.JPADetailsUserService;
import com.az_qa.backend.security.JwtService;
import com.az_qa.backend.vo.AuthResponseVO;
import com.az_qa.backend.vo.CredentialsVO;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
public class AuthorizationControllerTest {

  @Mock private AuthenticationManager authenticationManager;

  @Mock private JPADetailsUserService detalleUsuariosService;

  @Mock private UsersRepository usersRepository;

  @Mock private JwtService jwtService;

  @InjectMocks private AuthorizationController authorizationController;

  private CredentialsVO credentials;

  @BeforeEach
  void setUp() {

    credentials = new CredentialsVO();
    credentials.setMail("test@test.com");
    credentials.setPassword("123456");
  }

  @Test
  @DisplayName("autenticar: Debe retornar token y usuario cuando las credenciales son válidas")
  void autenticar_Success() {

    UserDetails userDetails = new User("test@test.com", "encodedPassword", new ArrayList<>());

    when(detalleUsuariosService.loadUserByUsername("test@test.com")).thenReturn(userDetails);

    when(jwtService.generarToken("test@test.com")).thenReturn("jwt-token");

    RoleEntity role = new RoleEntity();
    role.setId(1L);
    role.setPermission(UserRole.ADMIN);

    UserEntity entity = new UserEntity();
    entity.setId(1L);
    entity.setName("Juan");
    entity.setLastName("Perez");
    entity.setEmail("test@test.com");
    entity.setPassword("hash");
    entity.setIsActive(true);
    entity.setRole(role);

    when(usersRepository.findByEmail("test@test.com")).thenReturn(Optional.of(entity));

    AuthResponseVO response = authorizationController.autenticar(credentials);

    assertNotNull(response);

    assertEquals("jwt-token", response.getToken());

    assertNotNull(response.getUser());

    assertEquals("test@test.com", response.getUser().getEmail());

    assertNull(response.getUser().getPassword());
  }

  @Test
  @DisplayName("autenticar: Debe lanzar excepción cuando las credenciales son inválidas")
  void autenticar_InvalidCredentials() {

    doThrow(new BadCredentialsException("Credenciales incorrectas"))
        .when(authenticationManager)
        .authenticate(any());

    assertThrows(
        BadCredentialsException.class, () -> authorizationController.autenticar(credentials));
  }

  @Test
  @DisplayName("autenticar: Debe retornar token aunque no encuentre usuario")
  void autenticar_UserNotFound() {

    UserDetails userDetails = new User("test@test.com", "encodedPassword", new ArrayList<>());

    when(detalleUsuariosService.loadUserByUsername("test@test.com")).thenReturn(userDetails);

    when(jwtService.generarToken("test@test.com")).thenReturn("jwt-token");

    when(usersRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

    AuthResponseVO response = authorizationController.autenticar(credentials);

    assertNotNull(response);

    assertEquals("jwt-token", response.getToken());

    assertNull(response.getUser());
  }
}
