/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import com.az_qa.backend.dao.FeatureDAO;
import com.az_qa.backend.vo.FeatureVO;
import com.az_qa.backend.vo.TestCaseVO;
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

  /**
   * Service dependency used to manage test cases linked to a feature.
   */
  @Autowired private TestCasesService testCasesService;

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
   * Retrieves a feature by the service id on it.
   *
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
    return featureDAO.getAllFeatures();
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

  /**
   * Updates an existing feature.
   *
   * @param id        the feature's ID
   * @param featureVO the updated feature payload
   * @return the updated feature response
   */
  @Transactional
  public FeatureVO updateFeature(Long id, FeatureVO featureVO) {
    return featureDAO.updateFeature(id, featureVO);
  }

  /**
   * Deactivates a feature and all its related test cases.
   *
   * @param id the feature's ID
   */
  @Transactional
  public void deactivateFeature(Long id) {
    featureDAO.deactivateFeature(id);
    List<TestCaseVO> relatedTestCases = testCasesService.getByFeature(id);
    for (TestCaseVO testCase : relatedTestCases) {
      testCasesService.deactivate(testCase.getId());
    }
  }
}
