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
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.entity.ReleaseEntity;
import com.az_qa.backend.entity.ReleasedFeaturesEntity;
import com.az_qa.backend.entity.ServicesEntity;
import com.az_qa.backend.enumeration.ReleaseStatus;
import com.az_qa.backend.vo.ReleaseVO;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReleasesMapperTests {

  @Test
  @DisplayName("toVO: Debe mapear correctamente de Entity a VO con features y servicios")
  void toVO_Success() {
    ServicesEntity service = new ServicesEntity(10L, "Inventory Service", "Inventory description");
    FeatureEntity feature = new FeatureEntity("Stock Search", "Search stock by store");
    feature.setId(20L);
    feature.setService(service);

    ReleaseEntity entity = new ReleaseEntity();
    entity.setReleaseId(1L);
    entity.setReleaseName("Release Test");
    entity.setReleaseDescription("Release Description");
    entity.setReleaseCreationDate(LocalDate.of(2026, 5, 1));
    entity.setReleaseLaunchDate(LocalDate.of(2026, 5, 15));
    entity.setReleaseVersion("1.0.0");
    entity.setReleaseTags("qa, backend, , smoke ");
    entity.setReleaseStatus(ReleaseStatus.Draft);
    entity.setReleaseIsActive(true);
    entity.setFeatures(List.of(new ReleasedFeaturesEntity(entity, feature)));

    ReleaseVO vo = ReleaseMapper.toVO(entity);

    assertNotNull(vo);
    assertEquals(entity.getReleaseId(), vo.getReleaseId());
    assertEquals(entity.getReleaseName(), vo.getReleaseName());
    assertEquals(entity.getReleaseDescription(), vo.getReleaseDescription());
    assertEquals(entity.getReleaseCreationDate(), vo.getReleaseCreationDate());
    assertEquals(entity.getReleaseLaunchDate(), vo.getReleaseLaunchDate());
    assertEquals(entity.getReleaseVersion(), vo.getReleaseVersion());
    assertEquals(List.of("qa", "backend", "smoke"), vo.getReleaseTags());
    assertEquals(entity.getReleaseStatus(), vo.getReleaseStatus());
    assertEquals(entity.getReleaseIsActive(), vo.getReleaseIsActive());

    assertEquals(List.of("Inventory Service"), vo.getReleaseServices());
    assertEquals(10L, vo.getReleaseServiceId());
    assertEquals(List.of(20L), vo.getReleaseFeatureIds());
    assertNotNull(vo.getReleaseFeatures());
    assertEquals(1, vo.getReleaseFeatures().size());
    assertEquals(20L, vo.getReleaseFeatures().get(0).getId());
    assertEquals("Stock Search", vo.getReleaseFeatures().get(0).getFeatureName());
    assertEquals("Search stock by store", vo.getReleaseFeatures().get(0).getFeatureDescription());
    assertEquals(10L, vo.getReleaseFeatures().get(0).getIdService());
    assertEquals("Inventory Service", vo.getReleaseFeatures().get(0).getServiceName());
  }

  @Test
  @DisplayName("toVO: Debe retornar listas vacias cuando tags y features son null")
  void toVO_NullCollections() {
    ReleaseEntity entity = new ReleaseEntity();
    entity.setReleaseId(2L);
    entity.setReleaseName("Empty Release");
    entity.setReleaseDescription("Release without collections");
    entity.setReleaseCreationDate(LocalDate.of(2026, 6, 1));
    entity.setReleaseLaunchDate(null);
    entity.setReleaseVersion("2.0.0");
    entity.setReleaseTags(null);
    entity.setReleaseStatus(ReleaseStatus.Progress);
    entity.setFeatures(null);
    entity.setReleaseIsActive(true);

    ReleaseVO vo = ReleaseMapper.toVO(entity);

    assertNotNull(vo);
    assertTrue(vo.getReleaseTags().isEmpty());
    assertTrue(vo.getReleaseServices().isEmpty());
    assertNull(vo.getReleaseServiceId());
    assertTrue(vo.getReleaseFeatureIds().isEmpty());
    assertTrue(vo.getReleaseFeatures().isEmpty());
    assertEquals(entity.getReleaseIsActive(), vo.getReleaseIsActive());
  }

  @Test
  @DisplayName("toVO: Debe retornar null cuando el Entity es null")
  void toVO_NullInput() {
    assertNull(ReleaseMapper.toVO(null));
  }

  @Test
  @DisplayName("toEntity: Debe mapear correctamente de VO a Entity")
  void toEntity_Success() {
    ReleaseVO vo = new ReleaseVO();
    vo.setReleaseId(3L);
    vo.setReleaseName("Release VO");
    vo.setReleaseDescription("Description VO");
    vo.setReleaseCreationDate(LocalDate.of(2026, 7, 1));
    vo.setReleaseLaunchDate(LocalDate.of(2026, 7, 20));
    vo.setReleaseVersion("3.1.0");
    vo.setReleaseTags(List.of("qa", "regression", "backend"));
    vo.setReleaseStatus(ReleaseStatus.Active);
    vo.setReleaseIsActive(false);
    vo.setReleaseServiceId(10L);
    vo.setReleaseFeatureIds(List.of(20L, 21L));

    ReleaseEntity entity = ReleaseMapper.toEntity(vo);

    assertNotNull(entity);
    assertEquals(vo.getReleaseId(), entity.getReleaseId());
    assertEquals(vo.getReleaseName(), entity.getReleaseName());
    assertEquals(vo.getReleaseDescription(), entity.getReleaseDescription());
    assertEquals(vo.getReleaseCreationDate(), entity.getReleaseCreationDate());
    assertEquals(vo.getReleaseLaunchDate(), entity.getReleaseLaunchDate());
    assertEquals(vo.getReleaseVersion(), entity.getReleaseVersion());
    assertEquals("qa,regression,backend", entity.getReleaseTags());
    assertEquals(vo.getReleaseStatus(), entity.getReleaseStatus());
    assertEquals(vo.getReleaseIsActive(), entity.getReleaseIsActive());
  }

  @Test
  @DisplayName("toEntity: Debe retornar null en releaseTags cuando la lista de tags es null")
  void toEntity_NullTags() {
    ReleaseVO vo = new ReleaseVO();
    vo.setReleaseName("Release without tags");
    vo.setReleaseDescription("Description");
    vo.setReleaseCreationDate(LocalDate.of(2026, 8, 1));
    vo.setReleaseVersion("4.0.0");
    vo.setReleaseStatus(ReleaseStatus.Draft);
    vo.setReleaseTags(null);

    ReleaseEntity entity = ReleaseMapper.toEntity(vo);

    assertNotNull(entity);
    assertNull(entity.getReleaseTags());
  }

  @Test
  @DisplayName("toEntity: Debe retornar null cuando el VO es null")
  void toEntity_NullInput() {
    assertNull(ReleaseMapper.toEntity(null));
  }
}
