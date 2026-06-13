package com.az_qa.backend.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.az_qa.backend.entity.RoleEntity;
import com.az_qa.backend.entity.UserEntity;
import com.az_qa.backend.enumeration.UserRole;
import com.az_qa.backend.repository.UsersRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class JPADetailsUserServiceTest {

  @Mock private UsersRepository userRepository;

  @InjectMocks private JPADetailsUserService service;

  @Test
  @DisplayName("loadUserByUsername: Debe retornar UserDetails cuando el usuario existe")
  public void loadUserByUsername_UserFound() {

    RoleEntity role = new RoleEntity();
    role.setId(1L);
    role.setPermission(UserRole.ADMIN);

    UserEntity user = new UserEntity();
    user.setEmail("test@test.com");
    user.setPassword("hash");
    user.setRole(role);

    when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

    UserDetails result = service.loadUserByUsername("test@test.com");

    assertEquals("test@test.com", result.getUsername());

    assertEquals("hash", result.getPassword());
  }

  @Test
  @DisplayName("loadUserByUsername: Debe lanzar excepción cuando el usuario no existe")
  public void loadUserByUsername_UserNotFound() {

    when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

    assertThrows(
        UsernameNotFoundException.class, () -> service.loadUserByUsername("test@test.com"));
  }
}
