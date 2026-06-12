/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.az_qa.backend.dao.ReleaseDAO;
import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.entity.ReleaseEntity;
import com.az_qa.backend.enumeration.ReleaseStatus;
import com.az_qa.backend.exception.BadRequestException;
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.exception.ResourceNotFoundException;
import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.repository.ReleaseRepository;
import com.az_qa.backend.repository.ReleasedFeaturesRepository;
import com.az_qa.backend.repository.TestCasesRepository;
import com.az_qa.backend.vo.ReleaseVO;
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
public class ReleaseServiceTest {

  @Mock private ReleaseDAO releaseDAO;
  @Mock private ReleaseRepository releaseRepository;
  @Mock private FeaturesRepository featuresRepository;
  @Mock private ReleasedFeaturesRepository releasedFeaturesRepository;
  @Mock private TestCasesRepository testCasesRepository;

  @InjectMocks private ReleaseService releaseService;

  private ReleaseVO releaseStub;

  @BeforeEach
  void setUp() {
    releaseStub = new ReleaseVO();
    releaseStub.setReleaseId(1L);
    releaseStub.setReleaseName("Service Test Release");
    releaseStub.setReleaseStatus(ReleaseStatus.Draft);

    ReleaseVO releaseActive = new ReleaseVO();
    releaseActive.setReleaseId(2L);
    releaseActive.setReleaseName("Service Test Active Release");
    releaseActive.setReleaseStatus(ReleaseStatus.Active);
    releaseActive.setReleaseIsActive(true);

    ReleaseVO releaseInactive = new ReleaseVO();
    releaseInactive.setReleaseId(3L);
    releaseInactive.setReleaseName("Service Test Inactive Release");
    releaseInactive.setReleaseStatus(ReleaseStatus.Active);
    releaseInactive.setReleaseIsActive(false);
  }

  @Test
  @DisplayName("GET /last: Debe retornar los últimos 5 releases ordenados por fecha de creación")
  public void getLastReleases_Success() {
    when(releaseDAO.findLast(null)).thenReturn(Collections.singletonList(releaseStub));

    List<ReleaseVO> result = releaseService.getLastReleases(null);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Service Test Release", result.get(0).getReleaseName());
  }

  @Test
  @DisplayName("GET: Debe retornar el release con sus servicios asociados cuando el ID existe")
  public void getReleaseById_Success() {
    when(releaseDAO.findById(1L)).thenReturn(releaseStub);
    when(releaseRepository.findNombresServiciosByReleaseId(1L))
        .thenReturn(Collections.singletonList("Authentication Service"));

    ReleaseVO result = releaseService.getReleaseById(1L);

    assertNotNull(result, "El objeto obtenido no debería ser nulo");
    assertEquals(1L, result.getReleaseId());
    assertEquals("Service Test Release", result.getReleaseName());
    assertEquals(1, result.getReleaseServices().size());
  }

  @Test
  @DisplayName("GET: Debe lanzar ResourceNotFoundException cuando el ID no existe en el DAO")
  public void getReleaseById_ThrowsResourceNotFoundException() {
    when(releaseDAO.findById(99L)).thenThrow(new ItemNotFoundException("Not found"));

    assertThrows(
        ResourceNotFoundException.class,
        () -> {
          releaseService.getReleaseById(99L);
        });
  }

  @Test
  @DisplayName("POST: Debe crear el release exitosamente sin features asociadas")
  public void createRelease_WithoutFeatures_Success() {
    when(releaseDAO.save(any(ReleaseVO.class))).thenReturn(releaseStub);

    ReleaseVO inputVO = new ReleaseVO();
    inputVO.setReleaseName("New Release");
    inputVO.setReleaseFeatureIds(Collections.emptyList());

    ReleaseVO result = releaseService.createRelease(inputVO);

    assertNotNull(result);
    assertEquals(1L, result.getReleaseId());
  }

  @Test
  @DisplayName("POST: Debe lanzar ResourceNotFoundException si alguna feature provista no existe")
  public void createRelease_FeatureNotFound_ThrowsException() {
    when(releaseDAO.save(any(ReleaseVO.class))).thenReturn(releaseStub);
    when(featuresRepository.findAllById(any())).thenReturn(Collections.emptyList());

    ReleaseVO inputVO = new ReleaseVO();
    inputVO.setReleaseName("New Release");
    inputVO.setReleaseFeatureIds(List.of(101L));

    assertThrows(
        ResourceNotFoundException.class,
        () -> {
          releaseService.createRelease(inputVO);
        });
  }

