/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.entity.ReleaseEntity;
import com.az_qa.backend.entity.ReleasedFeaturesEntity;
import com.az_qa.backend.entity.ServicesEntity;
import com.az_qa.backend.enumeration.ReleaseStatus;
import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.repository.ReleaseRepository;
import com.az_qa.backend.repository.ReleasedFeaturesRepository;
import com.az_qa.backend.repository.ServicesRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ReportControllerIntegrationTest {

  @Autowired private WebApplicationContext context;
  @Autowired private ReleaseRepository releaseRepository;
  @Autowired private ServicesRepository servicesRepository;
  @Autowired private FeaturesRepository featuresRepository;
  @Autowired private ReleasedFeaturesRepository releasedFeaturesRepository;

  private MockMvc mockMvc;

  private Long savedServiceId;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

    releasedFeaturesRepository.deleteAll();
    releaseRepository.deleteAll();
    featuresRepository.deleteAll();
    servicesRepository.deleteAll();

    ServicesEntity service = new ServicesEntity();
    service.setName("Auth Service");
    service.setNew(true);
    service = servicesRepository.save(service);
    savedServiceId = service.getId();

    FeatureEntity feature = new FeatureEntity();
    feature.setName("Login Feature");
    feature.setDescription("Feature de autenticacion");
    feature.setService(service);
    feature = featuresRepository.save(feature);

    ReleaseEntity release =
        new ReleaseEntity(
            "Release Integracion",
            "Objetivo de prueba de integracion",
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 3, 15),
            "2.0.0",
            "qa,integracion",
            ReleaseStatus.Active,
            true);
    release.setNew(true);
    release = releaseRepository.save(release);

    ReleasedFeaturesEntity rf = new ReleasedFeaturesEntity(release, feature);
    releasedFeaturesRepository.save(rf);
  }

  @Test
  @WithMockUser
  @DisplayName("GET /api/v1/reports - Debe retornar 200 con la lista de releases")
  public void getReports_NoFilter_Returns200WithReleases() throws Exception {
    mockMvc
        .perform(get("/api/v1/reports"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].releaseName").value("Release Integracion"))
        .andExpect(jsonPath("$[0].releaseVersion").value("2.0.0"))
        .andExpect(jsonPath("$[0].releaseStatus").value("Active"));
  }

  @Test
  @WithMockUser
  @DisplayName(
      "GET /api/v1/reports - Filtro por serviceId debe retornar solo los releases del servicio")
  public void getReports_FilterByServiceId_ReturnsMatchingReleases() throws Exception {
    mockMvc
        .perform(get("/api/v1/reports").param("serviceId", savedServiceId.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].releaseName").value("Release Integracion"))
        .andExpect(jsonPath("$[0].services[0].serviceName").value("Auth Service"))
        .andExpect(jsonPath("$[0].services[0].features[0].featureName").value("Login Feature"));
  }

  @Test
  @WithMockUser
  @DisplayName("GET /api/v1/reports - Filtro por serviceId inexistente debe retornar lista vacía")
  public void getReports_FilterByNonExistentServiceId_ReturnsEmptyList() throws Exception {
    mockMvc
        .perform(get("/api/v1/reports").param("serviceId", "99999"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  @WithMockUser
  @DisplayName("GET /api/v1/reports - Filtro por rango de fechas que incluye el release lanzado")
  public void getReports_FilterByDateRange_ReturnsMatchingReleases() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/reports").param("startDate", "2026-01-01").param("endDate", "2026-12-31"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].releaseName").value("Release Integracion"));
  }

  @Test
  @WithMockUser
  @DisplayName(
      "GET /api/v1/reports - Filtro por rango de fechas que excluye el release debe retornar lista"
          + " vacía")
  public void getReports_FilterByDateRangeOutOfRange_ReturnsEmptyList() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/reports").param("startDate", "2020-01-01").param("endDate", "2020-12-31"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  @WithMockUser
  @DisplayName("GET /api/v1/reports - Filtro por tagName que coincide debe retornar el release")
  public void getReports_FilterByTagName_ReturnsMatchingRelease() throws Exception {
    mockMvc
        .perform(get("/api/v1/reports").param("tagName", "qa"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].releaseName").value("Release Integracion"));
  }

  @Test
  @WithMockUser
  @DisplayName("GET /api/v1/reports - Filtro por tagName inexistente debe retornar lista vacía")
  public void getReports_FilterByNonExistentTag_ReturnsEmptyList() throws Exception {
    mockMvc
        .perform(get("/api/v1/reports").param("tagName", "tag-que-no-existe"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  @WithMockUser
  @DisplayName("GET /api/v1/reports/export - Debe retornar 200 y el archivo CSV en el body")
  public void exportReportsCsv_Returns200WithCsv() throws Exception {
    mockMvc
        .perform(get("/api/v1/reports/export"))
        .andExpect(status().isOk())
        .andExpect(
            header()
                .string(
                    HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reportes_releases.csv"))
        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8"))
        .andExpect(content().string(containsString("Versión del release;Nombre del release")));
  }
}
