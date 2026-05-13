/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.repository.ServicesRepository;
import com.az_qa.backend.vo.FeatureVO;
import java.util.Optional;
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

  @Mock private ServicesRepository servicesRepository;

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

  @Test
  @DisplayName("PUT: updateFeature: Debe actualizar correctamente la feature")
  void updateFeature_Success() {

    FeatureEntity existingEntity = new FeatureEntity();
    existingEntity.setId(1L);
    existingEntity.setName("Old Feature");

    FeatureVO updateVO = new FeatureVO();
    updateVO.setFeatureName("Updated Feature");
    updateVO.setFeatureDescription("Nueva descripción");

    when(featuresRepository.findById(1L)).thenReturn(Optional.of(existingEntity));

    when(featuresRepository.findByName("Updated Feature")).thenReturn(Optional.empty());

    when(featuresRepository.save(any(FeatureEntity.class))).thenReturn(existingEntity);

    FeatureVO result = featureDAO.updateFeature(1L, updateVO);

    assertNotNull(result);

    assertEquals("Updated Feature", existingEntity.getName());

    assertEquals("Nueva descripción", existingEntity.getDescription());
  }

  @Test
  @DisplayName("PUT: updateFeature: Debe lanzar excepción cuando feature no existe")
  void updateFeature_ThrowsException_WhenFeatureDoesNotExist() {

    FeatureVO updateVO = new FeatureVO();
    updateVO.setFeatureName("Updated Feature");

    when(featuresRepository.findById(1L)).thenReturn(Optional.empty());

    ItemNotFoundException exception =
        assertThrows(ItemNotFoundException.class, () -> featureDAO.updateFeature(1L, updateVO));

    assertEquals("Feature with id {1} not found.", exception.getMessage());
  }

  @Test
  @DisplayName("PUT: updateFeature: Debe lanzar excepción cuando el nombre es null")
  void updateFeature_ThrowsException_WhenNameIsNull() {

    FeatureEntity existingEntity = new FeatureEntity();
    existingEntity.setId(1L);

    FeatureVO updateVO = new FeatureVO();

    updateVO.setFeatureName(null);

    when(featuresRepository.findById(1L)).thenReturn(Optional.of(existingEntity));

    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> featureDAO.updateFeature(1L, updateVO));

    assertEquals("Feature name cannot be empty.", exception.getMessage());
  }

  @Test
  @DisplayName("PUT: updateFeature: Debe lanzar excepción cuando el nombre está vacío")
  void updateFeature_ThrowsException_WhenNameIsBlank() {

    FeatureEntity existingEntity = new FeatureEntity();
    existingEntity.setId(1L);

    FeatureVO updateVO = new FeatureVO();

    updateVO.setFeatureName("");

    when(featuresRepository.findById(1L)).thenReturn(Optional.of(existingEntity));

    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> featureDAO.updateFeature(1L, updateVO));

    assertEquals("Feature name cannot be empty.", exception.getMessage());
  }

  @Test
  @DisplayName("PUT: updateFeature: Debe lanzar excepción cuando el nombre ya existe")
  void updateFeature_ThrowsException_WhenNameAlreadyExists() {

    FeatureEntity existingEntity = new FeatureEntity();
    existingEntity.setId(1L);

    FeatureEntity duplicatedEntity = new FeatureEntity();
    duplicatedEntity.setId(2L);

    FeatureVO updateVO = new FeatureVO();
    updateVO.setFeatureName("Duplicated Feature");

    when(featuresRepository.findById(1L)).thenReturn(Optional.of(existingEntity));

    when(featuresRepository.findByName("Duplicated Feature"))
        .thenReturn(Optional.of(duplicatedEntity));

    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> featureDAO.updateFeature(1L, updateVO));

    assertEquals("Feature name 'Duplicated Feature' already exists.", exception.getMessage());
  }

  @Test
  @DisplayName("PUT: updateFeature: Debe permitir mismo nombre si pertenece al mismo ID")
  void updateFeature_AllowsSameName_WhenSameId() {

    FeatureEntity existingEntity = new FeatureEntity();
    existingEntity.setId(1L);

    FeatureEntity duplicatedEntity = new FeatureEntity();
    duplicatedEntity.setId(1L);

    FeatureVO updateVO = new FeatureVO();
    updateVO.setFeatureName("Same Feature");

    when(featuresRepository.findById(1L)).thenReturn(Optional.of(existingEntity));

    when(featuresRepository.findByName("Same Feature")).thenReturn(Optional.of(duplicatedEntity));

    when(featuresRepository.save(any(FeatureEntity.class))).thenReturn(existingEntity);

    FeatureVO result = featureDAO.updateFeature(1L, updateVO);

    assertNotNull(result);
  }
}
