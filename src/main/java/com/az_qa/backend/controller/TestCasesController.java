/*

Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.controller;

import com.az_qa.backend.service.TestCasesService;
import com.az_qa.backend.vo.TestCaseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
class TestCasesController {

  @Autowired TestCasesService testCasesService;

  @PostMapping
  ResponseEntity<TestCaseVO> create(@Valid @RequestBody TestCaseVO testCaseVO) {
    TestCaseVO createdTestCase = testCasesService.createTestCase(testCaseVO);
    if (createdTestCase == null) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(createdTestCase);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update an existing test case by its ID.",
      description =
          "Updates the details of an existing test case identified by its ID. The request body"
              + " should contain the updated test case information. Returns the updated test case"
              + " if successful.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Test case updated successfully."),
        @ApiResponse(responseCode = "400", description = "Invalid payload or id mismatch."),
        @ApiResponse(responseCode = "404", description = "Test case not found."),
        @ApiResponse(responseCode = "409", description = "Test case duplicated by title.")
      })
  ResponseEntity<TestCaseVO> update(
      @PathVariable Long id, @Valid @RequestBody TestCaseVO testCaseVO) {
    TestCaseVO updatedTestCase = testCasesService.update(id, testCaseVO);
    if (updatedTestCase == null) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(updatedTestCase);
  }

  @PutMapping("/{id}/deactivate")
  ResponseEntity<Void> deactivate(@PathVariable @Positive Long id) {
    testCasesService.deactivate(id);
    return ResponseEntity.noContent().build();
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
  ResponseEntity<List<TestCaseVO>> getByFeature(@PathVariable Long featureId) {
    return ResponseEntity.ok(testCasesService.getByFeature(featureId));
  }
}
