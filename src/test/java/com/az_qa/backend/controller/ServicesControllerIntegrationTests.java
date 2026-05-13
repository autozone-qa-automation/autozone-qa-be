/*
 * Tecnológico de Monterrey — Campus Chihuahua
 * Desarrollo e Implantación de Sistemas de Software
 * TC3005B GPO500 - 2026
 * Autozone QA Automation
 */

package com.az_qa.backend.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.az_qa.backend.repository.ServicesRepository;
import com.az_qa.backend.vo.ServicesVO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ServicesControllerIntegrationTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ServicesRepository servicesRepository;

  private ObjectMapper objectMapper = new ObjectMapper();

  private ServicesVO serviceVO;

  @BeforeEach
  void setUp() {
    servicesRepository.deleteAll();

    serviceVO = new ServicesVO();
    serviceVO.setName("Servicio de Integracion");
    serviceVO.setDescription("Descripción válida para el test de integración");
  }

  @Test
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
