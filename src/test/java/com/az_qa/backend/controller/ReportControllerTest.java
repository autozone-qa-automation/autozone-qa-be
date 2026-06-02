/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.az_qa.backend.enumeration.ReleaseStatus;
import com.az_qa.backend.exception.GlobalExceptionHandler;
import com.az_qa.backend.service.ReportService;
import com.az_qa.backend.vo.ReportReleaseVO;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {

  private MockMvc mockMvc;

  @Mock private ReportService reportService;

  @InjectMocks private ReportController reportController;

  private ReportReleaseVO reportStub;

  @BeforeEach
  void setUp() {
    // Configuración Standalone en lugar de @WebMvcTest para evitar errores de
    // dependencias de Maven
    // Inyecta directamente el GlobalExceptionHandler cumpliendo las reglas del
    // proyecto
    mockMvc =
        MockMvcBuilders.standaloneSetup(reportController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

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

  @Nested
  @DisplayName("GET /api/v1/reports")
  class GetReports {

    @Test
    @DisplayName("Debe retornar 200 OK con la lista de reportes")
    void getReports_Success() throws Exception {
      when(reportService.getReports(any(), any(), any(), any())).thenReturn(List.of(reportStub));

      mockMvc
          .perform(get("/api/v1/reports").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$").isArray())
          .andExpect(jsonPath("$[0].releaseName").value("Test Release"));
    }

    @Test
    @DisplayName("Debe retornar 200 OK con lista vacía cuando ningún release coincide")
    void getReports_EmptyResult() throws Exception {
      when(reportService.getReports(any(), any(), any(), any())).thenReturn(List.of());

      mockMvc
          .perform(
              get("/api/v1/reports")
                  .param("serviceId", "1")
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$").isEmpty());
    }
  }

  @Nested
  @DisplayName("GET /api/v1/reports/export")
  class ExportReports {

    @Test
    @DisplayName(
        "Debe retornar 200 OK con el archivo CSV y headers correctos al pasar IDs de release")
    void exportReportsCsv_Success() throws Exception {
      // Arrange
      byte[] mockCsv = "Versión del release;Nombre del release\n1.0.0;Test Release".getBytes();
      List<Long> targetIds = List.of(1L, 2L, 3L);

      // Corregido: Ahora se mockea el nuevo método exportReportsCsvByIds que recibe
      // la lista de IDs
      when(reportService.exportReportsCsvByIds(targetIds)).thenReturn(mockCsv);

      // Act & Assert
      mockMvc
          .perform(
              get("/api/v1/reports/export")
                  .param(
                      "releaseIds",
                      "1,2,3")) // Corregido: Se pasa el parámetro requerido por el Controller
          .andExpect(status().isOk())
          .andExpect(
              header()
                  .string(
                      HttpHeaders.CONTENT_DISPOSITION,
                      "attachment; filename=reportes_releases.csv"))
          .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8"))
          .andExpect(content().bytes(mockCsv));
    }
  }
}
