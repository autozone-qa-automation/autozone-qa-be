/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import com.az_qa.backend.dao.FeatureDAO;
import com.az_qa.backend.dto.response.FeaturesResponse;
import com.az_qa.backend.vo.FeatureVO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeaturesService {

  @Autowired private FeatureDAO featureDAO;

  /**
   * Retrieves a feature by its ID.
   *
   * @param id the feature's ID
   * @return the matching feature response
   */
  public FeaturesResponse getFeatureById(Long id) {
    return null;
  }

  /**
   * Retrieves all registered features.
   *
   * @return a list of all features
   */
  public List<FeaturesResponse> getAllFeatures() {
    return null;
  }

  @Transactional
  public FeatureVO createFeature(FeatureVO featureVO) {
    return featureDAO.createFeature(featureVO);
  }
}
