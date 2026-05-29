/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.az_qa.backend.entity.ServicesEntity;
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.repository.ServicesRepository;
import com.az_qa.backend.vo.ServicesVO;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServicesDAOTests {

  @Mock private ServicesRepository servicesRepository;

  @InjectMocks private ServicesDAO servicesDAO;

  private ServicesEntity servicesEntity;
  private ServicesVO servicesVO;

  @BeforeEach
  void setUp() {
    servicesEntity = new ServicesEntity();
    servicesEntity.setId(1L);
    servicesEntity.setName("Service DAO Test");
    servicesEntity.setDescription("Description for DAO test");

    servicesVO = new ServicesVO();
    servicesVO.setId(1L);
    servicesVO.setName("Service DAO Test");
    servicesVO.setDescription("Description for DAO test");
  }

  @Test
  @DisplayName("POST: createService: Debe mapear, guardar y retornar el VO")
  void createService_Success() {
    when(servicesRepository.save(any(ServicesEntity.class))).thenReturn(servicesEntity);

    ServicesVO result = servicesDAO.createService(servicesVO);

    assertNotNull(result);
    assertEquals(servicesVO.getName(), result.getName());
    assertEquals(servicesVO.getDescription(), result.getDescription());
    assertEquals(1L, result.getId());
  }

  @Test
  @DisplayName("PUT: updateService: Debe actualizar correctamente el servicio")
  void updateService_Success() {
    ServicesVO updateVO = new ServicesVO();
    updateVO.setName("Servicio Actualizado");
    updateVO.setDescription("Nueva descripción");
    updateVO.setUrls(new ArrayList<>());
    servicesEntity.setUrls(new ArrayList<>());

    when(servicesRepository.findByIdWithUrls(1L)).thenReturn(Optional.of(servicesEntity));
    when(servicesRepository.save(any(ServicesEntity.class))).thenReturn(servicesEntity);

    ServicesVO result = servicesDAO.updateService(1L, updateVO);

    assertNotNull(result);
    assertEquals("Servicio Actualizado", servicesEntity.getName());
  }

  @Test
  @DisplayName("PUT: updateService: Debe lanzar excepción cuando el servicio no existe")
  void updateService_ThrowsException_WhenServiceDoesNotExist() {
    ServicesVO updateVO = new ServicesVO();
    updateVO.setName("Servicio Actualizado");
    updateVO.setUrls(new ArrayList<>());

    when(servicesRepository.findByIdWithUrls(1L)).thenReturn(Optional.empty());

    assertThrows(ItemNotFoundException.class, () -> servicesDAO.updateService(1L, updateVO));
  }
}
