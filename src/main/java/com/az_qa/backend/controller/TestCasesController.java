package com.az_qa.backend.controller;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.Positive;
import com.az_qa.backend.service.TestCasesService;
import com.az_qa.backend.dto.response.TestCasesResponse;

@RestController
@RequestMapping("/api/v1/testcases")
@Validated
public class TestCasesController {

    private final TestCasesService testCasesService;

    public TestCasesController(TestCasesService testCasesService) {
        this.testCasesService = testCasesService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestCasesResponse> getTestCaseById(@PathVariable @Positive long id) {
        return ResponseEntity.ok(testCasesService.getTestCaseById(id));
    }

    @GetMapping
    public ResponseEntity<List<TestCasesResponse>> getAll() {
        return ResponseEntity.ok(testCasesService.getAllTestCases());
    }

    @GetMapping("/feature/{featureId}")
    public ResponseEntity<List<TestCasesResponse>> getByFeatureId(@PathVariable Long featureId) {
        return ResponseEntity.ok(testCasesService.getByFeatureId(featureId));
    }
}