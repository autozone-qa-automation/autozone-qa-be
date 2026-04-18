/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.mapper;

import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.vo.FeatureVO;

public class FeatureMapper {

  private FeatureMapper() {}

  public static FeatureVO toVO(FeatureEntity entity) {
    if (entity == null) return null;

    return new FeatureVO(
        entity.getId(), entity.getName(), entity.getDescription(), entity.getIdServices());
  }

  public static FeatureEntity toEntity(FeatureVO featureVO) {
    if (featureVO == null) return null;

    FeatureEntity entity = new FeatureEntity();
    entity.setName(featureVO.getFeatureName());
    entity.setDescription(featureVO.getFeatureDescription());

    if (featureVO.getIdService() != null) {
      entity.setIdServices(featureVO.getIdService());
    }

    return entity;
  }
}
