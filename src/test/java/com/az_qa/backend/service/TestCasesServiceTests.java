/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.az_qa.backend.dao.TestCasesDAO;
import com.az_qa.backend.enumeration.TestCaseType;
import com.az_qa.backend.exception.DuplicatedItemException;
import com.az_qa.backend.exception.ItemIdMismatchException;
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.exception.MissingRequiredFieldException;
import com.az_qa.backend.vo.TestCaseVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestCasesServiceTests {

  @Mock private TestCasesDAO testCasesDAO;

  @InjectMocks private TestCasesService testCasesService;

  private TestCaseVO testCaseInput;

  @BeforeEach
  void setUp() {
    testCaseInput = new TestCaseVO();
    testCaseInput.setTitle("Update Test Case");
    testCaseInput.setSteps("1. Open page\n2. Submit form");
    testCaseInput.setExpectedOutput("The test case is updated");
    testCaseInput.setFeatureId(10L);
    testCaseInput.setType(TestCaseType.ON_DEMAND);
  }

  @Test
  @DisplayName("updateTestCase: Should return the updated test case when the title is unique")
  void updateTestCase_ReturnsUpdatedTestCase_WhenTitleIsUnique() {
    TestCaseVO updatedStub = new TestCaseVO();
    updatedStub.setId(1L);
    updatedStub.setTitle("Update Test Case");

    when(testCasesDAO.findByTitle("Update Test Case"))
        .thenThrow(new ItemNotFoundException("Test case not found with title: Update Test Case"));
    when(testCasesDAO.update(any(TestCaseVO.class))).thenReturn(updatedStub);

    TestCaseVO result = testCasesService.updateTestCase(1L, testCaseInput);

    assertNotNull(result);
    assertSame(updatedStub, result);
    assertEquals(1L, testCaseInput.getId());
    verify(testCasesDAO).findByTitle("Update Test Case");
    verify(testCasesDAO).update(testCaseInput);
  }

  @Test
  @DisplayName(
      "updateTestCase: Should return the updated test case when the title belongs to the same id")
  void updateTestCase_ReturnsUpdatedTestCase_WhenTitleBelongsToSameId() {
    TestCaseVO foundTestCase = new TestCaseVO();
    foundTestCase.setId(1L);
    foundTestCase.setTitle("Update Test Case");

    TestCaseVO updatedStub = new TestCaseVO();
    updatedStub.setId(1L);
    updatedStub.setTitle("Update Test Case");

    when(testCasesDAO.findByTitle("Update Test Case")).thenReturn(foundTestCase);
    when(testCasesDAO.update(any(TestCaseVO.class))).thenReturn(updatedStub);

    TestCaseVO result = testCasesService.updateTestCase(1L, testCaseInput);

    assertNotNull(result);
    assertSame(updatedStub, result);
    assertEquals(1L, testCaseInput.getId());
    verify(testCasesDAO).findByTitle("Update Test Case");
    verify(testCasesDAO).update(testCaseInput);
  }

  @Test
  @DisplayName(
      "updateTestCase: Should throw ItemIdMismatchException when the body id does not match")
  void updateTestCase_ThrowsException_WhenBodyIdDoesNotMatch() {
    testCaseInput.setId(2L);

    ItemIdMismatchException exception =
        assertThrows(
            ItemIdMismatchException.class,
            () -> testCasesService.updateTestCase(1L, testCaseInput));

    assertEquals("Path id {1} does not match test case id {2}.", exception.getMessage());
    verify(testCasesDAO, never()).findByTitle(any(String.class));
    verify(testCasesDAO, never()).update(any(TestCaseVO.class));
  }

  @Test
  @DisplayName("updateTestCase: Should throw MissingRequiredFieldException when title is missing")
  void updateTestCase_ThrowsException_WhenTitleIsMissing() {
    testCaseInput.setTitle(null);

    MissingRequiredFieldException exception =
        assertThrows(
            MissingRequiredFieldException.class,
            () -> testCasesService.updateTestCase(1L, testCaseInput));

    assertEquals("Test case title is required.", exception.getMessage());
    verify(testCasesDAO, never()).findByTitle(any(String.class));
    verify(testCasesDAO, never()).update(any(TestCaseVO.class));
  }

  @Test
  @DisplayName("updateTestCase: Should throw MissingRequiredFieldException when steps are missing")
  void updateTestCase_ThrowsException_WhenStepsAreMissing() {
    testCaseInput.setSteps(null);

    MissingRequiredFieldException exception =
        assertThrows(
            MissingRequiredFieldException.class,
            () -> testCasesService.updateTestCase(1L, testCaseInput));

    assertEquals("Test case steps are required.", exception.getMessage());
    verify(testCasesDAO, never()).findByTitle(any(String.class));
    verify(testCasesDAO, never()).update(any(TestCaseVO.class));
  }

  @Test
  @DisplayName(
      "updateTestCase: Should throw MissingRequiredFieldException when expected output is missing")
  void updateTestCase_ThrowsException_WhenExpectedOutputIsMissing() {
    testCaseInput.setExpectedOutput(null);

    MissingRequiredFieldException exception =
        assertThrows(
            MissingRequiredFieldException.class,
            () -> testCasesService.updateTestCase(1L, testCaseInput));

    assertEquals("Test case expected output is required.", exception.getMessage());
    verify(testCasesDAO, never()).findByTitle(any(String.class));
    verify(testCasesDAO, never()).update(any(TestCaseVO.class));
  }

  @Test
  @DisplayName("updateTestCase: Should throw MissingRequiredFieldException when feature is missing")
  void updateTestCase_ThrowsException_WhenFeatureIsMissing() {
    testCaseInput.setFeatureId(null);

    MissingRequiredFieldException exception =
        assertThrows(
            MissingRequiredFieldException.class,
            () -> testCasesService.updateTestCase(1L, testCaseInput));

    assertEquals("Test case feature is required.", exception.getMessage());
    verify(testCasesDAO, never()).findByTitle(any(String.class));
    verify(testCasesDAO, never()).update(any(TestCaseVO.class));
  }

  @Test
  @DisplayName("updateTestCase: Should throw MissingRequiredFieldException when type is missing")
  void updateTestCase_ThrowsException_WhenTypeIsMissing() {
    testCaseInput.setType(null);

    MissingRequiredFieldException exception =
        assertThrows(
            MissingRequiredFieldException.class,
            () -> testCasesService.updateTestCase(1L, testCaseInput));

    assertEquals("Test case type is required.", exception.getMessage());
    verify(testCasesDAO, never()).findByTitle(any(String.class));
    verify(testCasesDAO, never()).update(any(TestCaseVO.class));
  }

  @Test
  @DisplayName(
      "updateTestCase: Should throw DuplicatedItemException when the title belongs to a different"
          + " id")
  void updateTestCase_ThrowsException_WhenTitleBelongsToDifferentId() {
    TestCaseVO foundTestCase = new TestCaseVO();
    foundTestCase.setId(2L);
    foundTestCase.setTitle("Update Test Case");

    when(testCasesDAO.findByTitle("Update Test Case")).thenReturn(foundTestCase);

    DuplicatedItemException exception =
        assertThrows(
            DuplicatedItemException.class,
            () -> testCasesService.updateTestCase(1L, testCaseInput));

    assertEquals("Test case with title {Update Test Case} already exists.", exception.getMessage());
    verify(testCasesDAO).findByTitle("Update Test Case");
    verify(testCasesDAO, never()).update(any(TestCaseVO.class));
  }
}
