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
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.exception.ResourceNotFoundException;
import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.repository.ReleaseRepository;
import com.az_qa.backend.repository.ReleasedFeaturesRepository;
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

  @InjectMocks private ReleaseService releaseService;

  private ReleaseVO releaseStub;

  @BeforeEach
  void setUp() {
    releaseStub = new ReleaseVO();
    releaseStub.setReleaseId(1L);
    releaseStub.setReleaseName("Service Test Release");
    releaseStub.setReleaseStatus(ReleaseStatus.Draft);
  }

  @Test
  @DisplayName("GET /last: Debe retornar los últimos 5 releases ordenados por fecha de creación")
  public void getLastReleases_Success() {
    when(releaseDAO.findLast()).thenReturn(Collections.singletonList(releaseStub));

    List<ReleaseVO> result = releaseService.getLastReleases();

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
}
