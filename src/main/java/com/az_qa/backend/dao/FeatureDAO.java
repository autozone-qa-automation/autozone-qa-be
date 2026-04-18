/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.dao;

import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.mapper.FeatureMapper;
import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.vo.FeatureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FeatureDAO {

  @Autowired private FeaturesRepository featuresRepository;

  public FeatureVO getFeatureById(Long id) {
    throw new UnsupportedOperationException("getFeatureById not implemented yet");
  }

  public java.util.List<FeatureVO> getAllFeatures() {
    throw new UnsupportedOperationException("getAllFeatures not implemented yet");
  }

  public FeatureVO createFeature(FeatureVO featureVO) {
    FeatureEntity featureEntity = FeatureMapper.toEntity(featureVO);
    FeatureEntity savedEntity = featuresRepository.save(featureEntity);
    return FeatureMapper.toVO(savedEntity);
  }
}
