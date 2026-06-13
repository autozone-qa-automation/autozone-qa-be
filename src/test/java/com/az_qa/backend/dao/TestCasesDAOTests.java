/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.entity.TestCasesEntity;
import com.az_qa.backend.enumeration.TestCaseType;
import com.az_qa.backend.mapper.TestCasesMapper;
import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.repository.TestCasesRepository;
import com.az_qa.backend.vo.TestCaseVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestCasesDAOTests {

  @Mock private TestCasesRepository testCasesRepository;

  @Mock private FeaturesRepository featuresRepository;

  @InjectMocks private TestCasesDAO testCasesDAO;

  private TestCaseVO testCaseVO;
  private TestCasesEntity testCasesEntity;

  @BeforeEach
  void setUp() {
    testCaseVO = new TestCaseVO();
    testCaseVO.setId(1L);
    testCaseVO.setTitle("DAO Test Case");
    testCaseVO.setSteps("1. Open module\n2. Save changes");
    testCaseVO.setExpectedOutput("The update is persisted");
    testCaseVO.setFeatureId(10L);
    testCaseVO.setType(TestCaseType.ON_DEMAND);

    FeatureEntity featureEntity = new FeatureEntity();
    featureEntity.setId(10L);
    featureEntity.setName("DAO Feature");

    testCasesEntity = new TestCasesEntity();
    testCasesEntity.setId(1L);
    testCasesEntity.setTitle("DAO Test Case");
    testCasesEntity.setSteps("1. Open module\n2. Save changes");
    testCasesEntity.setExpectedOutput("The update is persisted");
    testCasesEntity.setFeature(featureEntity);
    testCasesEntity.setType(TestCaseType.ON_DEMAND);
  }

  @Test
  @DisplayName("PUT: updateTestCase: Should return null when the input VO is null")
  void updateTestCase_ReturnsNull_WhenInputIsNull() {
    TestCaseVO result = testCasesDAO.update(null);

    assertNull(result);
    verify(testCasesRepository, never()).save(any(TestCasesEntity.class));
  }

  @Test
  @DisplayName("PUT: updateTestCase: Should return null when the mapper returns null")
  void updateTestCase_ReturnsNull_WhenMapperReturnsNull() {
    try (MockedStatic<TestCasesMapper> mapperMock = Mockito.mockStatic(TestCasesMapper.class)) {
      mapperMock.when(() -> TestCasesMapper.toEntity(testCaseVO)).thenReturn(null);

      TestCaseVO result = testCasesDAO.update(testCaseVO);

      assertNull(result);
      verify(testCasesRepository, never()).save(any(TestCasesEntity.class));
    }
  }

  @Test
  @DisplayName("PUT: updateTestCase: Should map, save, and return the updated VO")
  void updateTestCase_Success() {
    when(testCasesRepository.save(any(TestCasesEntity.class))).thenReturn(testCasesEntity);

    TestCaseVO result = testCasesDAO.update(testCaseVO);

    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("DAO Test Case", result.getTitle());
    assertEquals("1. Open module\n2. Save changes", result.getSteps());
    assertEquals("The update is persisted", result.getExpectedOutput());
    assertEquals(10L, result.getFeatureId());
    verify(testCasesRepository).save(any(TestCasesEntity.class));
  }
}
