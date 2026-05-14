/*

Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import com.az_qa.backend.dao.TestCasesDAO;
import com.az_qa.backend.exception.*;
import com.az_qa.backend.vo.TestCaseVO;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestCasesService {

  @Autowired private TestCasesDAO dao;

  public TestCaseVO createTestCase(TestCaseVO testcaseVO) {
    if (testcaseVO.getTitle() == null) {
      throw new MissingRequiredFieldException("Test case title is required.");
    }
    if (testcaseVO.getSteps() == null) {
      throw new MissingRequiredFieldException("Test case steps are required.");
    }
    if (testcaseVO.getExpectedOutput() == null) {
      throw new MissingRequiredFieldException("Test case expected output is required.");
    }
    if (testcaseVO.getFeatureId() == null) {
      throw new MissingRequiredFieldException("Test case feature is required.");
    }
    if (testcaseVO.getType() == null) {
      throw new MissingRequiredFieldException("Test case type is required.");
    }
    try {
      dao.findByTitle(testcaseVO.getTitle());
      throw new DuplicatedItemException(
          "Test case with title {" + testcaseVO.getTitle() + "} already exists.");
    } catch (ItemNotFoundException e) {

    }
    return dao.create(testcaseVO);
  }

  public TestCaseVO update(final Long id, final TestCaseVO testcaseVO) {
    if (testcaseVO.getId() != null && !testcaseVO.getId().equals(id)) {
      throw new ItemIdMismatchException(
          "Path id {" + id + "} does not match test case id {" + testcaseVO.getId() + "}.");
    }
    if (testcaseVO.getTitle() == null) {
      throw new MissingRequiredFieldException("Test case title is required.");
    }
    if (testcaseVO.getSteps() == null) {
      throw new MissingRequiredFieldException("Test case steps are required.");
    }
    if (testcaseVO.getExpectedOutput() == null) {
      throw new MissingRequiredFieldException("Test case expected output is required.");
    }
    if (testcaseVO.getFeatureId() == null) {
      throw new MissingRequiredFieldException("Test case feature is required.");
    }
    if (testcaseVO.getType() == null) {
      throw new MissingRequiredFieldException("Test case type is required.");
    }

    dao.findById(id);

    try {
      TestCaseVO foundTestCase = dao.findByTitle(testcaseVO.getTitle());
      if (foundTestCase != null && !foundTestCase.getId().equals(id)) {
        throw new DuplicatedItemException(
            "Test case with title {" + testcaseVO.getTitle() + "} already exists.");
      }
    } catch (final ItemNotFoundException infe) {
      // No test case with the same title exists, so we can proceed with the update
    }

    testcaseVO.setId(id);
    return dao.update(testcaseVO);
  }

  public void deactivate(Long id) {
    dao.deactivate(id);
  }

  public TestCaseVO getTestCaseById(@Positive long id) {
    return dao.findById(id)
        .orElseThrow(() -> new ItemNotFoundException("Test case not found with id: " + id));
  }

  public List<TestCaseVO> getAllTestCases() {
    return dao.findAll();
  }

  public List<TestCaseVO> getByFeature(Long featureId) {
    return dao.findByFeature(featureId);
  }
}
