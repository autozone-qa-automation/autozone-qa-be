package com.az_qa.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.az_qa.backend.service.FeaturesService;
import com.az_qa.backend.vo.FeatureVO;
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
}
