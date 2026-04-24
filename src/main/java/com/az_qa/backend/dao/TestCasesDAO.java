/*

Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.dao;

import com.az_qa.backend.entity.TestCasesEntity;
import com.az_qa.backend.exception.ResourceNotFoundException;
import com.az_qa.backend.mapper.TestCasesMapper;
import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.repository.TestCasesRepository;
import com.az_qa.backend.vo.TestCaseVO;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TestCasesDAO {

  private final TestCasesRepository repository;
  private final FeaturesRepository featuresRepository;
  private final TestCasesMapper mapper;

  public TestCasesDAO(
      TestCasesRepository repository,
      FeaturesRepository featuresRepository,
      TestCasesMapper mapper) {
    this.repository = repository;
    this.featuresRepository = featuresRepository;
    this.mapper = mapper;
  }

  @Transactional
  public TestCaseVO create(TestCaseVO vo) {
    TestCasesEntity entity = mapper.toEntity(vo);
    entity.setId(null);
    entity.setCode(null);
    entity.setActive(true);
    entity = repository.save(entity);
    entity.setCode("TC-" + entity.getId());
    entity = repository.save(entity);
    return mapper.toVO(entity);
  }

  @Transactional
  public void deactivate(Long id) {
    TestCasesEntity entity =
        repository
            .findByIdAndActive(id, true)
            .orElseThrow(() -> new ResourceNotFoundException("TestCase not found with id: " + id));
    entity.setActive(false);
    repository.save(entity);
  }

  public List<TestCaseVO> findByFeature(Long featureId) {
    return repository.findByFeature_IdAndActiveTrue(featureId).stream()
        .map(mapper::toVO)
        .toList();
  }

  public Optional<TestCaseVO> findById(long id) {
    return repository.findByIdAndActive(id, true).map(mapper::toVO);
  }

  public List<TestCaseVO> findAll() {
    List<TestCasesEntity> entities = repository.findByActive(true);

    List<Long> featureIds =
        entities.stream().map(e -> e.getFeature().getId()).distinct().toList();

    Map<Long, String> featureNames =
        featuresRepository.findAllById(featureIds).stream()
            .collect(Collectors.toMap(f -> f.getId(), f -> f.getName()));

    return entities.stream()
        .map(
            e -> {
              TestCaseVO vo = mapper.toVO(e);
              vo.setFeatureName(featureNames.get(e.getFeature().getId()));
              return vo;
            })
        .toList();
  }
}
