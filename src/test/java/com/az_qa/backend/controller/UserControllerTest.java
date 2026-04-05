package com.az_qa.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.az_qa.backend.dto.request.UserRequest;
import com.az_qa.backend.dto.request.UserUpdateRequest;
import com.az_qa.backend.dto.response.UserResponse;
import com.az_qa.backend.exception.GlobalExceptionHandler;
import com.az_qa.backend.exception.ResourceNotFoundException;
import com.az_qa.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Unit tests for {@link UserController}.
 *
 * <p>Uses {@code @WebMvcTest} to load only the web layer. {@link UserService} is mocked with
 * {@code @MockitoBean} — no database involved.
 *
 * <p>Each nested class maps to one endpoint. Tests cover the same scenarios validated manually
 * via curl: happy paths, not found, invalid input, type mismatches, and negative IDs.
 */
@WebMvcTest(controllers = {UserController.class, GlobalExceptionHandler.class})
class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @MockitoBean private UserService userService;

  // ---------------------------------------------------------------------------
  // GET /api/v1/users
  // ---------------------------------------------------------------------------

  @Nested
  @DisplayName("GET /api/v1/users")
  class GetAll {

    @Test
    @DisplayName("returns 200 with a list of users")
    void returnsListOfUsers() throws Exception {
      UserResponse user = new UserResponse(1L, "Juan", "juan@mail.com");
      when(userService.getAllUsers()).thenReturn(List.of(user));

      mockMvc
          .perform(get("/api/v1/users"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].id").value(1))
          .andExpect(jsonPath("$[0].name").value("Juan"))
          .andExpect(jsonPath("$[0].email").value("juan@mail.com"));
    }

    @Test
    @DisplayName("returns 200 with empty list when no users exist")
    void returnsEmptyList() throws Exception {
      when(userService.getAllUsers()).thenReturn(List.of());

      mockMvc
          .perform(get("/api/v1/users"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$").isEmpty());
    }
  }

  // ---------------------------------------------------------------------------
  // GET /api/v1/users/{id}
  // ---------------------------------------------------------------------------

  @Nested
  @DisplayName("GET /api/v1/users/{id}")
  class GetById {

    @Test
    @DisplayName("returns 200 when user exists")
    void returnsUserWhenFound() throws Exception {
      UserResponse user = new UserResponse(1L, "Juan", "juan@mail.com");
      when(userService.getUserById(1L)).thenReturn(user);

      mockMvc
          .perform(get("/api/v1/users/1"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.name").value("Juan"))
          .andExpect(jsonPath("$.email").value("juan@mail.com"));
    }

    @Test
    @DisplayName("returns 404 when user does not exist")
    void returns404WhenNotFound() throws Exception {
      when(userService.getUserById(999L))
          .thenThrow(new ResourceNotFoundException("User not found with ID: 999"));

      mockMvc
          .perform(get("/api/v1/users/999"))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.status").value(404))
          .andExpect(jsonPath("$.message").value("User not found with ID: 999"));
    }

    @Test
    @DisplayName("returns 400 when id is negative")
    void returns400WhenIdIsNegative() throws Exception {
      mockMvc
          .perform(get("/api/v1/users/-1"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("returns 400 when id is zero")
    void returns400WhenIdIsZero() throws Exception {
      mockMvc
          .perform(get("/api/v1/users/0"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("returns 400 when id is not a number")
    void returns400WhenIdIsNotNumeric() throws Exception {
      mockMvc
          .perform(get("/api/v1/users/abc"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400));
    }
  }

  // ---------------------------------------------------------------------------
  // POST /api/v1/users
  // ---------------------------------------------------------------------------

  @Nested
  @DisplayName("POST /api/v1/users")
  class CreateUser {

    @Test
    @DisplayName("returns 201 when request is valid")
    void returns201WhenValid() throws Exception {
      UserRequest request = new UserRequest("Juan", "juan@mail.com");
      UserResponse response = new UserResponse(1L, "Juan", "juan@mail.com");
      when(userService.create(any(UserRequest.class))).thenReturn(response);

      mockMvc
          .perform(
              post("/api/v1/users")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.name").value("Juan"))
          .andExpect(jsonPath("$.email").value("juan@mail.com"));
    }

    @Test
    @DisplayName("returns 400 when name is missing")
    void returns400WhenNameIsMissing() throws Exception {
      UserRequest request = new UserRequest(null, "juan@mail.com");

      mockMvc
          .perform(
              post("/api/v1/users")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("returns 400 when name is too short")
    void returns400WhenNameIsTooShort() throws Exception {
      UserRequest request = new UserRequest("Jo", "juan@mail.com");

      mockMvc
          .perform(
              post("/api/v1/users")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("returns 400 when body is empty")
    void returns400WhenBodyIsEmpty() throws Exception {
      mockMvc
          .perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content("{}"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("returns 400 when body contains unknown fields")
    void returns400WhenBodyHasUnknownFields() throws Exception {
      String body = "{\"name\":\"Juan\",\"email\":\"juan@mail.com\",\"hack\":\"xss\"}";

      mockMvc
          .perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(body))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400));
    }
  }

  // ---------------------------------------------------------------------------
  // PUT /api/v1/users/{id}
  // ---------------------------------------------------------------------------

  @Nested
  @DisplayName("PUT /api/v1/users/{id}")
  class UpdateUser {

    @Test
    @DisplayName("returns 200 when updating name only")
    void returns200WhenUpdatingNameOnly() throws Exception {
      UserUpdateRequest request = new UserUpdateRequest();
      request.setName("Juan Actualizado");

      UserResponse response = new UserResponse(1L, "Juan Actualizado", "juan@mail.com");
      when(userService.update(eq(1L), any(UserUpdateRequest.class))).thenReturn(response);

      mockMvc
          .perform(
              put("/api/v1/users/1")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.name").value("Juan Actualizado"))
          .andExpect(jsonPath("$.email").value("juan@mail.com"));
    }

    @Test
    @DisplayName("returns 200 when updating email only")
    void returns200WhenUpdatingEmailOnly() throws Exception {
      UserUpdateRequest request = new UserUpdateRequest();
      request.setEmail("nuevo@mail.com");

      UserResponse response = new UserResponse(1L, "Juan", "nuevo@mail.com");
      when(userService.update(eq(1L), any(UserUpdateRequest.class))).thenReturn(response);

      mockMvc
          .perform(
              put("/api/v1/users/1")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.email").value("nuevo@mail.com"));
    }

    @Test
    @DisplayName("returns 404 when user does not exist")
    void returns404WhenNotFound() throws Exception {
      UserUpdateRequest request = new UserUpdateRequest();
      request.setName("Juan");

      when(userService.update(eq(999L), any(UserUpdateRequest.class)))
          .thenThrow(new ResourceNotFoundException("User not found with ID: 999"));

      mockMvc
          .perform(
              put("/api/v1/users/999")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.status").value(404))
          .andExpect(jsonPath("$.message").value("User not found with ID: 999"));
    }

    @Test
    @DisplayName("returns 400 when name is too short")
    void returns400WhenNameIsTooShort() throws Exception {
      UserUpdateRequest request = new UserUpdateRequest();
      request.setName("AB");

      mockMvc
          .perform(
              put("/api/v1/users/1")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("returns 400 when id is negative")
    void returns400WhenIdIsNegative() throws Exception {
      UserUpdateRequest request = new UserUpdateRequest();
      request.setName("Juan");

      mockMvc
          .perform(
              put("/api/v1/users/-1")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("returns 400 when id is not a number")
    void returns400WhenIdIsNotNumeric() throws Exception {
      UserUpdateRequest request = new UserUpdateRequest();
      request.setName("Juan");

      mockMvc
          .perform(
              put("/api/v1/users/abc")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400));
    }
  }
}
