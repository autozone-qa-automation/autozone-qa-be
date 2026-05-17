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
public class FeaturesControllerIntegrationTests {

  @Autowired private WebApplicationContext context;

  @Autowired private FeaturesRepository featuresRepository;

  @Autowired private ServicesRepository servicesRepository;

  private MockMvc mockMvc;

  private ObjectMapper objectMapper = new ObjectMapper();

  private FeatureVO featureVO;

  private Long validServiceId;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

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
  @WithMockUser
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
}
