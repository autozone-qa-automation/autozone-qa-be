package com.az_qa.backend.mapper;

import com.az_qa.backend.entity.RoleEntity;
import com.az_qa.backend.entity.UserEntity;
import com.az_qa.backend.vo.UserVO;

/**
 * Mapper for converting between UserEntity and UserVO.
 */
public class UserMapper {

  /**
   * Utility class constructor.
   */
  private UserMapper() {}

  /**
   * Converts a {@link UserEntity} into a {@link UserVO}.
   * Returns {@code null} when the input is {@code null}.
   * Maps role details into {@code roleId} and nested {@code role}
   * when available.
   *
   * @param entity persistence entity
   * @return value object representation or {@code null}
   */
  public static UserVO toVO(UserEntity entity) {
    if (entity == null) {
      return null;
    }

    UserVO vo =
        new UserVO(
            entity.getId(),
            entity.getName(),
            entity.getLastName(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getIsActive(),
            entity.getRole().getId(),
            RoleMapper.toVO(entity.getRole()));
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
