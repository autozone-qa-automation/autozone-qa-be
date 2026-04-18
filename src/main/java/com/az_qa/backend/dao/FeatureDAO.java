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
import java.awt.*;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
   * Retrieves all existing features.
   * @return list of all the features found.
   */
  public java.util.List<FeatureVO> getAllFeatures() {
    return featuresRepository.findAll().stream().map(FeatureMapper::toVO).toList();
  }

  @Transactional
  public FeatureVO createFeature(FeatureVO featureVO) {
    FeatureEntity featureEntity = FeatureMapper.toEntity(featureVO);
    return FeatureMapper.toVO(featuresRepository.save(featureEntity));
  }
}
