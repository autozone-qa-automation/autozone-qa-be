/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.dao;

import com.az_qa.backend.entity.UserEntity;
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.mapper.UserMapper;
import com.az_qa.backend.repository.RolesRepository;
import com.az_qa.backend.repository.UsersRepository;
import com.az_qa.backend.vo.UserVO;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class UsersDAO {
  /**
   * Repository dependency used for user persistence operations.
   */
  @Autowired private UsersRepository userRepository;

  @Autowired private RolesRepository roleRepository;

  /**
   * Persists a new user and returns the stored representation.
   *
   * @param userVO user to persist
   * @return persisted user or {@code null} when the input is {@code null}
   * @throws ItemNotFoundException when {@code roleId} is provided but no
   *                               role exists with that id
   */
  public UserVO add(UserVO userVO) {
    if (userVO == null) {
      return null;
    }
    UserEntity userEntity = UserMapper.toEntity(userVO);
    if (userEntity == null) {
      return null;
    }

    if (userEntity.getRole() == null) {
      userEntity.setRole(
          roleRepository
              .findById(userVO.getRoleId())
              .orElseThrow(
                  () ->
                      new ItemNotFoundException(
                          "Role with id {" + userVO.getRoleId() + "} not found.")));
    }

    userEntity.setNew(true);
    UserVO saved = UserMapper.toVO(userRepository.save(userEntity));
    return saved;
  }

  public UserVO findByEmail(String email) {
    Optional<UserEntity> userEntity = userRepository.findByEmail(email);
    if (userEntity.isEmpty()) {
      throw new ItemNotFoundException("User with email {" + email + "} not found.");
    }
    return UserMapper.toVO(userEntity.get());
  }


  /**
   * Finds a user by id.
   *
   * @param id user identifier
   * @return user representation
   * @throws ItemNotFoundException when no user exists with that id
   */
  public UserVO findById(Long id) {
    Optional<UserVO> userVO = userRepository.findById(id).map(UserMapper::toVO);
    if (userVO.isEmpty()) {
      throw new ItemNotFoundException("User with id {" + id + "} not found.");
    }
    return userVO.get();
  }

  /**
   * Retrieves all users currently stored.
   *
   * @return list of users
   */
  public List<UserVO> findAll() {
    List<UserVO> users = userRepository.findAll().stream().map(UserMapper::toVO).toList();
    return users;
  }

}