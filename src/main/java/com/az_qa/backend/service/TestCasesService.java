/*

Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import com.az_qa.backend.dao.TestCasesDAO;
import com.az_qa.backend.vo.TestCaseVO;

import com.az_qa.backend.exception.ResourceNotFoundException;

import jakarta.validation.constraints.Positive;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TestCasesService {

  private final TestCasesDAO dao;

  public TestCasesService(TestCasesDAO dao) {
    this.dao = dao;
  }

  public TestCaseVO createTestCase(TestCaseVO vo) {
    return dao.create(vo);
  }

  public void deactivate(Long id) {
    dao.deactivate(id);
  }

  public TestCaseVO getTestCaseById(@Positive long id) {
    return dao.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Test case not found with id: " + id));
  }

  public List<TestCaseVO> getAllTestCases() {
   return dao.findAll();
   }
  public List<TestCaseVO> getByFeature(Long featureId) {
    return dao.findByFeature(featureId);
  }
}
