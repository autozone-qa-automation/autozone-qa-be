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

import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.entity.ReleaseEntity;
import com.az_qa.backend.entity.TestCasesEntity;
import com.az_qa.backend.enumeration.TestCaseType;
import com.az_qa.backend.vo.TestCaseVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestCasesMapperTests {

  @Test
  @DisplayName("toEntity: Should map correctly from VO to Entity with feature and release")
  void toEntity_Success_WithFeatureAndRelease() {
    TestCaseVO vo = new TestCaseVO();
    vo.setId(1L);
    vo.setTitle("Mapper Test Case");
    vo.setDescription("Mapper description");
    vo.setType(TestCaseType.REGRESSION);
    vo.setPreconditions("User is logged in");
    vo.setPostconditions("Changes are persisted");
    vo.setInputs("Valid credentials");
    vo.setSteps("1. Open screen\n2. Save");
    vo.setExpectedOutput("The test case is stored");
    vo.setFeatureId(10L);
    vo.setReleaseId(20L);

    TestCasesEntity entity = TestCasesMapper.toEntity(vo);

    assertNotNull(entity);
    assertEquals(1L, entity.getId());
    assertEquals("Mapper Test Case", entity.getTitle());
    assertEquals("Mapper description", entity.getDescription());
    assertEquals(TestCaseType.REGRESSION, entity.getType());
    assertEquals("User is logged in", entity.getPreconditions());
    assertEquals("Changes are persisted", entity.getPostconditions());
    assertEquals("Valid credentials", entity.getInputs());
    assertEquals("1. Open screen\n2. Save", entity.getSteps());
    assertEquals("The test case is stored", entity.getExpectedOutput());
    assertNotNull(entity.getFeature());
    assertEquals(10L, entity.getFeature().getId());
    assertNotNull(entity.getRelease());
    assertEquals(20L, entity.getRelease().getReleaseId());
  }

  @Test
  @DisplayName("toEntity: Should leave feature and release null when their ids are missing")
  void toEntity_Success_WithoutFeatureAndRelease() {
    TestCaseVO vo = new TestCaseVO();
    vo.setId(2L);
    vo.setTitle("Mapper Without Relations");
    vo.setType(TestCaseType.ON_DEMAND);
    vo.setSteps("1. Execute");
    vo.setExpectedOutput("Mapped without relations");

    TestCasesEntity entity = TestCasesMapper.toEntity(vo);

    assertNotNull(entity);
    assertEquals(2L, entity.getId());
    assertEquals("Mapper Without Relations", entity.getTitle());
    assertNull(entity.getFeature());
    assertNull(entity.getRelease());
  }

  @Test
  @DisplayName("toVO: Should map correctly from Entity to VO when release exists")
  void toVO_Success_WithRelease() {
    FeatureEntity feature = new FeatureEntity();
    feature.setId(10L);
    feature.setName("Checkout Feature");
    feature.setDescription("Handles checkout flow");

    ReleaseEntity release = new ReleaseEntity();
    release.setReleaseId(20L);
    release.setReleaseName("Release 1");

    TestCasesEntity entity = new TestCasesEntity();
    entity.setId(1L);
    entity.setCode("TC-001");
    entity.setTitle("Mapper Test Case");
    entity.setDescription("Mapper description");
    entity.setType(TestCaseType.ON_DEMAND);
    entity.setPreconditions("User is logged in");
    entity.setPostconditions("Cart is updated");
    entity.setInputs("Valid cart");
    entity.setSteps("1. Open cart\n2. Confirm");
    entity.setExpectedOutput("Order is created");
    entity.setIsActive(true);
    entity.setFeature(feature);
    entity.setRelease(release);

    TestCaseVO vo = TestCasesMapper.toVO(entity);

    assertNotNull(vo);
    assertEquals(1L, vo.getId());
    assertEquals("TC-001", vo.getCode());
    assertEquals("Mapper Test Case", vo.getTitle());
    assertEquals(10L, vo.getFeatureId());
    assertEquals(20L, vo.getReleaseId());
    assertEquals("Mapper description", vo.getDescription());
    assertEquals(TestCaseType.ON_DEMAND, vo.getType());
    assertEquals("User is logged in", vo.getPreconditions());
    assertEquals("Cart is updated", vo.getPostconditions());
    assertEquals("Valid cart", vo.getInputs());
    assertEquals("1. Open cart\n2. Confirm", vo.getSteps());
    assertEquals("Order is created", vo.getExpectedOutput());
    assertEquals(true, vo.getActive());
    assertNotNull(vo.getFeature());
    assertNotNull(vo.getRelease());
  }

  @Test
  @DisplayName("toVO: Should map correctly from Entity to VO when release is null")
  void toVO_Success_WithoutRelease() {
    FeatureEntity feature = new FeatureEntity();
    feature.setId(11L);
    feature.setName("Search Feature");
    feature.setDescription("Handles search flow");

    TestCasesEntity entity = new TestCasesEntity();
    entity.setId(2L);
    entity.setTitle("Mapper Without Release");
    entity.setType(TestCaseType.REGRESSION);
    entity.setSteps("1. Search");
    entity.setExpectedOutput("Results are shown");
    entity.setIsActive(false);
    entity.setFeature(feature);
    entity.setRelease(null);

    TestCaseVO vo = TestCasesMapper.toVO(entity);

    assertNotNull(vo);
    assertEquals(11L, vo.getFeatureId());
    assertNull(vo.getReleaseId());
    assertNull(vo.getRelease());
    assertEquals(false, vo.getActive());
  }

  @Test
  @DisplayName("toVO: Should return null when the entity is null")
  void toVO_NullInput() {
    assertNull(TestCasesMapper.toVO(null));
  }
}
