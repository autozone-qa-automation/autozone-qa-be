/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.az_qa.backend.repository.FeaturesRepository;
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

  // CAMBIO CLAVE: Instancia manual para evitar el UnsatisfiedDependency
  private ObjectMapper objectMapper = new ObjectMapper();

  private FeatureVO featureVO;

  @BeforeEach
  void setUp() {
    featuresRepository.deleteAll();

    featureVO = new FeatureVO();
    featureVO.setFeatureName("Integration Test Feature");
    featureVO.setFeatureDescription("Esta es una descripcion valida para pasar el test");
    featureVO.setIdService(1L);
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
        .andExpect(jsonPath("$.idService").value(1L));

    assertNotNull(featuresRepository.findAll().get(0));
  }
}
