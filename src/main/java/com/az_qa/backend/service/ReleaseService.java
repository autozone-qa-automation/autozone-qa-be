/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import com.az_qa.backend.dao.ReleaseDAO;
import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.entity.ReleaseEntity;
import com.az_qa.backend.entity.ReleasedFeaturesEntity;
import com.az_qa.backend.entity.TestCasesEntity;
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.exception.ResourceNotFoundException;
import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.repository.ReleaseRepository;
import com.az_qa.backend.repository.ReleasedFeaturesRepository;
import com.az_qa.backend.repository.TestCasesRepository;
import com.az_qa.backend.vo.ReleaseVO;
import java.time.LocalDate;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReleaseService {
  private final ReleaseDAO releaseDAO;
  private final ReleaseRepository releaseRepository;
  private final FeaturesRepository featuresRepository;
  private final ReleasedFeaturesRepository releasedFeaturesRepository;
  private final TestCasesRepository testCasesRepository;

  public ReleaseService(
      ReleaseDAO releaseDAO,
      ReleaseRepository releaseRepository,
      FeaturesRepository featuresRepository,
      ReleasedFeaturesRepository releasedFeaturesRepository,
      TestCasesRepository testCasesRepository) {
    this.releaseDAO = releaseDAO;
    this.releaseRepository = releaseRepository;
    this.featuresRepository = featuresRepository;
    this.releasedFeaturesRepository = releasedFeaturesRepository;
    this.testCasesRepository = testCasesRepository;
  }

  /**
   * Retrieves a release by its ID.
   *
   * @param id the release's ID
   * @return the matching release
   */
  public ReleaseVO getReleaseById(Long id) {
    try {
      ReleaseVO vo = releaseDAO.findById(id);
      vo.setReleaseServices(releaseRepository.findNombresServiciosByReleaseId(id));
      return vo;
    } catch (ItemNotFoundException e) {
      throw new ResourceNotFoundException("Release with id {" + id + "} not found.");
    }
  }

  /**
   * Retrieves releases based on filter criteria.
   *
   * @param releaseStatus the status to filter by
   * @param releaseTags the tags to filter by
   * @return a list of filtered releases
   */
  public List<ReleaseVO> getReleasesFiltered(String releaseStatus, String releaseTags) {
    return releaseDAO.findFiltered(releaseStatus, releaseTags);
  }

  /**
   * Retrieves all registered releases.
   *
   * @return a list of all releases
   */
  public List<ReleaseVO> getAllReleases() {
    return releaseDAO.findAll();
  }

  /**
   * Creates a new release.
   *
   * @param releaseVO the release payload
   * @return the created release
   */
  @Transactional
  public ReleaseVO createRelease(ReleaseVO releaseVO) {
    releaseVO.setReleaseId(null);
    if (releaseVO.getReleaseCreationDate() == null) {
      releaseVO.setReleaseCreationDate(LocalDate.now());
    }

    ReleaseVO createdRelease = releaseDAO.save(releaseVO);
    List<Long> featureIds = releaseVO.getReleaseFeatureIds();
    if (featureIds == null || featureIds.isEmpty()) {
      return createdRelease;
    }

    List<FeatureEntity> features = featuresRepository.findAllById(featureIds);
    if (features.size() != featureIds.stream().distinct().count()) {
      throw new ResourceNotFoundException("One or more release features were not found.");
    }

    ReleaseEntity release = releaseRepository.getReferenceById(createdRelease.getReleaseId());
    releasedFeaturesRepository.saveAll(
        features.stream().map(feature -> new ReleasedFeaturesEntity(release, feature)).toList());

    return getReleaseById(createdRelease.getReleaseId());
  }

  /**
   * Updates the status of an existing release following the predefined state machine rules.
   *
   * @param id        the release's ID
   * @param newStatus the new status to apply
   * @return the updated release value object
   * @throws ResourceNotFoundException if the release is not found
   * @throws IllegalArgumentException  if the status transition is prohibited
   */
  @Transactional
  public ReleaseVO updateReleaseStatus(
      Long id, com.az_qa.backend.enumeration.ReleaseStatus newStatus) {
    try {
      ReleaseVO releaseVO = releaseDAO.findById(id);
      com.az_qa.backend.enumeration.ReleaseStatus currentStatus = releaseVO.getReleaseStatus();

      // Validate transition logic using the Enum's built-in rules
      if (!currentStatus.canTransitionTo(newStatus)) {
        throw new IllegalArgumentException(
            "Invalid status transition from " + currentStatus + " to " + newStatus);
      }

      releaseVO.setReleaseStatus(newStatus);
      releaseDAO.save(releaseVO);

      return getReleaseById(id);
    } catch (ItemNotFoundException e) {
      throw new ResourceNotFoundException("Release with id {" + id + "} not found.");
    }
  }

  /**
   * Deletes a release by its ID.
   *
   * @param id the release's ID
   * @return HTTP status indicating the result of the operation
   * @throws BadRequestException
   */
  @Transactional
  public void deleteReleaseById(Long id) throws BadRequestException {
    try {
      ReleaseVO releaseVO = releaseDAO.findById(id);
      releaseVO.setReleaseServices(releaseRepository.findNombresServiciosByReleaseId(id));
      com.az_qa.backend.enumeration.ReleaseStatus currentStatus = releaseVO.getReleaseStatus();
      if (currentStatus != com.az_qa.backend.enumeration.ReleaseStatus.Draft) {
        throw new BadRequestException(
            "Only releases in DRAFT status can be deleted. Current status: " + currentStatus);
      }

      // Desassociate released features
      List<ReleasedFeaturesEntity> releasedFeatures =
          releasedFeaturesRepository.findByRelease_ReleaseId(id);
      if (releasedFeatures != null && !releasedFeatures.isEmpty()) {
        releasedFeatures.forEach(rf -> rf.setRelease(null));
        releasedFeaturesRepository.saveAll(releasedFeatures);
      }

      // Desassociate test cases
      List<TestCasesEntity> testCases = testCasesRepository.findByRelease_ReleaseId(id);
      if (testCases != null && !testCases.isEmpty()) {
        testCases.forEach(tc -> tc.setRelease(null));
        testCasesRepository.saveAll(testCases);
      }

      // Mark release as inactive (soft delete)
      ReleaseEntity releaseEntity = releaseRepository.getReferenceById(id);
      releaseEntity.setReleaseIsActive(false);
      releaseRepository.save(releaseEntity);

    } catch (ItemNotFoundException e) {
      throw new ResourceNotFoundException("Release with id {" + id + "} not found.");
    }
  }
}
