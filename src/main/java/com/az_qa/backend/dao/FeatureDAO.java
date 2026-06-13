/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.dao;

import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.entity.ServicesEntity;
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.mapper.FeatureMapper;
import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.repository.ServicesRepository;
import com.az_qa.backend.vo.FeatureVO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FeatureDAO {

  @Autowired private FeaturesRepository featuresRepository;
  @Autowired private ServicesRepository servicesRepository;

  /**
   * Find features by id.
   *
   * @param id feature identifier.
   * @return optional Feature representation.
   */
  public FeatureVO getFeatureById(Long id) {
    return featuresRepository
        .findById(id)
        .filter(FeatureEntity::isActive)
        .map(FeatureMapper::toVO)
        .orElseThrow(() -> new ItemNotFoundException("Feature with id {" + id + "} not found."));
  }

  /**
   * Finds the features that contains the service id received.
   *
   * @param id Service id.
   * @return Features.
   */
  public List<FeatureVO> getFeaturesByServiceId(Long id) {
    return featuresRepository.findByServiceId(id).stream()
        .filter(FeatureEntity::isActive)
        .map(FeatureMapper::toVO)
        .collect(Collectors.toList());
  }

  /**
   * Retrieves all existing features.
   *
   * @return list of all the features found.
   */
  public java.util.List<FeatureVO> getAllFeatures() {
    return featuresRepository.findAll().stream()
        .filter(FeatureEntity::isActive)
        .map(FeatureMapper::toVO)
        .toList();
  }

  public FeatureVO createFeature(FeatureVO featureVO) {
    FeatureEntity featureEntity = FeatureMapper.toEntity(featureVO);

    if (featureVO.getIdService() != null) {
      ServicesEntity service =
          servicesRepository
              .findById(featureVO.getIdService())
              .orElseThrow(
                  () ->
                      new ItemNotFoundException(
                          "Service with id {" + featureVO.getIdService() + "} not found."));
      featureEntity.setService(service);
    }

    FeatureEntity savedEntity = featuresRepository.save(featureEntity);
    return FeatureMapper.toVO(savedEntity);
  }

  /**
   * Updates an existing feature with the provided information.
   *
   * @param id        Identifier of the feature to update.
   * @param featureVO Object containing the updated feature data.
   * @return Updated feature representation.
   * @throws ItemNotFoundException if the feature does not exist.
   * @throws RuntimeException      if the feature name is empty or already exists.
   */
  public FeatureVO updateFeature(Long id, FeatureVO featureVO) {
    FeatureEntity existingEntity =
        featuresRepository
            .findById(id)
            .orElseThrow(
                () -> new ItemNotFoundException("Feature with id {" + id + "} not found."));

    if (featureVO.getFeatureName() == null || featureVO.getFeatureName().isBlank()) {
      throw new RuntimeException("Feature name cannot be empty.");
    }

    featuresRepository
        .findByName(featureVO.getFeatureName())
        .ifPresent(
            f -> {
              if (!f.getId().equals(id)) {
                throw new RuntimeException(
                    "Feature name '" + featureVO.getFeatureName() + "' already exists.");
              }
            });

    existingEntity.setName(featureVO.getFeatureName());
    existingEntity.setDescription(featureVO.getFeatureDescription());

    FeatureEntity savedEntity = featuresRepository.save(existingEntity);
    return FeatureMapper.toVO(savedEntity);
  }

  /**
   * Deactivates a feature by setting its active status to false.
   *
   * @param id Identifier of the feature to deactivate.
   * @throws ItemNotFoundException if the feature does not exist.
   */
  public void deactivateFeature(Long id) {
    FeatureEntity existingEntity =
        featuresRepository
            .findById(id)
            .orElseThrow(
                () -> new ItemNotFoundException("Feature with id {" + id + "} not found."));

    existingEntity.setActive(false);
    featuresRepository.save(existingEntity);
  }
}
