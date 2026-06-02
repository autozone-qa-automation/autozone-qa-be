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

import com.az_qa.backend.enumeration.ReleaseStatus;
import com.az_qa.backend.service.ReportService;
import com.az_qa.backend.vo.ReportReleaseVO;
import java.time.LocalDate;
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
public class ReportControllerTest {

  @Mock private ReportService reportService;

  @InjectMocks private ReportController reportController;

  private ReportReleaseVO reportStub;

  @BeforeEach
  void setUp() {
    reportStub =
        new ReportReleaseVO(
            1L,
            "Test Release",
            "Objetivo de prueba",
            "1.0.0",
            ReleaseStatus.Active,
            List.of("qa"),
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 3, 1),
            List.of());
  }

  @Test
  @DisplayName("getReports: Debe retornar 200 OK con la lista de reportes")
  public void getReports_Success() {
    when(reportService.getReports(any(), any(), any(), any())).thenReturn(List.of(reportStub));

    ResponseEntity<List<ReportReleaseVO>> response =
        reportController.getReports(null, null, null, null);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals("Test Release", response.getBody().get(0).getReleaseName());
  }

  @Test
  @DisplayName("getReports: Debe retornar 200 OK con lista vacía cuando ningún release coincide")
  public void getReports_EmptyResult() {
    when(reportService.getReports(any(), any(), any(), any())).thenReturn(List.of());

    ResponseEntity<List<ReportReleaseVO>> response =
        reportController.getReports(1L, null, null, null);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isEmpty());
  }
}
