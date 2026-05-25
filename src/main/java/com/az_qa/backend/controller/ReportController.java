/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.controller;

import com.az_qa.backend.service.ReportService;
import com.az_qa.backend.vo.ReportReleaseVO;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/api/reports")
public class ReportController {

  private final ReportService reportService;

  public ReportController(ReportService reportService) {
    this.reportService = reportService;
  }

  /**
   * Returns a filtered list of releases in JSON format. All query parameters are optional; omitting
   * a parameter disables that filter.
   *
   * @param serviceId only releases containing a feature from this service
   * @param startDate lower bound (inclusive) for {@code releaseLaunchDate} (ISO-8601, e.g.
   *                  2026-01-15)
   * @param endDate   upper bound (inclusive) for {@code releaseLaunchDate}
   * @param tagName   case-insensitive substring to match against the release tags
   * @return 200 OK with the list of matching {@link ReportReleaseVO} objects
   */
  @GetMapping
  public ResponseEntity<List<ReportReleaseVO>> getReports(
      @RequestParam(required = false) Long serviceId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate endDate,
      @RequestParam(required = false) String tagName) {
    List<ReportReleaseVO> reports =
        reportService.getReports(serviceId, startDate, endDate, tagName);
    return ResponseEntity.ok(reports);
  }
}
