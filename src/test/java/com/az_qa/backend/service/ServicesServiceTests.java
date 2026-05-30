/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.az_qa.backend.dao.ServicesDAO;
import com.az_qa.backend.exception.DuplicatedItemException;
import com.az_qa.backend.vo.ServicesVO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServicesServiceTests {

  @Mock private ServicesDAO servicesDAO;

  @InjectMocks @Spy private ServicesService servicesService;

  private ServicesVO serviceInput;

  @BeforeEach
  void setUp() {
    serviceInput = new ServicesVO();
    serviceInput.setName("  Service Test  ");
    serviceInput.setDescription("");
  }

  @Test
  @DisplayName("createService: Debe limpiar el nombre y asignar descripción por defecto")
  void createService_SanitizationSuccess() {
    when(servicesService.getAllServices()).thenReturn(Collections.emptyList());
    when(servicesDAO.createService(any(ServicesVO.class))).thenAnswer(i -> i.getArguments()[0]);

    ServicesVO result = servicesService.createService(serviceInput);

    assertNotNull(result);
    assertEquals("Service Test", result.getName());
    assertEquals("No description provided for Service Test", result.getDescription());
    verify(servicesDAO).createService(any(ServicesVO.class));
  }

  @Test
  @DisplayName("createService: Debe lanzar DuplicatedItemException si el nombre ya existe")
  void createService_ThrowsExceptionWhenDuplicated() {
    ServicesVO existing = new ServicesVO();
    existing.setName("SERVICE TEST");

    when(servicesService.getAllServices()).thenReturn(List.of(existing));

    DuplicatedItemException exception =
        assertThrows(
            DuplicatedItemException.class,
            () -> {
              servicesService.createService(serviceInput);
            });

    assertEquals("A service with the name 'Service Test' already exists.", exception.getMessage());
    verify(servicesDAO, never()).createService(any(ServicesVO.class));
  }

  @Test
  @DisplayName("PUT: updateService: Debe retornar el servicio actualizado correctamente")
  void updateService_ReturnsUpdatedService() {
    ServicesVO updateVO = new ServicesVO();
    updateVO.setName("Servicio Actualizado");
    updateVO.setDescription("Nueva descripción");
    updateVO.setUrls(new ArrayList<>());

    ServicesVO updatedStub = new ServicesVO();
    updatedStub.setId(1L);
    updatedStub.setName("Servicio Actualizado");

    when(servicesDAO.existsByNameIgnoreCaseAndIdNot("Servicio Actualizado", 1L)).thenReturn(false);
    when(servicesDAO.updateService(any(Long.class), any(ServicesVO.class))).thenReturn(updatedStub);

    ServicesVO result = servicesService.updateService(1L, updateVO);

    assertNotNull(result);
    assertEquals("Servicio Actualizado", result.getName());
  }

  @Test
  @DisplayName("PUT: updateService: Debe lanzar excepción cuando el nombre está vacío")
  void updateService_ThrowsException_WhenNameIsBlank() {
    ServicesVO updateVO = new ServicesVO();
    updateVO.setName("");
    updateVO.setUrls(new ArrayList<>());

    assertThrows(IllegalArgumentException.class, () -> servicesService.updateService(1L, updateVO));
  }

  @Test
  @DisplayName("PUT: updateService: Debe lanzar excepción cuando el nombre está duplicado")
  void updateService_ThrowsException_WhenNameIsDuplicated() {
    ServicesVO updateVO = new ServicesVO();
    updateVO.setName("Nombre Duplicado");
    updateVO.setUrls(new ArrayList<>());

    when(servicesDAO.existsByNameIgnoreCaseAndIdNot("Nombre Duplicado", 1L)).thenReturn(true);

    assertThrows(DuplicatedItemException.class, () -> servicesService.updateService(1L, updateVO));
  }
}
