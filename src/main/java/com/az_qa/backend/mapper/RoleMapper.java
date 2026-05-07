/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.mapper;

import com.az_qa.backend.entity.RoleEntity;
import com.az_qa.backend.vo.RoleVO;

/**
 * Mapper for converting between RoleEntity and RoleVO.
 */
public class RoleMapper {

  /**
   * Converts a RoleEntity to a RoleVO.
   *
   * @param entity the RoleEntity to convert
   * @return the converted RoleVO
   */
  public static RoleVO toVO(RoleEntity entity) {
    if (entity == null) {
      return null;
    }

    RoleVO vo = new RoleVO();
    vo.setId(entity.getId());
    vo.setPermission(entity.getPermission());

    return vo;
  }

  /**
   * Converts a RoleVO to a RoleEntity.
   *
   * @param vo the RoleVO to convert
   * @return the converted RoleEntity
   */
  public static RoleEntity toEntity(RoleVO vo) {
    if (vo == null) {
      return null;
    }

    RoleEntity entity = new RoleEntity();
    entity.setId(vo.getId());
    entity.setPermission(vo.getPermission());
    return entity;
  }
}
