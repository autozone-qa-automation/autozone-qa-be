/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.az_qa.backend.dao.UsersDAO;
import com.az_qa.backend.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTests {

  @Mock private UsersDAO userDAO;

  @InjectMocks private UsersService usersService;

  @Test
  @DisplayName("deactivate: Must deactivate user successfully when DAO succeeds")
  public void deactivate_Success() {
    doNothing().when(userDAO).deactivate(1L);

    usersService.deactivate(1L);

    verify(userDAO).deactivate(1L);
  }

  @Test
  @DisplayName("deactivate: Must propagate ResourceNotFoundException when user does not exist")
  public void deactivate_ThrowsResourceNotFoundException() {
    doThrow(new ResourceNotFoundException("User not found with id: 1"))
        .when(userDAO)
        .deactivate(1L);

    assertThrows(ResourceNotFoundException.class, () -> usersService.deactivate(1L));
  }
}
