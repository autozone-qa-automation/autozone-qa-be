/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.repository;

import com.az_qa.backend.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Long> {
  /**
   * Finds a user by email.
   *
   * @param email user email
   * @return optional containing the matching user entity when found
   */
  Optional<UserEntity> findByEmail(String email);

  /**
   * Finds users by active status.
   *
   * @param isActive user active status
   * @return list of users matching the active status
   */
  List<UserEntity> findByIsActive(boolean isActive);

  /**
   * Finds a user by ID and active status.
   *
   * @param id       user ID
   * @param isActive user active status
   * @return optional containing the matching user entity when found
   */
  Optional<UserEntity> findByIdAndIsActive(long id, boolean isActive);
}
