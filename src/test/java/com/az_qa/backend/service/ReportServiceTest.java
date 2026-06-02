/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.entity.ReleaseEntity;
import com.az_qa.backend.entity.ReleasedFeaturesEntity;
import com.az_qa.backend.entity.ServicesEntity;
import com.az_qa.backend.entity.TestCasesEntity;
import com.az_qa.backend.enumeration.ReleaseStatus;
import com.az_qa.backend.repository.ReleaseRepository;
import com.az_qa.backend.repository.ReleasedFeaturesRepository;
import com.az_qa.backend.repository.TestCasesRepository;
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

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

  @Mock private ReleaseRepository releaseRepository;
  @Mock private ReleasedFeaturesRepository releasedFeaturesRepository;
  @Mock private TestCasesRepository testCasesRepository;

  @InjectMocks private ReportService reportService;

  private ReleaseEntity releaseStub;

  @BeforeEach
  void setUp() {
    releaseStub = new ReleaseEntity();
    releaseStub.setReleaseId(1L);
    releaseStub.setReleaseName("Test Release");
    releaseStub.setReleaseDescription("Objetivo de prueba");
    releaseStub.setReleaseVersion("1.0.0");
    releaseStub.setReleaseStatus(ReleaseStatus.Active);
    releaseStub.setReleaseCreationDate(LocalDate.of(2026, 1, 1));
    releaseStub.setReleaseLaunchDate(LocalDate.of(2026, 3, 1));
    releaseStub.setReleaseTags("qa,automation");
  }

  @Test
  @DisplayName("getReports: Debe retornar lista vacía cuando no hay releases que coincidan")
  public void getReports_NoMatches_ReturnsEmpty() {
    when(releaseRepository.findByFilters(any(), any(), any(), any())).thenReturn(List.of());

    List<ReportReleaseVO> result = reportService.getReports(null, null, null, null);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("getReports: Debe retornar reportes mapeados correctamente sin filtros")
  public void getReports_WithNullFilters_ReturnsMappedReports() {
    when(releaseRepository.findByFilters(null, null, null, null)).thenReturn(List.of(releaseStub));
    when(releasedFeaturesRepository.findByRelease_ReleaseId(1L)).thenReturn(List.of());

    List<ReportReleaseVO> result = reportService.getReports(null, null, null, null);

    assertNotNull(result);
    assertEquals(1, result.size());
    ReportReleaseVO report = result.get(0);
    assertEquals(1L, report.getReleaseId());
    assertEquals("Test Release", report.getReleaseName());
    assertEquals("1.0.0", report.getReleaseVersion());
    assertEquals(ReleaseStatus.Active, report.getReleaseStatus());
    assertEquals(2, report.getReleaseTags().size());
    assertTrue(report.getReleaseTags().contains("qa"));
    assertTrue(report.getReleaseTags().contains("automation"));
  }

  @Test
  @DisplayName("getReports: Debe delegar el filtro por serviceId al repositorio")
  public void getReports_FilterByServiceId_DelegatesToRepository() {
    when(releaseRepository.findByFilters(eq(5L), any(), any(), any()))
        .thenReturn(List.of(releaseStub));
    when(releasedFeaturesRepository.findByRelease_ReleaseId(1L)).thenReturn(List.of());

    List<ReportReleaseVO> result = reportService.getReports(5L, null, null, null);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  @DisplayName("getReports: Debe delegar el filtro por rango de fechas al repositorio")
  public void getReports_FilterByDateRange_DelegatesToRepository() {
    LocalDate start = LocalDate.of(2026, 1, 1);
    LocalDate end = LocalDate.of(2026, 12, 31);
    when(releaseRepository.findByFilters(any(), eq(start), eq(end), any()))
        .thenReturn(List.of(releaseStub));
    when(releasedFeaturesRepository.findByRelease_ReleaseId(1L)).thenReturn(List.of());

    List<ReportReleaseVO> result = reportService.getReports(null, start, end, null);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  @DisplayName("getReports: Debe delegar el filtro por tagName al repositorio")
  public void getReports_FilterByTagName_DelegatesToRepository() {
    when(releaseRepository.findByFilters(any(), any(), any(), eq("qa")))
        .thenReturn(List.of(releaseStub));
    when(releasedFeaturesRepository.findByRelease_ReleaseId(1L)).thenReturn(List.of());

    List<ReportReleaseVO> result = reportService.getReports(null, null, null, "qa");

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  @DisplayName("getReports: Debe construir la jerarquía servicio → feature → casos de prueba")
  public void getReports_WithFeatures_BuildsHierarchy() {
    ServicesEntity service = new ServicesEntity();
    service.setName("Auth Service");

    FeatureEntity feature = new FeatureEntity();
    feature.setName("Login Feature");
    feature.setService(service);

    ReleasedFeaturesEntity rf = new ReleasedFeaturesEntity();
    rf.setFeature(feature);

    TestCasesEntity tc = new TestCasesEntity();
    tc.setTitle("Login con credenciales válidas");

    when(releaseRepository.findByFilters(any(), any(), any(), any()))
        .thenReturn(List.of(releaseStub));
    when(releasedFeaturesRepository.findByRelease_ReleaseId(1L)).thenReturn(List.of(rf));
    when(testCasesRepository.findByFeature_IdAndIsActive(null, true)).thenReturn(List.of(tc));

    List<ReportReleaseVO> result = reportService.getReports(null, null, null, null);

    assertNotNull(result);
    assertEquals(1, result.size());
    ReportReleaseVO report = result.get(0);
    assertEquals(1, report.getServices().size());
    assertEquals("Auth Service", report.getServices().get(0).getServiceName());
    assertEquals(1, report.getServices().get(0).getFeatures().size());
    assertEquals(
        "Login Feature", report.getServices().get(0).getFeatures().get(0).getFeatureName());
    assertEquals(1, report.getServices().get(0).getFeatures().get(0).getTestCases().size());
    assertEquals(
        "Login con credenciales válidas",
        report.getServices().get(0).getFeatures().get(0).getTestCases().get(0));
  }
}
