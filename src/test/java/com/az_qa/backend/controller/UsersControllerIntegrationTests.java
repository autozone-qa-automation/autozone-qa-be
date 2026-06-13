/*
 * Tecnológico de Monterrey — Campus Chihuahua
 * Desarrollo e Implantación de Sistemas de Software
 * TC3005B GPO500 - 2026
 * Autozone QA Automation
 */

package com.az_qa.backend.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.az_qa.backend.entity.RoleEntity;
import com.az_qa.backend.enumeration.UserRole;
import com.az_qa.backend.repository.RolesRepository;
import com.az_qa.backend.repository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UsersControllerIntegrationTests {

  @Autowired private WebApplicationContext context;

  @Autowired private UsersRepository usersRepository;

  @Autowired private RolesRepository rolesRepository;

  private MockMvc mockMvc;

  private ObjectMapper objectMapper = new ObjectMapper();

  private Map<String, Object> userPayload;

  private Long validRoleId;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

    usersRepository.deleteAll();
    rolesRepository.deleteAll();

    RoleEntity role = new RoleEntity();
    role.setPermission(UserRole.DEV);
    role = rolesRepository.save(role);
    validRoleId = role.getId();

    userPayload = new LinkedHashMap<>();
    userPayload.put("name", "John");
    userPayload.put("lastName", "Doe");
    userPayload.put("email", "john.doe@example.com");
    userPayload.put("password", "secret123");
    userPayload.put("isActive", true);
    userPayload.put("roleId", validRoleId);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("POST /api/v1/users - Integración completa (Controller -> Service -> DAO -> DB)")
  public void createUser_IntegrationSuccess() throws Exception {

    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userPayload)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.name").value("John"))
        .andExpect(jsonPath("$.lastName").value("Doe"))
        .andExpect(jsonPath("$.email").value("john.doe@example.com"));

    assertNotNull(usersRepository.findByEmail("john.doe@example.com").orElse(null));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("POST /api/v1/users - Error por email duplicado (409 Conflict)")
  public void createUser_IntegrationConflict_WhenEmailAlreadyExists() throws Exception {

    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userPayload)))
        .andExpect(status().isCreated());

    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userPayload)))
        .andExpect(status().isConflict());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("POST /api/v1/users - Error por campo name vacío (400 Bad Request)")
  public void createUser_IntegrationBadRequest_WhenNameIsBlank() throws Exception {

    userPayload.put("name", "");

    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userPayload)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("POST /api/v1/users - Error por email vacío (400 Bad Request)")
  public void createUser_IntegrationBadRequest_WhenEmailIsBlank() throws Exception {

    userPayload.put("email", "");

    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userPayload)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("POST /api/v1/users - Error por password vacío (400 Bad Request)")
  public void createUser_IntegrationBadRequest_WhenPasswordIsBlank() throws Exception {

    userPayload.put("password", "");

    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userPayload)))
        .andExpect(status().isBadRequest());
  }
}
