/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.mapper;

/**
 * Utility mapper for converting release persistence entities and value objects.
 * This class contains only static conversion methods.
 */
public class ReleaseMapper {

  private ReleaseMapper() {}

  /**
   * Converts a release entity into a release value object.
   *
   * @param entity the release entity to convert
   * @return the matching release value object, or {@code null} when the input is {@code null}
   */
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
        entity.getReleaseServiceIds(),
        entity.getReleaseFeatureIds());
  }

  /**
   * Converts a release value object into a release entity.
   *
   * @param vo the release value object to convert
   * @return the matching release entity, or {@code null} when the input is {@code null}
   */
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
