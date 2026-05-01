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
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.exception.ResourceNotFoundException;
import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.repository.ReleaseRepository;
import com.az_qa.backend.repository.ReleasedFeaturesRepository;
import com.az_qa.backend.vo.ReleaseVO;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReleaseService {
  private final ReleaseDAO releaseDAO;
  private final ReleaseRepository releaseRepository;
  private final FeaturesRepository featuresRepository;
  private final ReleasedFeaturesRepository releasedFeaturesRepository;

  public ReleaseService(
      ReleaseDAO releaseDAO,
      ReleaseRepository releaseRepository,
      FeaturesRepository featuresRepository,
      ReleasedFeaturesRepository releasedFeaturesRepository) {
    this.releaseDAO = releaseDAO;
    this.releaseRepository = releaseRepository;
    this.featuresRepository = featuresRepository;
    this.releasedFeaturesRepository = releasedFeaturesRepository;
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
}
