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
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FeatureDAO {

  @Autowired private FeaturesRepository featuresRepository;
  @Autowired private ServicesRepository servicesRepository;

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
   * Finds the features that contains the service id received.
   * @param id Service id.
   * @return Features.
   */
  public List<FeatureVO> getFeaturesByServiceId(Long id) {
    List<FeatureVO> featureVO =
        featuresRepository.findByServiceId(id).stream()
            .map(FeatureMapper::toVO)
            .collect(Collectors.toList());

    if (featureVO.isEmpty()) {
      throw new ItemNotFoundException("No feature found for service identifier {" + id + "}.");
    }
    return featureVO;
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
    
    if (featureVO.getIdService() != null) {
      ServicesEntity service = servicesRepository.findById(featureVO.getIdService())
          .orElseThrow(() -> new ItemNotFoundException("Service with id {" + featureVO.getIdService() + "} not found."));
      featureEntity.setService(service);
    }
    
    FeatureEntity savedEntity = featuresRepository.save(featureEntity);
    return FeatureMapper.toVO(savedEntity);
  }
}