  @Test
  @DisplayName("POST: Debe asociar features y retornar el objeto completo tras la inserción")
  public void createRelease_WithFeatures_Success() {
    when(releaseDAO.save(any(ReleaseVO.class))).thenReturn(releaseStub);

    FeatureEntity mockFeature = new FeatureEntity();
    when(featuresRepository.findAllById(any())).thenReturn(List.of(mockFeature));
    when(releaseRepository.getReferenceById(1L)).thenReturn(new ReleaseEntity());
    when(releaseDAO.findById(1L)).thenReturn(releaseStub);

    ReleaseVO inputVO = new ReleaseVO();
    inputVO.setReleaseName("New Release");
    inputVO.setReleaseFeatureIds(List.of(101L));

    ReleaseVO result = releaseService.createRelease(inputVO);

    assertNotNull(result);
    assertEquals(1L, result.getReleaseId());
  }

  @Test
  @DisplayName("PUT: Debe lanzar IllegalArgumentException si la transición de estado no es válida")
  public void updateReleaseStatus_InvalidTransition_ThrowsException() {
    releaseStub.setReleaseStatus(ReleaseStatus.Active);
    when(releaseDAO.findById(1L)).thenReturn(releaseStub);

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          releaseService.updateReleaseStatus(1L, ReleaseStatus.Draft);
        });
  }

  @Test
  @DisplayName(
      "PUT: Debe actualizar el estado correctamente si cumple con las reglas de transición")
  public void updateReleaseStatus_Success() {
    when(releaseDAO.findById(1L)).thenReturn(releaseStub);
    when(releaseDAO.save(any(ReleaseVO.class))).thenReturn(releaseStub);
    when(releaseDAO.findById(1L)).thenReturn(releaseStub);

    ReleaseVO result = releaseService.updateReleaseStatus(1L, ReleaseStatus.Progress);

    assertNotNull(result);
  }

  @Test
  @DisplayName(
      "DELETE: Debe hacer soft delete correctamente cuando el status es Draft y está activo")
  public void deleteRelease_Success() {
    ReleaseVO releaseDraft = new ReleaseVO();
    releaseDraft.setReleaseId(4L);
    releaseDraft.setReleaseStatus(ReleaseStatus.Draft);
    releaseDraft.setReleaseIsActive(true);

    when(releaseDAO.findById(4L)).thenReturn(releaseDraft);
    when(releaseRepository.findNombresServiciosByReleaseId(4L)).thenReturn(Collections.emptyList());
    when(releasedFeaturesRepository.findByRelease_ReleaseId(4L))
        .thenReturn(Collections.emptyList());
    when(testCasesRepository.findByRelease_ReleaseId(4L)).thenReturn(Collections.emptyList());

    ReleaseEntity mockEntity = new ReleaseEntity();
    mockEntity.setReleaseIsActive(true);
    when(releaseRepository.getReferenceById(4L)).thenReturn(mockEntity);

    releaseService.deleteReleaseById(4L);

    // Verify soft delete
    org.junit.jupiter.api.Assertions.assertFalse(mockEntity.getReleaseIsActive());
    org.mockito.Mockito.verify(releaseRepository).save(mockEntity);
  }

  @Test
  @DisplayName("DELETE: Debe lanzar BadRequestException cuando el estado no es Draft")
  public void deleteRelease_ThrowsBadRequestException_WhenNotDraft() {
    ReleaseVO releaseProgress = new ReleaseVO();
    releaseProgress.setReleaseId(2L);
    releaseProgress.setReleaseStatus(ReleaseStatus.Progress);
    releaseProgress.setReleaseIsActive(true);

    when(releaseDAO.findById(2L)).thenReturn(releaseProgress);
    when(releaseRepository.findNombresServiciosByReleaseId(2L)).thenReturn(Collections.emptyList());

    assertThrows(
        BadRequestException.class,
        () -> {
          releaseService.deleteReleaseById(2L);
        });
  }

  @Test
  @DisplayName("DELETE: Debe lanzar BadRequestException cuando el release no está activo")
  public void deleteRelease_ThrowsBadRequestException_WhenNotActive() {
    ReleaseVO releaseDraftInactive = new ReleaseVO();
    releaseDraftInactive.setReleaseId(3L);
    releaseDraftInactive.setReleaseStatus(ReleaseStatus.Draft);
    releaseDraftInactive.setReleaseIsActive(false);

    when(releaseDAO.findById(3L)).thenReturn(releaseDraftInactive);
    when(releaseRepository.findNombresServiciosByReleaseId(3L)).thenReturn(Collections.emptyList());

    assertThrows(
        BadRequestException.class,
        () -> {
          releaseService.deleteReleaseById(3L);
        });
  }

  @Test
  @DisplayName("DELETE: Debe lanzar ResourceNotFoundException cuando el ID no existe")
  public void deleteRelease_ThrowsResourceNotFoundException_WhenIdNotFound() {
    when(releaseDAO.findById(99L)).thenThrow(new ItemNotFoundException("Not found"));

    assertThrows(
        ResourceNotFoundException.class,
        () -> {
          releaseService.deleteReleaseById(99L);
        });
  }
}
