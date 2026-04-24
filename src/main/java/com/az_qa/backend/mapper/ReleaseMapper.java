/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.mapper;

public class ReleaseMapper {

  private ReleaseMapper() {}

  public static com.az_qa.backend.vo.ReleaseVO toVO(com.az_qa.backend.entity.ReleaseEntity entity) {
    if (entity == null) {
      return null;
    }

    return new com.az_qa.backend.vo.ReleaseVO(
        entity.getReleaseId(),
        entity.getReleaseName(),
        entity.getReleaseDescription(),
        entity.getReleaseCreationDate(),
        entity.getReleaseLaunchDate(),
        entity.getReleaseVersion(),
        entity.getReleaseTags(),
        entity.getReleaseStatus(),
        entity.getReleaseServices(),
        entity.getReleaseServiceIds());
  }

  public static com.az_qa.backend.entity.ReleaseEntity toEntity(com.az_qa.backend.vo.ReleaseVO vo) {
    if (vo == null) {
      return null;
    }

    com.az_qa.backend.entity.ReleaseEntity entity = new com.az_qa.backend.entity.ReleaseEntity();
    entity.setReleaseId(vo.getReleaseId());
    entity.setReleaseName(vo.getReleaseName());
    entity.setReleaseDescription(vo.getReleaseDescription());
    entity.setReleaseCreationDate(vo.getReleaseCreationDate());
    entity.setReleaseLaunchDate(vo.getReleaseLaunchDate());
    entity.setReleaseVersion(vo.getReleaseVersion());
    entity.setReleaseTags(vo.getReleaseTags());
    entity.setReleaseStatus(vo.getReleaseStatus());

    return entity;
  }
}
