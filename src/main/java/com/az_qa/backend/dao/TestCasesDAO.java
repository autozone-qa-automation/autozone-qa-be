/*

Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.dao;

import com.az_qa.backend.entity.TestCasesEntity;
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.mapper.TestCasesMapper;
import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.repository.TestCasesRepository;
import com.az_qa.backend.vo.TestCaseVO;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TestCasesDAO {

  /**
   * Repository dependency used for test case persistence operations.
   */
  @Autowired private TestCasesRepository repository;

  @Autowired private FeaturesRepository featuresRepository;

  // @Autowired private final TestCasesMapper mapper;

  @Transactional
  public TestCaseVO create(TestCaseVO testcaseVO) {
    if (testcaseVO == null) {
      return null;
    }
    TestCasesEntity entity = TestCasesMapper.toEntity(testcaseVO);
    if (entity == null) {
      return null;
    }

    if (entity.getFeature() == null) {
      entity.setFeature(
          featuresRepository
              .findById(testcaseVO.getFeatureId())
              .orElseThrow(
                  () ->
                      new ItemNotFoundException(
                          "Feature with id {" + entity.getFeature().getId() + "} not found.")));
    }

    entity.setNew(true);
    TestCaseVO saved = TestCasesMapper.toVO(repository.save(entity));
    return saved;
    // return mapper.toVO(entity);
  }

  @Transactional
  public TestCaseVO update(TestCaseVO testcaseVO) {
    if (testcaseVO == null) {
      return null;
    }
    TestCasesEntity entity = TestCasesMapper.toEntity(testcaseVO);
    if (entity == null) {
      return null;
    }

    TestCaseVO updated = TestCasesMapper.toVO(repository.save(entity));
    return updated;
  }

  @Transactional
  public void deactivate(Long id) {
    TestCasesEntity entity =
        repository
            .findByIdAndIsActive(id, true)
            .orElseThrow(() -> new ItemNotFoundException("TestCase not found with id: " + id));
    entity.setIsActive(false);
    repository.save(entity);
  }

  public List<TestCaseVO> findByFeature(Long featureId) {
    return repository.findByFeature_IdAndIsActive(featureId, true).stream()
        .map(TestCasesMapper::toVO)
        .toList();
  }

  public TestCaseVO findByTitle(String title) {
    Optional<TestCasesEntity> entity = repository.findByTitleAndIsActive(title, true);
    if (entity.isEmpty()) {
      throw new ItemNotFoundException("Test case not found with title: " + title);
    }
    return TestCasesMapper.toVO(entity.get());
  }

  public Optional<TestCaseVO> findById(long id) {
    return repository.findByIdAndIsActive(id, true).map(TestCasesMapper::toVO);
  }

  public List<TestCaseVO> findAll() {
    List<TestCasesEntity> entities = repository.findByIsActive(true);

    List<Long> featureIds = entities.stream().map(e -> e.getFeature().getId()).distinct().toList();

    Map<Long, String> featureNames =
        featuresRepository.findAllById(featureIds).stream()
            .collect(Collectors.toMap(f -> f.getId(), f -> f.getName()));

    return entities.stream()
        .map(
            e -> {
              TestCaseVO vo = TestCasesMapper.toVO(e);
              vo.setFeatureName(featureNames.get(e.getFeature().getId()));
              return vo;
            })
        .toList();
  }
}
