/*

Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.mapper;

import com.az_qa.backend.entity.TestCasesEntity;
import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.repository.ReleaseRepository;
import com.az_qa.backend.vo.TestCaseVO;
import org.springframework.stereotype.Component;

@Component
public class TestCasesMapper {

  private final FeaturesRepository featuresRepository;
  private final ReleaseRepository releaseRepository;

  public TestCasesMapper(
      FeaturesRepository featuresRepository, ReleaseRepository releaseRepository) {
    this.featuresRepository = featuresRepository;
    this.releaseRepository = releaseRepository;
  }

  public TestCasesEntity toEntity(TestCaseVO vo) {
    TestCasesEntity e = new TestCasesEntity();
    e.setTitle(vo.getTitle());
    e.setFeature(featuresRepository.getReferenceById(vo.getRelatedFeature()));
    e.setRelease(releaseRepository.getReferenceById(vo.getReleaseId()));
    e.setDescription(vo.getDescription());
    e.setType(vo.getType());
    e.setPreconditions(vo.getPreconditions());
    e.setPostconditions(vo.getPostconditions());
    e.setInputs(vo.getInputs());
    e.setSteps(vo.getSteps());
    e.setExpectedOutput(vo.getExpectedOutput());
    return e;
  }

  public TestCaseVO toVO(TestCasesEntity e) {
    TestCaseVO vo = new TestCaseVO();
    vo.setId(e.getId());
    vo.setCode(e.getCode());
    vo.setTitle(e.getTitle());
    vo.setRelatedFeature(e.getFeature().getId());
    vo.setReleaseId(e.getRelease() != null ? e.getRelease().getReleaseId() : null);
    vo.setDescription(e.getDescription());
    vo.setType(e.getType());
    vo.setPreconditions(e.getPreconditions());
    vo.setPostconditions(e.getPostconditions());
    vo.setInputs(e.getInputs());
    vo.setSteps(e.getSteps());
    vo.setExpectedOutput(e.getExpectedOutput());
    vo.setActive(e.getActive());
    return vo;
  }
}
