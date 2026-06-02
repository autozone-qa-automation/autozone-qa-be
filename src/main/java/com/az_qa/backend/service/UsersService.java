/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import com.az_qa.backend.dao.UsersDAO;
import com.az_qa.backend.exception.DuplicatedItemException;
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.exception.MissingRequiredFieldException;
import com.az_qa.backend.vo.UpdateUserVO;
import com.az_qa.backend.vo.UserVO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service contract for user management operations.
 */
@Service
public class UsersService {

  @Autowired private UsersDAO userDAO;

  /**
   * Adds a new user to the persistence layer.
   *
   * @param userVO user payload to persist
   * @return persisted user representation or {@code null} when the input is
   *         {@code null}
   * @throws MissingRequiredFieldException when {@code producerId} is missing
   *                                       for user creation
   */
  public UserVO add(UserVO userVO) {
    if (userVO.getRoleId() == null) {
      throw new MissingRequiredFieldException("Producer id is required for user creation.");
    }
    if (userVO.getEmail() == null) {
      throw new MissingRequiredFieldException("Email is required for user creation.");
    }
    if (userVO.getPassword() == null) {
      throw new MissingRequiredFieldException("Password is required for user creation.");
    }
    if (userVO.getName() == null) {
      throw new MissingRequiredFieldException("Name is required for user creation.");
    }
    if (userVO.getLastName() == null) {
      throw new MissingRequiredFieldException("Last name is required for user creation.");
    }
    try {
      userDAO.findByEmail(userVO.getEmail());
      throw new DuplicatedItemException(
          "User with email {" + userVO.getEmail() + "} already exists.");
    } catch (ItemNotFoundException e) {
      return userDAO.add(userVO);
    }
  }

  /**
   * Finds a user by id.
   *
   * @param id user identifier
   * @return user representation
   */
  public UserVO findById(Long id) {
    return userDAO.findById(id);
  }

  /**
   * Retrieves all users available in the persistence layer.
   *
   * @return list of users
   */
  public List<UserVO> findAll() {
    return userDAO.findAll();
  }

  /**
   * Deactivates a user by id.
   *
   * @param id user id
   * @return no content response if deactivated, not found if user does not exist
   *
   * @throws ItemNotFoundException when no user exists with the provided id
   */
  public void deactivate(Long id) {
    userDAO.deactivate(id);
  }

  /**
   * Updates a user in the persistence layer.
   *
   * @param userVO user payload to persist
   * @return persisted user representation
   * @throws MissingRequiredFieldException when mandatory fields are missing
   * @throws ItemNotFoundException when the user does not exist or is inactive
   * @throws DuplicatedItemException when the email belongs to another user
   */
  public UserVO update(Long id, UpdateUserVO updateUserVO) {
    try {
      UserVO existingUserWithEmail = userDAO.findByEmail(updateUserVO.getEmail());
      if (!existingUserWithEmail.getId().equals(id)) {
        throw new DuplicatedItemException(
            "User with email {" + updateUserVO.getEmail() + "} already exists.");
      }
    } catch (ItemNotFoundException e) {
    }

    UserVO userVO =
        new UserVO(
            id,
            updateUserVO.getName(),
            updateUserVO.getLastName(),
            updateUserVO.getEmail(),
            null,
            updateUserVO.getIsActive(),
            updateUserVO.getRoleId());

    return userDAO.update(userVO);
  }
}
