/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.vo.FeatureVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FeaturesDAOTests {

  @Mock private FeaturesRepository featuresRepository;

  @InjectMocks private FeatureDAO featureDAO;

  private FeatureEntity featureEntity;
  private FeatureVO featureVO;

  @BeforeEach
  void setUp() {
    featureEntity = new FeatureEntity();
    featureEntity.setId(1L);
    featureEntity.setName("Test DAO");

    featureVO = new FeatureVO();
    featureVO.setId(1L);
    featureVO.setFeatureName("Test DAO");
  }

  @Test
  @DisplayName("POST: createFeature: Debe mapear, guardar y retornar el VO")
  void createFeature_Success() {
    when(featuresRepository.save(any(FeatureEntity.class))).thenReturn(featureEntity);

    FeatureVO result = featureDAO.createFeature(featureVO);

    assertNotNull(result);
    assertEquals(featureVO.getFeatureName(), result.getFeatureName());
  }
}
