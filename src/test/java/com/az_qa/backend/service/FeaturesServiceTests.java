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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.az_qa.backend.dao.FeatureDAO;
import com.az_qa.backend.vo.FeatureVO;
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
}
