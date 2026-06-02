/*

Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.mapper;

import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.entity.ReleaseEntity;
// import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.entity.TestCasesEntity;
import com.az_qa.backend.vo.TestCaseVO;

public class TestCasesMapper {

  private TestCasesMapper() {}

  public static TestCasesEntity toEntity(TestCaseVO vo) {
    TestCasesEntity e = new TestCasesEntity();
    e.setId(vo.getId());
    e.setTitle(vo.getTitle());
    e.setDescription(vo.getDescription());
    e.setType(vo.getType());
    e.setPreconditions(vo.getPreconditions());
    e.setPostconditions(vo.getPostconditions());
    e.setInputs(vo.getInputs());
    e.setSteps(vo.getSteps());
    e.setExpectedOutput(vo.getExpectedOutput());

    if (vo.getFeatureId() != null) {
      FeatureEntity feature = new FeatureEntity();
      feature.setId(vo.getFeatureId());
      e.setFeature(feature);
    }
    if (vo.getReleaseId() != null) {
      ReleaseEntity release = new ReleaseEntity();
      release.setReleaseId(vo.getReleaseId());
      e.setRelease(release);
    }
    return e;
  }

  public static TestCaseVO toVO(TestCasesEntity e) {
    if (e == null) {
      return null;
    }

    TestCaseVO vo = new TestCaseVO();
    vo.setId(e.getId());
    vo.setCode(e.getCode());
    vo.setTitle(e.getTitle());
    vo.setFeatureId(e.getFeature().getId());
    vo.setReleaseId(e.getRelease() != null ? e.getRelease().getReleaseId() : null);
    vo.setDescription(e.getDescription());
    vo.setType(e.getType());
    vo.setPreconditions(e.getPreconditions());
    vo.setPostconditions(e.getPostconditions());
    vo.setInputs(e.getInputs());
    vo.setSteps(e.getSteps());
    vo.setExpectedOutput(e.getExpectedOutput());
    vo.setActive(e.getIsActive());
    vo.setFeature(FeatureMapper.toVO(e.getFeature()));
    vo.setRelease(ReleaseMapper.toVO(e.getRelease()));
    return vo;
  }
}
