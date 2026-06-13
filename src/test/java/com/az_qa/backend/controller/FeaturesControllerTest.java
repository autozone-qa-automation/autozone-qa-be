/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.az_qa.backend.service.FeaturesService;
import com.az_qa.backend.vo.FeatureVO;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class FeaturesControllerTest {

  @Mock private FeaturesService featuresService;

  @InjectMocks private FeaturesController featuresController;

  private FeatureVO featureStub;

  @BeforeEach
  void setUp() {
    featureStub = new FeatureVO();
    featureStub.setId(1L);
    featureStub.setFeatureName("Test Unitario");
  }

  @Test
  @DisplayName("getFeatureById: Debe retornar 200 OK con la feature cuando existe")
  public void getFeatureById_Success() {
    when(featuresService.getFeatureById(1L)).thenReturn(featureStub);

    ResponseEntity<FeatureVO> response = featuresController.getFeatureById(1L);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Test Unitario", response.getBody().getFeatureName());
    assertEquals(1L, response.getBody().getId());
  }

  @Test
  @DisplayName("getFeaturesByServiceId: Debe retornar 200 OK con la lista de features del servicio")
  public void getFeaturesByServiceId_Success() {
    List<FeatureVO> featureList = List.of(featureStub);
    when(featuresService.getFeaturesByServiceId(1L)).thenReturn(featureList);

    ResponseEntity<List<FeatureVO>> response = featuresController.getFeaturesByServiceId(1L);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
    assertEquals("Test Unitario", response.getBody().get(0).getFeatureName());
  }

  @Test
  @DisplayName(
      "getFeaturesByServiceId: Debe retornar 200 OK con lista vacía cuando no hay features")
  public void getFeaturesByServiceId_ReturnsEmptyList() {
    when(featuresService.getFeaturesByServiceId(99L)).thenReturn(Collections.emptyList());

    ResponseEntity<List<FeatureVO>> response = featuresController.getFeaturesByServiceId(99L);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().isEmpty());
  }

  @Test
  @DisplayName("getAll: Debe retornar 200 OK con la lista de todas las features")
  public void getAll_ReturnsList() {
    List<FeatureVO> featureList = List.of(featureStub);
    when(featuresService.getAllFeatures()).thenReturn(featureList);

    ResponseEntity<List<FeatureVO>> response = featuresController.getAll();

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
  }

  @Test
  @DisplayName("getAll: Debe retornar 200 OK con lista vacía cuando no hay features")
  public void getAll_ReturnsEmptyList() {
    when(featuresService.getAllFeatures()).thenReturn(Collections.emptyList());

    ResponseEntity<List<FeatureVO>> response = featuresController.getAll();

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().isEmpty());
  }

  @Test
  @DisplayName("create: Debe retornar 201 Created cuando el servicio tiene éxito")
  public void create_Success() {
    when(featuresService.createFeature(any(FeatureVO.class))).thenReturn(featureStub);

    ResponseEntity<FeatureVO> response = featuresController.create(new FeatureVO());

    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals("Test Unitario", response.getBody().getFeatureName());
  }

  @Test
  @DisplayName("create: Debe retornar 400 Bad Request cuando el servicio devuelve null")
  public void create_ReturnsBadRequest() {
    when(featuresService.createFeature(any(FeatureVO.class))).thenReturn(null);

    ResponseEntity<FeatureVO> response = featuresController.create(new FeatureVO());

    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  @DisplayName("update: Debe retornar 200 OK cuando la actualización es exitosa")
  public void update_Success() {

    FeatureVO updatedFeature = new FeatureVO();
    updatedFeature.setId(1L);
    updatedFeature.setFeatureName("Feature Actualizada");

    when(featuresService.updateFeature(any(Long.class), any(FeatureVO.class)))
        .thenReturn(updatedFeature);

    ResponseEntity<FeatureVO> response = featuresController.update(1L, updatedFeature);

    assertNotNull(response);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    assertEquals("Feature Actualizada", response.getBody().getFeatureName());
  }

  @Test
  @DisplayName("deactivate: Debe retornar 200 OK cuando se desactiva la feature")
  public void deactivate_Success() {
    ResponseEntity<Void> response = featuresController.deactivate(1L);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}
