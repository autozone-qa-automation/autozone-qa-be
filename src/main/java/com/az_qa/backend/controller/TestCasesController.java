package com.az_qa.backend.controller;

import com.az_qa.backend.service.TestCasesService;
import com.az_qa.backend.vo.TestCaseVO;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

@RestController
@RequestMapping("/api/v1/testcases")
@Validated
public class TestCasesController {

  private final TestCasesService testCasesService;

  public TestCasesController(TestCasesService testCasesService) {
    this.testCasesService = testCasesService;
  }

  @GetMapping("/{id}")
  ResponseEntity<TestCaseVO> getTestCaseById(@PathVariable @Positive long id) {
    return ResponseEntity.ok(testCasesService.getTestCaseById(id));
  }

  @GetMapping
  ResponseEntity<List<TestCaseVO>> getAll() {
    return ResponseEntity.ok(testCasesService.getAllTestCases());
  }

  @GetMapping("/feature/{featureId}")
  ResponseEntity<List<TestCaseVO>> getByFeatureId(@PathVariable Long featureId) {
    return ResponseEntity.ok(testCasesService.getByFeatureId(featureId));
  }
}
