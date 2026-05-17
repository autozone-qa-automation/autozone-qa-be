/*
 * Tecnológico de Monterrey — Campus Chihuahua
 * Desarrollo e Implantación de Sistemas de Software
 * TC3005B GPO500 - 2026
 * Autozone QA Automation
 */

package com.az_qa.backend.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.entity.ServicesEntity;
import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.repository.ServicesRepository;
import com.az_qa.backend.vo.FeatureVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class FeaturesControllerIntegrationTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private FeaturesRepository featuresRepository;

  @Autowired private ServicesRepository servicesRepository;

  private ObjectMapper objectMapper = new ObjectMapper();

  private FeatureVO featureVO;

  private Long validServiceId;

  @BeforeEach
  void setUp() {
    featuresRepository.deleteAll();
    servicesRepository.deleteAll();

    ServicesEntity testService = new ServicesEntity();
    testService.setName("Servicio de Integracion");
    testService.setDescription("Descripción del servicio de prueba");
    testService.setNew(true);

    testService = servicesRepository.save(testService);

    validServiceId = testService.getId();

    featureVO = new FeatureVO();
    featureVO.setFeatureName("Integration Test Feature");
    featureVO.setFeatureDescription("Esta es una descripcion valida para pasar el test");
    featureVO.setIdService(validServiceId);
  }

  @Test
  @DisplayName("POST /api/v1/features - Integración completa (Controller -> Service -> DAO -> DB)")
  public void createFeature_IntegrationSuccess() throws Exception {

    mockMvc
        .perform(
            post("/api/v1/features")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(featureVO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.featureName").value("Integration Test Feature"))
        .andExpect(jsonPath("$.idService").value(validServiceId));

    assertNotNull(featuresRepository.findAll().get(0));
  }

  @Test
  @DisplayName("PUT /api/v1/features/{id} - Integración completa para actualizar feature")
  public void updateFeature_IntegrationSuccess() throws Exception {

    FeatureEntity existingFeature = new FeatureEntity();

    existingFeature.setName("Old Feature Name");
    existingFeature.setDescription("Old Feature Description");

    existingFeature.setService(servicesRepository.findById(validServiceId).orElseThrow());

    existingFeature = featuresRepository.save(existingFeature);

    Long featureId = existingFeature.getId();
    FeatureVO updatedFeatureVO = new FeatureVO();

    updatedFeatureVO.setFeatureName("Updated Feature Name");

    updatedFeatureVO.setFeatureDescription("Updated Feature Description");

    updatedFeatureVO.setIdService(validServiceId);

    mockMvc
        .perform(
            put("/api/v1/features/{id}", featureId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedFeatureVO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(featureId))
        .andExpect(jsonPath("$.featureName").value("Updated Feature Name"))
        .andExpect(jsonPath("$.featureDescription").value("Updated Feature Description"));

    FeatureEntity updatedEntity = featuresRepository.findById(featureId).orElseThrow();

    assertNotNull(updatedEntity);
  }
}
