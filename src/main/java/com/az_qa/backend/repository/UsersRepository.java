package com.az_qa.backend.repository;

import com.az_qa.backend.entity.UserEntity;
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
}
