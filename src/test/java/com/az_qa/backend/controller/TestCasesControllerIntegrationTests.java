/*
 * Tecnológico de Monterrey — Campus Chihuahua
 * Desarrollo e Implantación de Sistemas de Software
 * TC3005B GPO500 - 2026
 * Autozone QA Automation
 */

package com.az_qa.backend.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.entity.TestCasesEntity;
import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.repository.TestCasesRepository;
import com.az_qa.backend.vo.TestCaseVO;
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
public class TestCasesControllerIntegrationTests {

  @Autowired private WebApplicationContext context;

  @Autowired private TestCasesRepository testCasesRepository;

  @Autowired private FeaturesRepository featuresRepository;

  private MockMvc mockMvc;

  private ObjectMapper objectMapper = new ObjectMapper();

  private TestCaseVO testCaseVO;

  private Long validFeatureId;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

    testCasesRepository.deleteAll();
    featuresRepository.deleteAll();

    FeatureEntity testFeature = new FeatureEntity();
    testFeature.setName("Feature de Integracion");
    testFeature.setDescription("Descripción del feature de prueba");

    testFeature = featuresRepository.save(testFeature);

    validFeatureId = testFeature.getId();

    testCaseVO = new TestCaseVO();
    testCaseVO.setTitle("Integration Test Case");
    testCaseVO.setSteps("1. Paso uno\n2. Paso dos");
    testCaseVO.setExpectedOutput("Resultado esperado del test case de integración");
    testCaseVO.setFeatureId(validFeatureId);
    testCaseVO.setType(com.az_qa.backend.enumeration.TestCaseType.ON_DEMAND);
  }

  @Test
  @WithMockUser(authorities = "ADMIN")
  @DisplayName(
      "PUT /api/v1/test-cases/{id} - Integracion completa para actualizar un test case existente")
  public void updateTestCase_IntegrationSuccess() throws Exception {

    TestCasesEntity existingTestCase = new TestCasesEntity();
    existingTestCase.setTitle("Test Case a Actualizar");
    existingTestCase.setSteps("Pasos iniciales");
    existingTestCase.setExpectedOutput("Resultado inicial");
    existingTestCase.setFeature(featuresRepository.findById(validFeatureId).orElse(null));
    existingTestCase.setType(com.az_qa.backend.enumeration.TestCaseType.ON_DEMAND);
    existingTestCase.setNew(true);
    existingTestCase = testCasesRepository.save(existingTestCase);

    testCaseVO.setTitle("Test Case Actualizado");
    testCaseVO.setSteps("1. Paso actualizado\n2. Paso adicional");
    testCaseVO.setExpectedOutput("Resultado actualizado");

    mockMvc
        .perform(
            put("/api/v1/test-cases/{id}", existingTestCase.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCaseVO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Test Case Actualizado"))
        .andExpect(jsonPath("$.steps").value("1. Paso actualizado\n2. Paso adicional"))
        .andExpect(jsonPath("$.expectedOutput").value("Resultado actualizado"));

    TestCasesEntity updatedTestCase =
        testCasesRepository.findById(existingTestCase.getId()).orElseThrow();
    assertNotNull(updatedTestCase);
  }

  @Test
  @WithMockUser(authorities = "ADMIN")
  @DisplayName("PUT /api/v1/test-cases/{id}/deactivate - Desactiva un test case activo")
  public void deactivateTestCase_IntegrationSuccess() throws Exception {
    TestCasesEntity existingTestCase = new TestCasesEntity();
    existingTestCase.setTitle("Test Case a Desactivar");
    existingTestCase.setSteps("Pasos del test case");
    existingTestCase.setExpectedOutput("Resultado esperado");
    existingTestCase.setFeature(featuresRepository.findById(validFeatureId).orElse(null));
    existingTestCase.setType(com.az_qa.backend.enumeration.TestCaseType.ON_DEMAND);
    existingTestCase.setNew(true);
    existingTestCase = testCasesRepository.save(existingTestCase);

    mockMvc
        .perform(put("/api/v1/test-cases/{id}/deactivate", existingTestCase.getId()))
        .andExpect(status().isNoContent());

    assertTrue(testCasesRepository.findByIdAndIsActive(existingTestCase.getId(), true).isEmpty());
    assertTrue(
        testCasesRepository.findByIdAndIsActive(existingTestCase.getId(), false).isPresent());
  }

  @Test
  @WithMockUser(authorities = "ADMIN")
  @DisplayName("PUT /api/v1/test-cases/{id}/deactivate - Retorna 404 si no existe")
  public void deactivateTestCase_NotFound() throws Exception {
    mockMvc
        .perform(put("/api/v1/test-cases/{id}/deactivate", 999L))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("TestCase not found with id: 999"));
  }

  @Test
  @WithMockUser(authorities = "ADMIN")
  @DisplayName("PUT /api/v1/test-cases/{id}/deactivate - Retorna 400 si el id no es positivo")
  public void deactivateTestCase_InvalidId() throws Exception {
    mockMvc
        .perform(put("/api/v1/test-cases/{id}/deactivate", 0L))
        .andExpect(status().isBadRequest());
  }
}
