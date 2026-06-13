/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.az_qa.backend.dao.FeatureDAO;
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.vo.FeatureVO;
import com.az_qa.backend.vo.TestCaseVO;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FeaturesServiceTests {

  @Mock private FeatureDAO featureDAO;
  @Mock private TestCasesService testCasesService;

  @InjectMocks private FeaturesService featuresService;

  private FeatureVO featureStub;

  @BeforeEach
  void setUp() {
    featureStub = new FeatureVO();
    featureStub.setId(10L);
    featureStub.setFeatureName("Unit Test Feature");
    featureStub.setIdService(1L);
  }

  @Test
  @DisplayName("GET: Debe retornar la feature cuando el DAO responde correctamente")
  public void getFeatureById_ReturnsFeature() {
    when(featureDAO.getFeatureById(10L)).thenReturn(featureStub);

    FeatureVO result = featuresService.getFeatureById(10L);

    assertNotNull(result);
    assertEquals(10L, result.getId());
    assertEquals("Unit Test Feature", result.getFeatureName());
  }

  @Test
  @DisplayName("GET: Debe propagar ItemNotFoundException cuando el DAO no encuentra la feature")
  public void getFeatureById_ThrowsItemNotFoundException() {
    when(featureDAO.getFeatureById(99L))
        .thenThrow(new ItemNotFoundException("Feature with id {99} not found."));

    assertThrows(ItemNotFoundException.class, () -> featuresService.getFeatureById(99L));
  }

  @Test
  @DisplayName(
      "GET: Debe retornar la lista de features del servicio cuando el DAO responde correctamente")
  public void getFeaturesByServiceId_ReturnsList() {
    List<FeatureVO> featureList = List.of(featureStub);
    when(featureDAO.getFeaturesByServiceId(1L)).thenReturn(featureList);

    List<FeatureVO> result = featuresService.getFeaturesByServiceId(1L);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Unit Test Feature", result.get(0).getFeatureName());
  }

  @Test
  @DisplayName("GET: Debe retornar lista vacía cuando no hay features para el servicio")
  public void getFeaturesByServiceId_ReturnsEmptyList() {
    when(featureDAO.getFeaturesByServiceId(99L)).thenReturn(Collections.emptyList());

    List<FeatureVO> result = featuresService.getFeaturesByServiceId(99L);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("GET: Debe retornar todas las features cuando el DAO responde correctamente")
  public void getAllFeatures_ReturnsList() {
    List<FeatureVO> featureList = List.of(featureStub);
    when(featureDAO.getAllFeatures()).thenReturn(featureList);

    List<FeatureVO> result = featuresService.getAllFeatures();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Unit Test Feature", result.get(0).getFeatureName());
  }

  @Test
  @DisplayName("GET: Debe retornar lista vacía cuando no hay features en el DAO")
  public void getAllFeatures_ReturnsEmptyList() {
    when(featureDAO.getAllFeatures()).thenReturn(Collections.emptyList());

    List<FeatureVO> result = featuresService.getAllFeatures();

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("POST: Debe retornar la característica creada cuando el DAO responde correctamente")
  public void createFeature_ReturnsCreatedFeature() {
    when(featureDAO.createFeature(any(FeatureVO.class))).thenReturn(featureStub);

    FeatureVO result = featuresService.createFeature(new FeatureVO());

    assertNotNull(result, "El objeto creado no debería ser nulo");
    assertEquals(10L, result.getId());
    assertEquals("Unit Test Feature", result.getFeatureName());
  }

  @Test
  @DisplayName("POST: Debe manejar el caso donde el DAO retorna null")
  public void createFeature_ReturnsNull_WhenDaoFails() {
    when(featureDAO.createFeature(any(FeatureVO.class))).thenReturn(null);

    FeatureVO result = featuresService.createFeature(new FeatureVO());

    assertNull(result, "Debería retornar null si el DAO falla al insertar");
  }

  @Test
  @DisplayName("PUT: Debe retornar la feature actualizada correctamente")
  public void updateFeature_ReturnsUpdatedFeature() {

    FeatureVO updatedFeature = new FeatureVO();
    updatedFeature.setId(10L);
    updatedFeature.setFeatureName("Updated Feature");

    when(featureDAO.updateFeature(any(Long.class), any(FeatureVO.class)))
        .thenReturn(updatedFeature);

    FeatureVO result = featuresService.updateFeature(10L, updatedFeature);

    assertNotNull(result);

    assertEquals(10L, result.getId());

    assertEquals("Updated Feature", result.getFeatureName());
  }

  @Test
  @DisplayName("PUT: Debe retornar null cuando el DAO falla al actualizar")
  public void updateFeature_ReturnsNull_WhenDaoFails() {

    when(featureDAO.updateFeature(any(Long.class), any(FeatureVO.class))).thenReturn(null);

    FeatureVO result = featuresService.updateFeature(10L, new FeatureVO());

    assertNull(result);
  }

  @Test
  @DisplayName("PUT: deactivateFeature: Debe desactivar la feature")
  public void deactivateFeature_Success() {
    when(testCasesService.getByFeature(10L)).thenReturn(Collections.emptyList());

    featuresService.deactivateFeature(10L);

    verify(featureDAO).deactivateFeature(10L);
    verify(testCasesService).getByFeature(10L);
  }

  @Test
  @DisplayName("PUT: deactivateFeature: Debe desactivar feature y test cases relacionados")
  public void deactivateFeature_DeactivatesRelatedTestCases() {
    TestCaseVO testCase = new TestCaseVO();
    testCase.setId(5L);

    when(testCasesService.getByFeature(10L)).thenReturn(List.of(testCase));

    featuresService.deactivateFeature(10L);

    verify(featureDAO).deactivateFeature(10L);
    verify(testCasesService).deactivate(5L);
  }
}
