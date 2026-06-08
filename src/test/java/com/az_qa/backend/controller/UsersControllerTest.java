/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.az_qa.backend.service.UsersService;
import com.az_qa.backend.vo.UpdateUserVO;
import com.az_qa.backend.vo.UserVO;

@ExtendWith(MockitoExtension.class)
public class UsersControllerTest {

  @Mock private UsersService usersService;

  @InjectMocks private UsersController usersController;

  private UserVO userStub;
  private UpdateUserVO updateStub;

  @BeforeEach
  void setUp() {
    userStub = new UserVO();
    userStub.setId(1L);
    userStub.setName("John");
    userStub.setLastName("Doe");
    userStub.setEmail("john.doe@example.com");

    updateStub = new UpdateUserVO();
    updateStub.setName("John Updated");
    updateStub.setLastName("Doe");
    updateStub.setEmail("john.updated@example.com");
  }

  @Test
  @DisplayName("updates: Must return 200 OK when user update is successful")
  public void updates_Success() {
    UserVO updatedUser = new UserVO();
    updatedUser.setId(1L);
    updatedUser.setName("John Updated");
    updatedUser.setLastName("Doe");
    updatedUser.setEmail("john.updated@example.com");

    when(usersService.update(any(Long.class), any(UpdateUserVO.class)))
        .thenReturn(updatedUser);

    ResponseEntity<UserVO> response = usersController.updates(1L, updateStub);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("John Updated", response.getBody().getName());
    assertEquals("john.updated@example.com", response.getBody().getEmail());
  }

  @Test
  @DisplayName("updates: Must return 400 Bad Request when the service returns null")
  public void updates_ReturnsBadRequest() {
    when(usersService.update(any(Long.class), any(UpdateUserVO.class)))
        .thenReturn(null);

    ResponseEntity<UserVO> response = usersController.updates(1L, updateStub);

    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  @DisplayName("addNew: Must return 201 Created when a new user is created successfully")
  public void addNew_Success() {
    when(usersService.add(any(UserVO.class))).thenReturn(userStub);

    ResponseEntity<UserVO> response = usersController.addNew(new UserVO());

    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals("John", response.getBody().getName());
  }

  @Test
  @DisplayName("deactivate: Must return 204 No Content when the user is deactivated")
  public void deactivate_Success() {
    doNothing().when(usersService).deactivate(any(Long.class));

    ResponseEntity<Void> response = usersController.deactivate(1L);

    assertNotNull(response);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }
}