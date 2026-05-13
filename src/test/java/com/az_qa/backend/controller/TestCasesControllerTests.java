/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.az_qa.backend.enumeration.TestCaseType;
import com.az_qa.backend.exception.ResourceNotFoundException;
import com.az_qa.backend.service.TestCasesService;
import com.az_qa.backend.vo.TestCaseVO;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TestCasesControllerTests {

  @Mock private TestCasesService testCasesService;

  @InjectMocks private TestCasesController testCasesController;

  private TestCaseVO testCaseStub;

  @BeforeEach
  void setUp() {
    testCaseStub = new TestCaseVO();
    testCaseStub.setId(1L);
    testCaseStub.setTitle("Test Case Demo");
    testCaseStub.setRelatedFeature(1L);
    testCaseStub.setType(TestCaseType.REGRESSION);
    testCaseStub.setSteps("Step 1");
    testCaseStub.setExpectedOutput("Expected result");
  }

  @Test
  @DisplayName("create: Debe retornar 201 Created cuando el servicio tiene éxito")
  public void create_Success() {

    when(testCasesService.createTestCase(any(TestCaseVO.class))).thenReturn(testCaseStub);

    ResponseEntity<TestCaseVO> response = testCasesController.create(new TestCaseVO());

    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals("Test Case Demo", response.getBody().getTitle());
  }

  @Nested
  @DisplayName("getTestCaseById")
  class GetTestCaseById {

    @Test
    @DisplayName("Debe retornar 200 OK con el test case cuando existe")
    public void getTestCaseById_Success() {
      when(testCasesService.getTestCaseById(1L)).thenReturn(testCaseStub);

      ResponseEntity<TestCaseVO> response = testCasesController.getTestCaseById(1L);

      assertNotNull(response);
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals(testCaseStub.getId(), response.getBody().getId());
      assertEquals(testCaseStub.getTitle(), response.getBody().getTitle());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando el test case no existe")
    public void getTestCaseById_NotFound() {
      when(testCasesService.getTestCaseById(99L))
          .thenThrow(new ResourceNotFoundException("Test case not found with id: 99"));

      assertThrows(ResourceNotFoundException.class, () -> testCasesController.getTestCaseById(99L));
    }
  }

  @Nested
  @DisplayName("getAll")
  class GetAll {

    @Test
    @DisplayName("Debe retornar 200 OK con la lista de test cases")
    public void getAll_ReturnsList() {
      List<TestCaseVO> list = List.of(testCaseStub);
      when(testCasesService.getAllTestCases()).thenReturn(list);

      ResponseEntity<List<TestCaseVO>> response = testCasesController.getAll();

      assertNotNull(response);
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals(1, response.getBody().size());
      assertEquals(testCaseStub.getTitle(), response.getBody().get(0).getTitle());
    }

    @Test
    @DisplayName("Debe retornar 200 OK con lista vacía cuando no hay test cases")
    public void getAll_ReturnsEmpty() {
      when(testCasesService.getAllTestCases()).thenReturn(Collections.emptyList());

      ResponseEntity<List<TestCaseVO>> response = testCasesController.getAll();

      assertNotNull(response);
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertNotNull(response.getBody());
      assertEquals(0, response.getBody().size());
    }
  }

  @Nested
  @DisplayName("getByFeature")
  class GetByFeature {

    @Test
    @DisplayName("Debe retornar 200 OK con los test cases de un feature")
    public void getByFeature_ReturnsList() {
      List<TestCaseVO> list = List.of(testCaseStub);
      when(testCasesService.getByFeature(1L)).thenReturn(list);

      ResponseEntity<List<TestCaseVO>> response = testCasesController.getByFeature(1L);

      assertNotNull(response);
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals(1, response.getBody().size());
      assertEquals(testCaseStub.getRelatedFeature(), response.getBody().get(0).getRelatedFeature());
    }

    @Test
    @DisplayName("Debe retornar 200 OK con lista vacía cuando el feature no tiene test cases")
    public void getByFeature_ReturnsEmpty() {
      when(testCasesService.getByFeature(99L)).thenReturn(Collections.emptyList());

      ResponseEntity<List<TestCaseVO>> response = testCasesController.getByFeature(99L);

      assertNotNull(response);
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertNotNull(response.getBody());
      assertEquals(0, response.getBody().size());
    }
  }
}