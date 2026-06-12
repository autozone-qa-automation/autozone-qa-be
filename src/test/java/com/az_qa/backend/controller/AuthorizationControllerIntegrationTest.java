package com.az_qa.backend.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.az_qa.backend.entity.RoleEntity;
import com.az_qa.backend.entity.UserEntity;
import com.az_qa.backend.enumeration.UserRole;
import com.az_qa.backend.repository.RolesRepository;
import com.az_qa.backend.repository.UsersRepository;
import com.az_qa.backend.vo.CredentialsVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthorizationControllerIntegrationTest {

  @Autowired private WebApplicationContext context;

  @Autowired private UsersRepository usersRepository;

  @Autowired private RolesRepository rolesRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {

    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

    usersRepository.deleteAll();
    rolesRepository.deleteAll();

    RoleEntity role = new RoleEntity();
    role.setPermission(UserRole.ADMIN);

    role = rolesRepository.save(role);

    UserEntity user = new UserEntity();
    user.setName("Juan");
    user.setLastName("Perez");
    user.setEmail("admin@test.com");
    user.setPassword(passwordEncoder.encode("123456"));
    user.setIsActive(true);
    user.setRole(role);
    user.setNew(true);

    usersRepository.save(user);
  }

  @Test
  @DisplayName("POST /api/v1/authentify - Login exitoso")
  public void login_Success() throws Exception {

    CredentialsVO credentials = new CredentialsVO();
    credentials.setMail("admin@test.com");
    credentials.setPassword("123456");

    mockMvc
        .perform(
            post("/api/v1/authentify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.user.email").value("admin@test.com"));

    assertNotNull(usersRepository.findByEmail("admin@test.com").orElse(null));
  }

  @Test
  @DisplayName("POST /api/v1/authentify - Password incorrecto")
  public void login_WrongPassword() throws Exception {

    CredentialsVO credentials = new CredentialsVO();
    credentials.setMail("admin@test.com");
    credentials.setPassword("passwordIncorrecto");

    mockMvc
        .perform(
            post("/api/v1/authentify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
        .andExpect(status().isInternalServerError());
  }

  @Test
  @DisplayName("POST /api/v1/authentify - Usuario inexistente")
  public void login_UserNotFound() throws Exception {

    CredentialsVO credentials = new CredentialsVO();
    credentials.setMail("noexiste@test.com");
    credentials.setPassword("123456");

    mockMvc
        .perform(
            post("/api/v1/authentify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
        .andExpect(status().isInternalServerError());
  }
}
