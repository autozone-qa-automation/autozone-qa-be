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
    if (entity == null) {
      return null;
    }

    FeatureVO featureVO =
        new FeatureVO(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getService() != null ? entity.getService().getId() : null);
    if (entity.getService() != null) {
      featureVO.setServiceName(entity.getService().getName());
    }
    return featureVO;
  }

  public static FeatureEntity toEntity(FeatureVO featureVO) {
    if (featureVO == null) return null;

    FeatureEntity entity = new FeatureEntity();
    entity.setName(featureVO.getFeatureName());
    entity.setDescription(featureVO.getFeatureDescription());

    return entity;
  }
}
