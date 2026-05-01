package com.az_qa.backend.mapper;

import com.az_qa.backend.entity.RoleEntity;
import com.az_qa.backend.entity.UserEntity;
import com.az_qa.backend.vo.UserVO;

/**
 * Mapper for converting between UserEntity and UserVO.
 */
public class UserMapper {

  /**
   * Converts a UserEntity to a UserVO.
   *
   * @param entity the UserEntity to convert
   * @return the converted UserVO
   */
  public static UserVO toVO(UserEntity entity) {
    if (entity == null) {
      return null;
    }

    UserVO vo = new UserVO();
    vo.setId(entity.getId());
    vo.setName(entity.getName());
    vo.setLastName(entity.getLastName());
    vo.setEmail(entity.getEmail());
    vo.setPassword(entity.getPassword());
    vo.setIsActive(entity.getIsActive());
    if (entity.getRole() != null) {
      vo.setRoleId(entity.getRole().getId());
      vo.setRolePermission(entity.getRole().getPermission());
    }

    return vo;
  }

  /**
   * Converts a UserVO to a UserEntity.
   *
   * @param vo the UserVO to convert
   * @return the converted UserEntity
   */
  public static UserEntity toEntity(UserVO vo) {
    if (vo == null) {
      return null;
    }

    UserEntity entity = new UserEntity();
    entity.setId(vo.getId());
    entity.setName(vo.getName());
    entity.setLastName(vo.getLastName());
    entity.setEmail(vo.getEmail());
    entity.setPassword(vo.getPassword());
    entity.setIsActive(vo.getIsActive());

    if (vo.getRoleId() != 0) {
      RoleEntity roleEntity = new RoleEntity();
      roleEntity.setId(vo.getRoleId());
      entity.setRole(roleEntity);
    }

    return entity;
  }
}
