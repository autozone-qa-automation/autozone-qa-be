<<<<<<< HEAD
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

import com.az_qa.backend.repository.ServicesRepository;
import com.az_qa.backend.vo.ServicesVO;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ServicesControllerIntegrationTests {

  @Autowired private WebApplicationContext context;

  @Autowired private ServicesRepository servicesRepository;

  private MockMvc mockMvc;

  private ObjectMapper objectMapper = new ObjectMapper();

  private ServicesVO serviceVO;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

    servicesRepository.deleteAll();

    serviceVO = new ServicesVO();
    serviceVO.setName("Servicio de Integracion");
    serviceVO.setDescription("Descripción válida para el test de integración");
  }

  @Test
  @WithMockUser
  @DisplayName("POST /api/v1/services - Integración completa (Controller -> Service -> DAO -> DB)")
  public void createService_IntegrationSuccess() throws Exception {

    mockMvc
        .perform(
            post("/api/v1/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(serviceVO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.name").value("Servicio de Integracion"))
        .andExpect(
            jsonPath("$.description").value("Descripción válida para el test de integración"));

    assertNotNull(servicesRepository.findAll().get(0));
  }

  @Test
  @WithMockUser
  @DisplayName("POST /api/v1/services - Error por duplicado (Integración con Exception Handler)")
  public void createService_IntegrationConflict() throws Exception {

    mockMvc
        .perform(
            post("/api/v1/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(serviceVO)))
        .andExpect(status().isCreated());

    mockMvc
        .perform(
            post("/api/v1/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(serviceVO)))
        .andExpect(status().isConflict());
  }
}
=======
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.az_qa.backend.entity.ServicesEntity;
import com.az_qa.backend.repository.ServicesRepository;
import com.az_qa.backend.vo.ServicesVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
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
public class ServicesControllerIntegrationTests {

  @Autowired private WebApplicationContext context;

  @Autowired private ServicesRepository servicesRepository;

  private MockMvc mockMvc;

  private ObjectMapper objectMapper = new ObjectMapper();

  private ServicesVO serviceVO;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

    servicesRepository.deleteAll();

    serviceVO = new ServicesVO();
    serviceVO.setName("Servicio de Integracion");
    serviceVO.setDescription("Descripción válida para el test de integración");
  }

  @Test
  @WithMockUser(authorities = "ADMIN")
  @DisplayName("POST /api/v1/services - Integración completa (Controller -> Service -> DAO -> DB)")
  public void createService_IntegrationSuccess() throws Exception {

    mockMvc
        .perform(
            post("/api/v1/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(serviceVO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.name").value("Servicio de Integracion"))
        .andExpect(
            jsonPath("$.description").value("Descripción válida para el test de integración"));

    assertNotNull(servicesRepository.findAll().get(0));
  }

  @Test
  @WithMockUser(authorities = "ADMIN")
  @DisplayName("POST /api/v1/services - Error por duplicado (Integración con Exception Handler)")
  public void createService_IntegrationConflict() throws Exception {

    mockMvc
        .perform(
            post("/api/v1/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(serviceVO)))
        .andExpect(status().isCreated());

    mockMvc
        .perform(
            post("/api/v1/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(serviceVO)))
        .andExpect(status().isConflict());
  }

  @Test
  @WithMockUser(authorities = "ADMIN")
  @DisplayName("PUT /api/v1/services/{id} - Integración completa para actualizar servicio")
  public void updateService_IntegrationSuccess() throws Exception {
    ServicesEntity testService = new ServicesEntity();
    testService.setName("Servicio a Editar");
    testService.setDescription("Descripción original");
    testService.setNew(true);
    testService = servicesRepository.save(testService);

    ServicesVO updateVO = new ServicesVO();
    updateVO.setName("Servicio Editado");
    updateVO.setDescription("Nueva descripción");
    updateVO.setUrls(new ArrayList<>());

    mockMvc
        .perform(
            put("/api/v1/services/{id}", testService.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateVO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Servicio Editado"));
  }

  @Test
  @WithMockUser(authorities = "ADMIN")
  @DisplayName("PUT /api/v1/services/{id} - Debe retornar 409 cuando el nombre está duplicado")
  public void updateService_ReturnsConflict_WhenNameDuplicated() throws Exception {
    ServicesEntity service1 = new ServicesEntity();
    service1.setName("Servicio Uno");
    service1.setNew(true);
    servicesRepository.save(service1);

    ServicesEntity service2 = new ServicesEntity();
    service2.setName("Servicio Dos");
    service2.setNew(true);
    service2 = servicesRepository.save(service2);

    ServicesVO updateVO = new ServicesVO();
    updateVO.setName("Servicio Uno");
    updateVO.setUrls(new ArrayList<>());

    mockMvc
        .perform(
            put("/api/v1/services/{id}", service2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateVO)))
        .andExpect(status().isConflict());
  }
}
>>>>>>> 606e4bd42340fbd4b31b54b24726042f5f51716d
