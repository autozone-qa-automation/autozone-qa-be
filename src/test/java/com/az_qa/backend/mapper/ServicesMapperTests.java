/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.az_qa.backend.entity.ServicesEntity;
import com.az_qa.backend.entity.UrlEntity;
import com.az_qa.backend.vo.ServicesVO;
import com.az_qa.backend.vo.UrlVO;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ServicesMapperTests {

  @Test
  @DisplayName("serviceToEntity: Debe mapear correctamente de VO a Entity con URLs")
  void serviceToEntity_Success() {
    UrlVO urlVO = new UrlVO();
    urlVO.setNombre("Documentation");
    urlVO.setUrl("http://docs.com");

    ServicesVO vo = new ServicesVO();
    vo.setName("Service Test");
    vo.setDescription("Description Test");
    vo.setUrls(List.of(urlVO));

    ServicesEntity entity = ServicesMapper.serviceToEntity(vo);

    assertNotNull(entity);
    assertEquals(vo.getName(), entity.getName());
    assertEquals(vo.getDescription(), entity.getDescription());

    assertNotNull(entity.getUrls());
    assertEquals(1, entity.getUrls().size());
    assertEquals("Documentation", entity.getUrls().get(0).getNombre());

    assertEquals(entity, entity.getUrls().get(0).getServicio());
  }

  @Test
  @DisplayName("serviceToEntity: Debe retornar null cuando el VO es null")
  void serviceToEntity_NullInput() {
    assertNull(ServicesMapper.serviceToEntity(null));
  }

  @Test
  @DisplayName("urlToEntity: Debe mapear correctamente una URL individual")
  void urlToEntity_Success() {
    UrlVO vo = new UrlVO();
    vo.setNombre("GitHub");
    vo.setUrl("http://github.com");

    UrlEntity entity = ServicesMapper.urlToEntity(vo);

    assertNotNull(entity);
    assertEquals("GitHub", entity.getNombre());
    assertEquals("http://github.com", entity.getUrl());
  }

  @Test
  @DisplayName("urlToEntity: Debe retornar null cuando el UrlVO es null")
  void urlToEntity_NullInput() {
    assertNull(ServicesMapper.urlToEntity(null));
  }
}
