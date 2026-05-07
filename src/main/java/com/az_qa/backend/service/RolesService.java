/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import com.az_qa.backend.dao.RolesDAO;
import com.az_qa.backend.vo.RoleVO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolesService {

  @Autowired private RolesDAO roleDAO;

  /**
   * Finds a role by id.
   *
   * @param id role identifier
   * @return role representation
   */
  public RoleVO findById(Long id) {
    return roleDAO.findById(id);
  }

  /**
   * Retrieves all roles available in the persistence layer.
   *
   * @return list of roles
   */
  public List<RoleVO> findAll() {
    return roleDAO.findAll();
  }
}
