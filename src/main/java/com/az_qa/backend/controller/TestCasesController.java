/*

Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.controller;

import com.az_qa.backend.service.TestCasesService;
import com.az_qa.backend.vo.TestCaseVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test-cases")
@Validated
public class TestCasesController {

  private final TestCasesService testCasesService;

  public TestCasesController(TestCasesService testCasesService) {
    this.testCasesService = testCasesService;
  }

  @PostMapping
  public ResponseEntity<TestCaseVO> create(@Valid @RequestBody TestCaseVO testCaseVO) {
    TestCaseVO createdTestCase = testCasesService.createTestCase(testCaseVO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdTestCase);
  }

  @PutMapping("/{id}/deactivate")
  public ResponseEntity<Void> deactivate(@PathVariable @Positive Long id) {
    testCasesService.deactivate(id);
    return ResponseEntity.noContent().build();
  }

  
  @GetMapping("/{id}")
   public ResponseEntity<TestCaseVO> getTestCaseById(@PathVariable @Positive
   long id) {
    return ResponseEntity.ok(testCasesService.getTestCaseById(id));
   }
   
  @GetMapping
   public ResponseEntity<List<TestCaseVO>> getAll() {
    return ResponseEntity.ok(testCasesService.getAllTestCases());
   }
   
  @GetMapping("/feature/{featureId}")
  public ResponseEntity<List<TestCaseVO>> getByFeature(@PathVariable Long featureId) {
    return ResponseEntity.ok(testCasesService.getByFeature(featureId));
  }
}
