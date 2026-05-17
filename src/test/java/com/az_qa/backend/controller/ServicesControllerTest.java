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

import com.az_qa.backend.service.ServicesService;
import com.az_qa.backend.vo.ServicesVO;
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
public class ServicesControllerTest {

  @Mock private ServicesService servicesService;

  @InjectMocks private ServiceController servicesController;

  private ServicesVO serviceStub;

  @BeforeEach
  void setUp() {
    serviceStub = new ServicesVO();
    serviceStub.setId(1L);
    serviceStub.setName("Service QA Test unitario");
    serviceStub.setDescription("Test unitario para controller creation");
  }

  @Test
  @DisplayName("createService: Debe retornar 201 Created cuando el servicio se crea con éxito")
  public void createService_Success() {
    when(servicesService.createService(any(ServicesVO.class))).thenReturn(serviceStub);

    ResponseEntity<ServicesVO> response = servicesController.createService(new ServicesVO());

    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals("Service QA Test unitario", response.getBody().getName());
  }

  @Test
  @DisplayName("createService: Debe retornar 400 Bad Request cuando el servicio devuelve null")
  public void createService_ReturnsBadRequest() {
    when(servicesService.createService(any(ServicesVO.class))).thenReturn(null);

    ResponseEntity<ServicesVO> response = servicesController.createService(new ServicesVO());

    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
}
