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

@Service
public class FeaturesService {

  /**
   * DAO dependency used to access feature data.
   */
  @Autowired private FeatureDAO featureDAO;

  /**
   * Retrieves a feature by its ID.
   *
   * @param id the feature's ID
   * @return the matching feature response
   */
  public FeatureVO getFeatureById(Long id) {
    return featureDAO.getFeatureById(id);
  }

  /**
   * Retrieves all registered features.
   *
   * @return a list of all features
   */
  public List<FeatureVO> getAllFeatures() {
    return featureDAO.getAllFeatures();
  }

  /**
   * Creates a new feature.
   *
   * @param request the feature payload
   * @return the created feature response
   */
  public FeatureVO create(FeatureVO request) {
    return null;
  }
}
