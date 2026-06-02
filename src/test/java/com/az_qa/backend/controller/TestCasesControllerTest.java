<<<<<<< HEAD
/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.az_qa.backend.service.TestCasesService;
import com.az_qa.backend.vo.TestCaseVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TestCasesControllerTest {

  @Mock private TestCasesService testCasesService;

  @InjectMocks private TestCasesController testCasesController;

  private TestCaseVO testCaseStub;

  @BeforeEach
  void setUp() {
    testCaseStub = new TestCaseVO();
    testCaseStub.setId(1L);
    testCaseStub.setTitle("Test Unitario");
  }

  @Test
  @DisplayName("update: Debe retornar 200 ok cuando el test case es actualizado exitosamente")
  public void update_Success() {

    when(testCasesService.updateTestCase(any(Long.class), any(TestCaseVO.class)))
        .thenReturn(testCaseStub);

    ResponseEntity<TestCaseVO> response = testCasesController.update(1L, new TestCaseVO());

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Test Unitario", response.getBody().getTitle());
  }

  @Test
  @DisplayName(
      "update: Debe retornar 400 Invalid Payload or id mismatch cuando el servicio devuelve null")
  public void update_ReturnsBadRequest() {

    when(testCasesService.updateTestCase(any(Long.class), any(TestCaseVO.class))).thenReturn(null);

    ResponseEntity<TestCaseVO> response = testCasesController.update(1L, new TestCaseVO());

    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
}
=======
/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.az_qa.backend.service.TestCasesService;
import com.az_qa.backend.vo.TestCaseVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TestCasesControllerTest {

  @Mock private TestCasesService testCasesService;

  @InjectMocks private TestCasesController testCasesController;

  private TestCaseVO testCaseStub;

  @BeforeEach
  void setUp() {
    testCaseStub = new TestCaseVO();
    testCaseStub.setId(1L);
    testCaseStub.setTitle("Test Unitario");
  }

  @Test
  @DisplayName("update: Debe retornar 200 ok cuando el test case es actualizado exitosamente")
  public void update_Success() {

    when(testCasesService.updateTestCase(any(Long.class), any(TestCaseVO.class)))
        .thenReturn(testCaseStub);

    ResponseEntity<TestCaseVO> response = testCasesController.update(1L, new TestCaseVO());

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Test Unitario", response.getBody().getTitle());
  }

  @Test
  @DisplayName(
      "update: Debe retornar 400 Invalid Payload or id mismatch cuando el servicio devuelve null")
  public void update_ReturnsBadRequest() {

    when(testCasesService.updateTestCase(any(Long.class), any(TestCaseVO.class))).thenReturn(null);

    ResponseEntity<TestCaseVO> response = testCasesController.update(1L, new TestCaseVO());

    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  @DisplayName("deactivate: Debe retornar 204 No Content cuando el test case se desactiva")
  public void deactivate_Success() {
    doNothing().when(testCasesService).deactivate(1L);

    ResponseEntity<Void> response = testCasesController.deactivate(1L);

    assertNotNull(response);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(testCasesService, times(1)).deactivate(1L);
  }
}
>>>>>>> 606e4bd42340fbd4b31b54b24726042f5f51716d
