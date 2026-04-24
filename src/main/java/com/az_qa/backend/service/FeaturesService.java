/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import com.az_qa.backend.dao.FeatureDAO;
import com.az_qa.backend.vo.FeatureVO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeaturesService {

  /**
   * DAO dependency used to access feature data.
   */
  @Autowired private FeatureDAO featureDAO;

  @Autowired private ServicesService servicesService;

  /**
   * Retrieves a feature by its ID.
   *
   * @param id the feature's ID
   * @return the matching feature response
   */
  public FeatureVO getFeatureById(Long id) {
    FeatureVO featureVO = featureDAO.getFeatureById(id);
    featureVO.setServiceName(servicesService.getServiceNameById(featureVO.getIdService()));
    return featureVO;
  }

  /**
   * Retrieves a feature by the service id on it.
   * @param id Service id.
   * @return Feature.
   */
  public List<FeatureVO> getFeaturesByServiceId(Long id) {
    return featureDAO.getFeaturesByServiceId(id);
  }

  /**
   * Retrieves all registered features.
   *
   * @return a list of all features
   */
  public List<FeatureVO> getAllFeatures() {
    List<FeatureVO> featureVOList = featureDAO.getAllFeatures();
    List<FeatureVO> featuresList = new java.util.ArrayList<>(List.of());
    for (FeatureVO featureVO : featureVOList) {
      featureVO.setServiceName(servicesService.getServiceNameById(featureVO.getIdService()));
      featuresList.add(featureVO);
    }
    return featuresList;
  }

  /**
   * Creates a new feature.
   *
   * @param featureVO the feature payload
   * @return the created feature response
   */
  @Transactional
  public FeatureVO createFeature(FeatureVO featureVO) {
    return featureDAO.createFeature(featureVO);
  }
}
