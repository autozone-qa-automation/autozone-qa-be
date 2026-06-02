/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.controller;

import com.az_qa.backend.service.ReportService;
import com.az_qa.backend.vo.ReportReleaseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for the Reports module.
 * Provides endpoints to query releases with optional filters.
 */
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

  private final ReportService reportService;

  public ReportController(ReportService reportService) {
    this.reportService = reportService;
  }

  /**
   * Returns a filtered list of releases in JSON format. All query parameters are
   * optional; omitting
   * a parameter disables that filter.
   *
   * @param serviceId only releases containing a feature from this service
   * @param startDate lower bound (inclusive) for {@code releaseLaunchDate}
   *                  (ISO-8601, e.g.
   *                  2026-01-15)
   * @param endDate   upper bound (inclusive) for {@code releaseLaunchDate}
   * @param tagName   case-insensitive substring to match against the release tags
   * @return 200 OK with the list of matching {@link ReportReleaseVO} objects
   */
  @GetMapping
  public ResponseEntity<List<ReportReleaseVO>> getReports(
      @RequestParam(required = false) Long serviceId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
      @RequestParam(required = false) String tagName) {
    List<ReportReleaseVO> reports = reportService.getReports(serviceId, startDate, endDate, tagName);
    return ResponseEntity.ok(reports);
  }

  /**
   * Exporta una lista específica de releases en formato CSV basada en sus IDs.
   * Basado en el requerimiento de descarga de reportes del SRS (Página 15).
   *
   * @param releaseIds Lista de IDs de los releases a exportar
   * @return 200 OK con el archivo CSV adjunto para su descarga
   */
  @Operation(summary = "Exportar reportes a CSV por IDs", description = "Genera y descarga un archivo CSV conteniendo exclusivamente los releases cuyos IDs son"
      + " proporcionados.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Archivo CSV generado exitosamente", content = @Content(mediaType = "text/csv"))
  })
  @GetMapping(value = "/export", produces = "text/csv")
  public ResponseEntity<byte[]> exportReportsCsv(@RequestParam List<Long> releaseIds) {

    byte[] csvData = reportService.exportReportsCsvByIds(releaseIds);

    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reportes_releases.csv");
    headers.set(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");

    return ResponseEntity.ok().headers(headers).body(csvData);
  }
}
