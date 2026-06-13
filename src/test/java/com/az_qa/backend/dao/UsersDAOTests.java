/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.az_qa.backend.entity.RoleEntity;
import com.az_qa.backend.entity.UserEntity;
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.exception.ResourceNotFoundException;
import com.az_qa.backend.repository.RolesRepository;
import com.az_qa.backend.repository.UsersRepository;
import com.az_qa.backend.vo.UserVO;
import java.util.List;
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

  @Mock private UsersRepository usersRepository;

  @Mock private RolesRepository rolesRepository;

  @InjectMocks private UsersDAO usersDAO;

  private UserVO userVO;
  private UserEntity userEntity;
  private UserEntity activeUser;

  @BeforeEach
  void setUp() {
    RoleEntity roleEntity = new RoleEntity();
    roleEntity.setId(1L);

    userVO = new UserVO();
    userVO.setId(1L);
    userVO.setName("John");
    userVO.setLastName("Doe");
    userVO.setEmail("john.doe@example.com");
    userVO.setPassword("secret");
    userVO.setIsActive(true);
    userVO.setRoleId(1L);

    userEntity = new UserEntity();
    userEntity.setId(1L);
    userEntity.setName("John");
    userEntity.setLastName("Doe");
    userEntity.setEmail("john.doe@example.com");
    userEntity.setPassword("secret");
    userEntity.setIsActive(true);
    userEntity.setRole(roleEntity);

    activeUser = new UserEntity();
    activeUser.setId(1L);
    activeUser.setName("John");
    activeUser.setLastName("Doe");
    activeUser.setEmail("john.doe@example.com");
    activeUser.setIsActive(true);
  }

  @Test
  @DisplayName("add: Must return null when input VO is null")
  void add_ReturnsNull_WhenInputIsNull() {
    UserVO result = usersDAO.add(null);

    assertNull(result);
    verify(usersRepository, never()).save(any(UserEntity.class));
  }

  @Test
  @DisplayName("add: Must persist and return VO when role is already present")
  void add_Success_WhenRoleIsAlreadyMapped() {
    when(usersRepository.save(any(UserEntity.class))).thenReturn(userEntity);

    UserVO result = usersDAO.add(userVO);

    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("john.doe@example.com", result.getEmail());
    verify(rolesRepository, never()).findById(any(Long.class));
  }

  @Test
  @DisplayName("add: Must resolve role from repository when mapper does not attach one")
  void add_Success_WhenRoleIsResolvedFromRepository() {
    RoleEntity roleEntity = new RoleEntity();
    roleEntity.setId(0L);

    UserVO userWithoutMappedRole = new UserVO();
    userWithoutMappedRole.setId(2L);
    userWithoutMappedRole.setName("Alice");
    userWithoutMappedRole.setLastName("Smith");
    userWithoutMappedRole.setEmail("alice.smith@example.com");
    userWithoutMappedRole.setPassword("secret");
    userWithoutMappedRole.setIsActive(true);
    userWithoutMappedRole.setRoleId(0L);

    UserEntity savedEntity = new UserEntity();
    savedEntity.setId(2L);
    savedEntity.setName("Alice");
    savedEntity.setLastName("Smith");
    savedEntity.setEmail("alice.smith@example.com");
    savedEntity.setPassword("secret");
    savedEntity.setIsActive(true);
    savedEntity.setRole(roleEntity);

    when(rolesRepository.findById(0L)).thenReturn(Optional.of(roleEntity));
    when(usersRepository.save(any(UserEntity.class))).thenReturn(savedEntity);

    UserVO result = usersDAO.add(userWithoutMappedRole);

    assertNotNull(result);
    assertEquals(2L, result.getId());
    assertEquals(0L, result.getRoleId());
    verify(rolesRepository).findById(0L);
  }

  @Test
  @DisplayName("add: Must throw ItemNotFoundException when role does not exist")
  void add_ThrowsItemNotFoundException_WhenRoleDoesNotExist() {
    UserVO userWithoutMappedRole = new UserVO();
    userWithoutMappedRole.setName("Alice");
    userWithoutMappedRole.setLastName("Smith");
    userWithoutMappedRole.setEmail("alice.smith@example.com");
    userWithoutMappedRole.setPassword("secret");
    userWithoutMappedRole.setIsActive(true);
    userWithoutMappedRole.setRoleId(0L);

    when(rolesRepository.findById(0L)).thenReturn(Optional.empty());

    ItemNotFoundException exception =
        assertThrows(ItemNotFoundException.class, () -> usersDAO.add(userWithoutMappedRole));

    assertEquals("Role with id {0} not found.", exception.getMessage());
  }

  @Test
  @DisplayName("deactivate: Must set isActive to false and save when user is found and active")
  public void deactivate_Success() {
    when(usersRepository.findByIdAndIsActive(1L, true)).thenReturn(Optional.of(activeUser));
    when(usersRepository.save(any(UserEntity.class))).thenReturn(activeUser);

    usersDAO.deactivate(1L);

    assertFalse(activeUser.getIsActive());
    verify(usersRepository).save(activeUser);
  }

  @Test
  @DisplayName(
      "deactivate: Must throw ResourceNotFoundException when user is not found or not active")
  public void deactivate_ThrowsResourceNotFoundException() {
    when(usersRepository.findByIdAndIsActive(1L, true)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> usersDAO.deactivate(1L));
  }

  @Test
  @DisplayName("findById(Long): Must return VO when user exists")
  void findById_ReturnsVO_WhenUserExists() {
    Long id = 1L;
    when(usersRepository.findById(id)).thenReturn(Optional.of(userEntity));

    UserVO result = usersDAO.findById(id);

    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("john.doe@example.com", result.getEmail());
  }

  @Test
  @DisplayName("findById(Long): Must throw ItemNotFoundException when user does not exist")
  void findById_ThrowsItemNotFoundException_WhenUserNotFound() {
    Long id = 99L;
    when(usersRepository.findById(id)).thenReturn(Optional.empty());

    ItemNotFoundException exception =
        assertThrows(ItemNotFoundException.class, () -> usersDAO.findById(id));

    assertEquals("User with id {99} not found.", exception.getMessage());
  }

  @Test
  @DisplayName("findAll: Must return list of mapped VOs when users exist")
  void findAll_ReturnsMappedList_WhenUsersExist() {
    RoleEntity roleEntity = new RoleEntity();
    roleEntity.setId(1L);

    UserEntity second = new UserEntity();
    second.setId(2L);
    second.setName("Jane");
    second.setLastName("Doe");
    second.setEmail("jane.doe@example.com");
    second.setIsActive(true);
    second.setRole(roleEntity);

    when(usersRepository.findAll()).thenReturn(List.of(userEntity, second));

    List<UserVO> result = usersDAO.findAll();

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("john.doe@example.com", result.get(0).getEmail());
    assertEquals("jane.doe@example.com", result.get(1).getEmail());
  }

  @Test
  @DisplayName("findAll: Must return empty list when no users exist")
  void findAll_ReturnsEmptyList_WhenNoUsersExist() {
    when(usersRepository.findAll()).thenReturn(List.of());

    List<UserVO> result = usersDAO.findAll();

    assertNotNull(result);
    assertEquals(0, result.size());
  }
}
