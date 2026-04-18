package com.az_qa.backend.service;

import com.az_qa.backend.enumeration.TestCaseType;
import com.az_qa.backend.vo.TestCaseVO;
import jakarta.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

@Service
public class TestCasesService {

  private final List<TestCaseVO> testCases = new ArrayList<>();

  public TestCasesService() {
    testCases.add(
        new TestCaseVO(
            1L,
            "Login Test",
            101L,
            "Test user login functionality",
            TestCaseType.REGRESSION,
            "User is on login page",
            "User is logged in",
            "Username: testuser, Password: password123",
            "1. Enter username. 2. Enter password. 3. Click login."));
    testCases.add(
        new TestCaseVO(
            2L,
            "Search Product Test",
            102L,
            "Test product search feature",
            TestCaseType.REGRESSION,
            "User is on home page",
            "Search results displayed",
            "Search query: 'oil filter'",
            "1. Enter search query. 2. Click search button."));
    testCases.add(
        new TestCaseVO(
            3L,
            "Checkout Test",
            101L,
            "Test checkout process",
            TestCaseType.REGRESSION,
            "Items in cart",
            "Order placed successfully",
            "Payment info: valid card",
            "1. Go to cart. 2. Proceed to checkout. 3. Enter payment. 4. Confirm order."));
  }

  public TestCaseVO getTestCaseById(@Positive long id) {
    return testCases.stream()
        .filter(tc -> tc.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Test case not found with id: " + id));
  }

  public List<TestCaseVO> getAllTestCases() {
    return new ArrayList<>(testCases);
  }

  public List<TestCaseVO> getByFeatureId(Long featureId) {
    return testCases.stream()
        .filter(tc -> tc.getRelatedFeature().equals(featureId))
        .collect(Collectors.toList());
  }
}
