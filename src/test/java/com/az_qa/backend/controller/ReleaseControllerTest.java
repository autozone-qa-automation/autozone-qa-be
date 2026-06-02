/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.az_qa.backend.enumeration.ReleaseStatus;
import com.az_qa.backend.exception.ResourceNotFoundException;
import com.az_qa.backend.service.ReleaseService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ReleaseControllerTest {

  @Mock private ReleaseService releaseService;

  @InjectMocks private ReleaseController releaseController;

  private ReleaseVO releaseStub;

  @BeforeEach
  void setUp() {
    releaseStub = new ReleaseVO();
    releaseStub.setReleaseId(1L);
    releaseStub.setReleaseName("QA Automation Release");
    releaseStub.setReleaseStatus(ReleaseStatus.Active);
  }

  @Test
  @DisplayName("getAllReleases: Debe retornar 200 OK con la lista de releases filtrados")
  public void getAllReleases_Success() {
    when(releaseService.getReleasesFiltered(any(), any()))
        .thenReturn(Collections.singletonList(releaseStub));

    ResponseEntity<List<ReleaseVO>> response = releaseController.getAllReleases("qa", "Active");

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals("QA Automation Release", response.getBody().get(0).getReleaseName());
  }

  @Test
  @DisplayName("getReleaseById: Debe retornar 200 OK cuando el ID existe")
  public void getReleaseById_Success() {
    when(releaseService.getReleaseById(1L)).thenReturn(releaseStub);

    ResponseEntity<ReleaseVO> response = releaseController.getReleaseById(1L);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1L, response.getBody().getReleaseId());
  }

  @Test
  @DisplayName("getReleaseById: Debe retornar 404 Not Found cuando el recurso no existe")
  public void getReleaseById_NotFound() {
    when(releaseService.getReleaseById(99L))
        .thenThrow(new ResourceNotFoundException("Release not found"));

    ResponseEntity<ReleaseVO> response = releaseController.getReleaseById(99L);

    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  @DisplayName("createRelease: Debe retornar 201 Created cuando se crea correctamente")
  public void createRelease_Success() {
    when(releaseService.createRelease(any(ReleaseVO.class))).thenReturn(releaseStub);

    ResponseEntity<ReleaseVO> response = releaseController.createRelease(new ReleaseVO());

    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("QA Automation Release", response.getBody().getReleaseName());
  }

  @Test
  @DisplayName("updateReleaseStatus: Debe retornar 200 OK cuando la actualización es exitosa")
  public void updateReleaseStatus_Success() {
    when(releaseService.updateReleaseStatus(eq(1L), any(ReleaseStatus.class)))
        .thenReturn(releaseStub);

    ResponseEntity<?> response = releaseController.updateReleaseStatus(1L, ReleaseStatus.Active);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  @DisplayName(
      "updateReleaseStatus: Debe retornar 404 Not Found cuando el ID a actualizar no existe")
  public void updateReleaseStatus_NotFound() {
    when(releaseService.updateReleaseStatus(eq(99L), any(ReleaseStatus.class)))
        .thenThrow(new ResourceNotFoundException("Release not found"));

    ResponseEntity<?> response = releaseController.updateReleaseStatus(99L, ReleaseStatus.Active);

    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  @DisplayName("updateReleaseStatus: Debe retornar 400 Bad Request cuando el estado es inválido")
  public void updateReleaseStatus_BadRequest() {
    when(releaseService.updateReleaseStatus(eq(1L), any(ReleaseStatus.class)))
        .thenThrow(new IllegalArgumentException("Invalid status"));

    ResponseEntity<?> response = releaseController.updateReleaseStatus(1L, ReleaseStatus.Draft);

    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
}
