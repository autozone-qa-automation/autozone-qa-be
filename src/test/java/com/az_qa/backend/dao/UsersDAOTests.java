/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.az_qa.backend.entity.UserEntity;
import com.az_qa.backend.exception.ResourceNotFoundException;
import com.az_qa.backend.repository.UsersRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UsersDAOTests {

  @Mock private UsersRepository userRepository;

  @InjectMocks private UsersDAO usersDAO;

  private UserEntity activeUser;

  @BeforeEach
  void setUp() {
    activeUser = new UserEntity();
    activeUser.setId(1L);
    activeUser.setName("John");
    activeUser.setLastName("Doe");
    activeUser.setEmail("john.doe@example.com");
    activeUser.setIsActive(true);
  }

  @Test
  @DisplayName("deactivate: Must set isActive to false and save when user is found and active")
  public void deactivate_Success() {
    when(userRepository.findByIdAndIsActive(1L, true)).thenReturn(Optional.of(activeUser));
    when(userRepository.save(any(UserEntity.class))).thenReturn(activeUser);

    usersDAO.deactivate(1L);

    assertFalse(activeUser.getIsActive());
    verify(userRepository).save(activeUser);
  }

  @Test
  @DisplayName(
      "deactivate: Must throw ResourceNotFoundException when user is not found or not active")
  public void deactivate_ThrowsResourceNotFoundException() {
    when(userRepository.findByIdAndIsActive(1L, true)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> usersDAO.deactivate(1L));
  }
}
