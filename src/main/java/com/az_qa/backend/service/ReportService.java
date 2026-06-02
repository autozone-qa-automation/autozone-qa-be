/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.entity.ReleaseEntity;
import com.az_qa.backend.entity.ReleasedFeaturesEntity;
import com.az_qa.backend.entity.ServicesEntity;
import com.az_qa.backend.entity.TestCasesEntity;
import com.az_qa.backend.repository.ReleaseRepository;
import com.az_qa.backend.repository.ReleasedFeaturesRepository;
import com.az_qa.backend.repository.TestCasesRepository;
import com.az_qa.backend.vo.ReportFeatureVO;
import com.az_qa.backend.vo.ReportReleaseVO;
import com.az_qa.backend.vo.ReportServiceVO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service for querying release reports with optional filters. */
@Service
public class ReportService {

  private final ReleaseRepository releaseRepository;
  private final ReleasedFeaturesRepository releasedFeaturesRepository;
  private final TestCasesRepository testCasesRepository;

  public ReportService(
      ReleaseRepository releaseRepository,
      ReleasedFeaturesRepository releasedFeaturesRepository,
      TestCasesRepository testCasesRepository) {
    this.releaseRepository = releaseRepository;
    this.releasedFeaturesRepository = releasedFeaturesRepository;
    this.testCasesRepository = testCasesRepository;
  }

  /**
   * Returns a list of releases matching the provided filters. All parameters are optional; passing
   * {@code null} skips that filter.
   *
   * @param serviceId only releases containing a feature from this service
   * @param startDate lower bound (inclusive) for {@code releaseLaunchDate}
   * @param endDate   upper bound (inclusive) for {@code releaseLaunchDate}
   * @param tagName   case-insensitive substring to match against the release tags
   * @return list of report VOs with the hierarchical release → service → feature → test-case
   *         structure
   */
  @Transactional(readOnly = true)
  public List<ReportReleaseVO> getReports(
      Long serviceId, LocalDate startDate, LocalDate endDate, String tagName) {
    return releaseRepository.findByFilters(serviceId, startDate, endDate, tagName).stream()
        .map(this::toReportVO)
        .toList();
  }

  private ReportReleaseVO toReportVO(ReleaseEntity release) {
    List<String> tags =
        release.getReleaseTags() == null
            ? List.of()
            : Arrays.stream(release.getReleaseTags().split(","))
                .map(String::trim)
                .filter(t -> !t.isEmpty())
                .toList();

    Map<String, List<ReportFeatureVO>> byService = new LinkedHashMap<>();

    List<ReleasedFeaturesEntity> releasedFeatures =
        releasedFeaturesRepository.findByRelease_ReleaseId(release.getReleaseId());

    for (ReleasedFeaturesEntity rf : releasedFeatures) {
      FeatureEntity feature = rf.getFeature();
      if (feature == null) continue;

      ServicesEntity svc = feature.getService();
      String serviceName = svc != null ? svc.getName() : "Unknown";

      List<String> tcTitles =
          testCasesRepository.findByFeature_IdAndIsActive(feature.getId(), true).stream()
              .map(TestCasesEntity::getTitle)
              .toList();

      byService
          .computeIfAbsent(serviceName, k -> new ArrayList<>())
          .add(new ReportFeatureVO(feature.getName(), tcTitles));
    }

    List<ReportServiceVO> services =
        byService.entrySet().stream()
            .map(e -> new ReportServiceVO(e.getKey(), e.getValue()))
            .toList();

    return new ReportReleaseVO(
        release.getReleaseId(),
        release.getReleaseName(),
        release.getReleaseDescription(),
        release.getReleaseVersion(),
        release.getReleaseStatus(),
        tags,
        release.getReleaseCreationDate(),
        release.getReleaseLaunchDate(),
        services);
  }
}
