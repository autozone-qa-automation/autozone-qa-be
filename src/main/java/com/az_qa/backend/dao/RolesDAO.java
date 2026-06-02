/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.dao;

import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.mapper.RoleMapper;
import com.az_qa.backend.repository.RolesRepository;
import com.az_qa.backend.vo.RoleVO;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RolesDAO {

  @Autowired private RolesRepository roleRepository;

  /**
   * Finds a producer by id.
   *
   * @param id producer identifier
   * @return producer representation
   */
  public RoleVO findById(Long id) {
    Optional<RoleVO> roleVO = roleRepository.findById(id).map(RoleMapper::toVO);
    if (roleVO.isEmpty()) {
      throw new ItemNotFoundException("Role with id {" + id + "} not found.");
    }
    return roleVO.get();
  }

  /**
   * Retrieves all producers currently stored.
   *
   * @return list of producers
   */
  public List<RoleVO> findAll() {
    List<RoleVO> permissions = roleRepository.findAll().stream().map(RoleMapper::toVO).toList();
    return permissions;
  }
}
