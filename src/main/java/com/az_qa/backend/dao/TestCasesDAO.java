/*

Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.dao;

import com.az_qa.backend.entity.TestCasesEntity;
import com.az_qa.backend.mapper.TestCasesMapper;
import com.az_qa.backend.repository.TestCasesRepository;
import com.az_qa.backend.vo.TestCaseVO;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TestCasesDAO {

  private final TestCasesRepository repository;
  private final TestCasesMapper mapper;

  public TestCasesDAO(TestCasesRepository repository, TestCasesMapper mapper) {
    this.repository = repository;
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
            .findByIdAndActiveTrue(id)
            .orElseThrow(() -> new RuntimeException("TestCase not found"));

    entity.setActive(false);
  }

  public List<TestCaseVO> findByFeature(Long featureId) {

    return repository.findByActiveTrue().stream()
        .filter(tc -> tc.getRelatedFeature().equals(featureId))
        .map(mapper::toVO)
        .toList();
  }
}
