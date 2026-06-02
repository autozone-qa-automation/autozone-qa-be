/*
 * Tecnológico de Monterrey — Campus Chihuahua
 * Desarrollo e Implantación de Sistemas de Software
 * TC3005B GPO500 - 2026
 * Autozone QA Automation
 */

package com.az_qa.backend.service;

import com.az_qa.backend.dao.ServicesDAO;
import com.az_qa.backend.exception.DuplicatedItemException;
import com.az_qa.backend.vo.ServicesVO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service component that handles business logic for services.
 */
@Service
public class ServicesService {

  /** DAO dependency used for service data access operations. */
  @Autowired
  private ServicesDAO servicesDAO;

  /**
   * Retrieves all existing services with their URLs.
   *
   * @return list of all services found
   */
  public List<ServicesVO> getAllServices() {
    return servicesDAO.findAll();
  }

  /**
   * Finds a service by id with its URLs.
   *
   * @param id service identifier
   * @return service representation with URLs
   */
  public ServicesVO getServiceById(Long id) {
    return servicesDAO.findById(id);
  }

  /**
   * Creates a new service with the provided details.
   *
   * @param serviceVO the service data to create
   * @return the created service representation
   */
  @Transactional
  public ServicesVO createService(ServicesVO serviceVO) {
    String sanitizedName = serviceVO.getName().trim();
    serviceVO.setName(sanitizedName);

    List<ServicesVO> existingServices = getAllServices();
    boolean isDuplicated = existingServices.stream().anyMatch(s -> s.getName().equalsIgnoreCase(sanitizedName));

    if (isDuplicated) {
      throw new DuplicatedItemException(
          "A service with the name '" + sanitizedName + "' already exists.");
    }

    if (serviceVO.getDescription() == null || serviceVO.getDescription().isBlank()) {
      serviceVO.setDescription("No description provided for " + sanitizedName);
    }

    return servicesDAO.createService(serviceVO);
  }

  /**
   * Deletes a service by its identifier.
   *
   * @param id the identifier of the service to delete
   */
  public void deleteService(Long id) {
    servicesDAO.deleteService(id);
  }
}
