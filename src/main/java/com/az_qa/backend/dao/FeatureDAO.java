/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.dao;

import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.mapper.FeatureMapper;
import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.vo.FeatureVO;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FeatureDAO {

  @Autowired private FeaturesRepository featuresRepository;

  /**
   * Find features by id.
   * @param id feature identifier.
   * @return optional Feature representation.
   */
  public FeatureVO getFeatureById(Long id) {
    Optional<FeatureVO> featureVO = featuresRepository.findById(id).map(FeatureMapper::toVO);

    if (featureVO.isEmpty()) {
      throw new ItemNotFoundException("Feature with id {" + id + "} not found.");
    }
    return featureVO.get();
  }

  /**
   * Finds the feature that contains the service id received.
   * @param id Service id.
   * @return Feature.
   */
  public FeatureVO getFeatureByServiceId(Long id) {
    Optional<FeatureVO> featureVO =
        featuresRepository.findByIdServices(id).map(FeatureMapper::toVO);

    if (featureVO.isEmpty()) {
      throw new ItemNotFoundException("No feature found for service identifier {" + id + "}.");
    }
    return featureVO.get();
  }

  /**
   * Retrieves all existing features.
   * @return list of all the features found.
   */
  public java.util.List<FeatureVO> getAllFeatures() {
    return featuresRepository.findAll().stream().map(FeatureMapper::toVO).toList();
  }

  public FeatureVO createFeature(FeatureVO featureVO) {
    FeatureEntity featureEntity = FeatureMapper.toEntity(featureVO);
    FeatureEntity savedEntity = featuresRepository.save(featureEntity);
    return FeatureMapper.toVO(savedEntity);
  }
}
