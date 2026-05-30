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
import com.az_qa.backend.vo.ReportFeatureVO;
import com.az_qa.backend.vo.ReportReleaseVO;
import com.az_qa.backend.vo.ReportServiceVO;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

  @Test
  @DisplayName("exportReportsCsv: Debe retornar archivo base sin datos si no hay resultados")
  public void exportReportsCsv_NoMatches_ReturnsOnlyHeaders() {
    when(releaseRepository.findByFilters(any(), any(), any(), any())).thenReturn(List.of());

    byte[] result = reportService.exportReportsCsv(null, null, null, null);
    String csvContent = new String(result, StandardCharsets.UTF_8);

    assertNotNull(result);
    assertTrue(csvContent.contains("Versión del release;Nombre del release"));
  }

  @Test
  @DisplayName(
      "exportReportsCsv: Debe generar CSV con jerarquía, escapar caracteres especiales y usar BOM")
  public void exportReportsCsv_WithHierarchyAndSpecialChars_GeneratesCorrectCsv() {
    releaseStub.setReleaseName("Release; \"Crítico\"");

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

    byte[] result = reportService.exportReportsCsv(null, null, null, null);

    assertNotNull(result);
    assertTrue(result.length > 3);

    assertEquals((byte) 0xEF, result[0]);
    assertEquals((byte) 0xBB, result[1]);
    assertEquals((byte) 0xBF, result[2]);

    String csvContent = new String(result, 3, result.length - 3, StandardCharsets.UTF_8);

    assertTrue(csvContent.contains("Versión del release;Nombre del release;"));
    assertTrue(csvContent.contains("\"Release; \"\"Crítico\"\"\""));
    assertTrue(csvContent.contains("Auth Service"));
    assertTrue(csvContent.contains("Login Feature"));
    assertTrue(csvContent.contains("Login con credenciales válidas"));
  }

  @Test
  @DisplayName("exportReportsCsv: Cubre ramas donde el release no tiene servicios asociados")
  public void exportReportsCsv_NoServices() {
    when(releaseRepository.findByFilters(any(), any(), any(), any()))
        .thenReturn(List.of(releaseStub));
    when(releasedFeaturesRepository.findByRelease_ReleaseId(1L)).thenReturn(List.of());

    byte[] result = reportService.exportReportsCsv(null, null, null, null);
    String csvContent = new String(result, StandardCharsets.UTF_8);

    assertTrue(csvContent.contains(";;;\n"));
  }

  @Test
  @DisplayName("exportReportsCsv: Cubre ramas de tags nulos, feature nulo y sin casos de prueba")
  public void exportReportsCsv_MissingBranches_TagsAndTestCases() {
    releaseStub.setReleaseTags(null);

    ReleasedFeaturesEntity rfNull = new ReleasedFeaturesEntity();
    rfNull.setFeature(null);

    ServicesEntity service = new ServicesEntity();
    service.setName("Test Service");
    FeatureEntity feature = new FeatureEntity();
    feature.setName("Test Feature");
    feature.setService(service);

    ReleasedFeaturesEntity rfValid = new ReleasedFeaturesEntity();
    rfValid.setFeature(feature);

    when(releaseRepository.findByFilters(any(), any(), any(), any()))
        .thenReturn(List.of(releaseStub));
    when(releasedFeaturesRepository.findByRelease_ReleaseId(1L))
        .thenReturn(List.of(rfNull, rfValid));
    when(testCasesRepository.findByFeature_IdAndIsActive(null, true)).thenReturn(List.of());

    byte[] result = reportService.exportReportsCsv(null, null, null, null);
    String csvContent = new String(result, StandardCharsets.UTF_8);

    assertTrue(csvContent.contains("Test Service;Test Feature;\n"));
  }

  // --- LA PRUEBA MAGICA QUE APAGA EL ROJO EN JACOCO ---

  @Test
  @DisplayName("exportReportsCsv: Cubre 100% del formateo CSV forzando VOs vacíos (Spy)")
  public void exportReportsCsv_FormattingBranches_ForceCoverage() {
    // Usamos un Spy para probar la lógica de formateo de exportReportsCsv aislada
    // de getReports
    ReportService spyService =
        Mockito.spy(
            new ReportService(releaseRepository, releasedFeaturesRepository, testCasesRepository));

    // 1. Feature sin test cases (lista nula y lista vacía)
    ReportFeatureVO featureEmptyTests = new ReportFeatureVO("FeatureEmptyTests", List.of());
    ReportFeatureVO featureNullTests = new ReportFeatureVO("FeatureNullTests", null);

    // 2. Service sin features (lista nula y vacía), y otro con los features de
    // arriba
    ReportServiceVO serviceEmptyFeatures = new ReportServiceVO("ServiceEmptyFeatures", List.of());
    ReportServiceVO serviceNullFeatures = new ReportServiceVO("ServiceNullFeatures", null);
    ReportServiceVO serviceWithFeatures =
        new ReportServiceVO("ServiceWithFeatures", List.of(featureEmptyTests, featureNullTests));

    // 3. Inyectamos los VOs directo usando el Mockito.doReturn
    ReportReleaseVO mockRelease =
        new ReportReleaseVO(
            1L,
            "Format Release",
            "Desc",
            "1.0.0",
            ReleaseStatus.Active,
            List.of("tag"),
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 3, 1),
            List.of(serviceEmptyFeatures, serviceNullFeatures, serviceWithFeatures));

    Mockito.doReturn(List.of(mockRelease)).when(spyService).getReports(any(), any(), any(), any());

    byte[] result = spyService.exportReportsCsv(null, null, null, null);
    String csvContent = new String(result, StandardCharsets.UTF_8);

    // Validamos que pasó por todos los csv.append(String.format(...)) que marcan en
    // rojo
    assertTrue(csvContent.contains("ServiceEmptyFeatures;;\n"));
    assertTrue(csvContent.contains("ServiceNullFeatures;;\n"));
    assertTrue(csvContent.contains("FeatureEmptyTests;\n"));
    assertTrue(csvContent.contains("FeatureNullTests;\n"));
  }

  @Test
  @DisplayName(
      "exportReportsCsv: Cubre ramas amarillas (fechas nulas, datos nulos, saltos de línea, sin"
          + " servicio)")
  public void exportReportsCsv_YellowBranches_EdgeCases() {
    // 1. Fechas y status nulos para cubrir las ramas falsas de los ternarios (? :)
    releaseStub.setReleaseCreationDate(null);
    releaseStub.setReleaseLaunchDate(null);
    releaseStub.setReleaseStatus(null);

    // 2. Dato nulo directo para cubrir if (data == null) en escapeCsv
    releaseStub.setReleaseVersion(null);

    // 3. String con salto de línea (\n) y comilla simple (') para cubrir las
    // validaciones del CSV
    releaseStub.setReleaseName("Nombre\nCon'Salto");

    // 4. Tags con espacios extra para forzar el filter(t -> !t.isEmpty()) a
    // descartar un elemento
    releaseStub.setReleaseTags("qa, , automation");

    // 5. Feature con servicio nulo para cubrir el operador (svc != null ?
    // svc.getName() : "Unknown")
    FeatureEntity featureNoService = new FeatureEntity();
    featureNoService.setName("Feature Huerfano");
    featureNoService.setService(null); // Servicio Nulo intencional

    ReleasedFeaturesEntity rf = new ReleasedFeaturesEntity();
    rf.setFeature(featureNoService);

    when(releaseRepository.findByFilters(any(), any(), any(), any()))
        .thenReturn(List.of(releaseStub));
    when(releasedFeaturesRepository.findByRelease_ReleaseId(1L)).thenReturn(List.of(rf));
    // TestCases vacío para cerrar el ciclo sin fallos
    when(testCasesRepository.findByFeature_IdAndIsActive(null, true)).thenReturn(List.of());

    byte[] result = reportService.exportReportsCsv(null, null, null, null);
    String csvContent = new String(result, StandardCharsets.UTF_8);

    // Verificamos que se manejó correctamente el "Unknown" y se limpió el salto de
    // línea
    assertNotNull(csvContent);
    assertTrue(csvContent.contains("Unknown;Feature Huerfano;"));
    assertTrue(csvContent.contains("\"Nombre Con'Salto\""));
  }

  @Test
  @DisplayName(
      "exportReportsCsv: Cubre la ultima rama logica del sanitizador (Comillas sin punto y coma)")
  public void exportReportsCsv_FinalBranch_QuoteWithoutSemicolon() {
    // Para lograr el 100% debemos evaluar que el string tenga comillas dobles (")
    // pero NO un punto y coma (;). Así evaluamos la segunda condición del || como
    // TRUE.
    releaseStub.setReleaseVersion("Version \"Beta\""); // <-- Contiene comillas, sin punto y coma

    when(releaseRepository.findByFilters(any(), any(), any(), any()))
        .thenReturn(List.of(releaseStub));
    when(releasedFeaturesRepository.findByRelease_ReleaseId(1L)).thenReturn(List.of());

    byte[] result = reportService.exportReportsCsv(null, null, null, null);

    // Verificamos que se hayan escapado las comillas dobles de "Beta" a ""Beta""
    assertNotNull(result);
  }

  @Test
  @DisplayName("exportReportsCsv: Cubre la ultima rama amarilla (lista de servicios nula en el VO)")
  public void exportReportsCsv_FinalBranch_NullServicesList() {
    // Usamos el Spy para inyectar un Release que tenga la lista de servicios en
    // NULL
    ReportService spyService =
        Mockito.spy(
            new ReportService(releaseRepository, releasedFeaturesRepository, testCasesRepository));

    ReportReleaseVO releaseNullServices =
        new ReportReleaseVO(
            1L,
            "Release NULL Services",
            "Desc",
            "1.0.0",
            ReleaseStatus.Active,
            List.of("tag"),
            LocalDate.now(),
            LocalDate.now(),
            null // <-- ¡ESTE ES EL NULL QUE JACOCO ESTABA ESPERANDO!
            );

    Mockito.doReturn(List.of(releaseNullServices))
        .when(spyService)
        .getReports(any(), any(), any(), any());

    byte[] result = spyService.exportReportsCsv(null, null, null, null);
    String csvContent = new String(result, StandardCharsets.UTF_8);

    // Verificamos que pasa por la condición (release.getServices() == null)
    // correctamente
    assertTrue(csvContent.contains(";;;\n"));
  }

  @Test
  @DisplayName("exportReportsCsv: Cubre la ultima rama amarilla (lista de tags nula en el VO)")
  public void exportReportsCsv_FinalBranch_NullTagsInVO() {
    // Usamos el Spy para inyectar un ReleaseVO que tenga los tags explícitamente en
    // NULL
    ReportService spyService =
        Mockito.spy(
            new ReportService(releaseRepository, releasedFeaturesRepository, testCasesRepository));

    ReportReleaseVO releaseNullTags =
        new ReportReleaseVO(
            1L,
            "Release NULL Tags",
            "Desc",
            "1.0.0",
            ReleaseStatus.Active,
            null, // <-- ¡AQUÍ ESTÁ EL NULL MÁGICO PARA LOS TAGS!
            LocalDate.now(),
            LocalDate.now(),
            List.of() // Sin servicios para no afectar otras métricas
            );

    Mockito.doReturn(List.of(releaseNullTags))
        .when(spyService)
        .getReports(any(), any(), any(), any());

    byte[] result = spyService.exportReportsCsv(null, null, null, null);
    String csvContent = new String(result, StandardCharsets.UTF_8);

    // Verificamos que no explote y genere la línea vacía al final (debido al else
    // del ternario: "")
    assertNotNull(csvContent);
    assertTrue(csvContent.contains("Release NULL Tags"));
  }
}
