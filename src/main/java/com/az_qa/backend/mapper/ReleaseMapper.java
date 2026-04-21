package com.az_qa.backend.mapper;

public class ReleaseMapper {
    
  private ReleaseMapper() {}

  public static com.az_qa.backend.vo.ReleaseVO toVO(com.az_qa.backend.entity.ReleaseEntity entity) {
    if (entity == null) {
      return null;
    }

    return new com.az_qa.backend.vo.ReleaseVO(
        entity.getId(),
        entity.getName(),
        entity.getDescription(),
        entity.getCreationDate(),
        entity.getLaunchDate(),
        entity.getVersion(),
        entity.getTags(),
        entity.getStatus(),
        entity.getService()
    );
  }

  public static com.az_qa.backend.entity.ReleaseEntity toEntity(com.az_qa.backend.vo.ReleaseVO vo) {
    if (vo == null) {
      return null;
    }

    com.az_qa.backend.entity.ReleaseEntity entity = new com.az_qa.backend.entity.ReleaseEntity();
    entity.setId(vo.getReleaseId());
    entity.setName(vo.getReleaseName());
    entity.setDescription(vo.getReleaseDescription());
    entity.setCreationDate(vo.getReleaseCreationDate());
    entity.setLaunchDate(vo.getReleaseLaunchDate());
    entity.setVersion(vo.getReleaseVersion());
    entity.setTags(vo.getReleaseTags());
    entity.setStatus(vo.getReleaseStatus());
    entity.setService(vo.getReleaseService());

    return entity;
  }


}
