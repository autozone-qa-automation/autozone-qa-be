/*
 * Tecnológico de Monterrey — Campus Chihuahua
 * Desarrollo e Implantación de Sistemas de Software
 * TC3005B GPO500 - 2026
 * Autozone QA Automation
 */

package com.az_qa.backend.dao;

import com.az_qa.backend.entity.ServicesEntity;
import com.az_qa.backend.entity.UrlEntity;
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.mapper.ServicesMapper;
import com.az_qa.backend.repository.ServicesRepository;
import com.az_qa.backend.vo.ServicesVO;
import com.az_qa.backend.vo.UrlVO;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Data access component that coordinates service persistence operations.
 */
@Repository
public class ServicesDAO {

  /** Repository dependency used for service persistence operations. */
  @Autowired private ServicesRepository servicesRepository;

  /**
   * Finds a service by id with its URLs.
   *
   * @param id service identifier
   * @return service representation with URLs
   */
  public ServicesVO findById(Long id) {
    Optional<ServicesVO> servicesVO =
        servicesRepository.findByIdWithUrls(id).map(ServicesMapper::toVO);

    if (servicesVO.isEmpty()) {
      throw new ItemNotFoundException("Service with id " + id + " not found");
    }

    return servicesVO.get();
  }

  /**
   * Finds the service information by id.
   * @param id Service id.
   * @return Service information.
   */
  public ServicesVO findServiceById(Long id) {
    Optional<ServicesVO> servicesVO = servicesRepository.findById(id).map(ServicesMapper::toVO);

    if (servicesVO.isEmpty()) {
      throw new ItemNotFoundException("Service with id " + id + " not found");
    }
    return servicesVO.get();
  }

  /**
   * Retrieves all existing services with their URLs.
   *
   * @return list of all services found
   */
  public List<ServicesVO> findAll() {
    return servicesRepository.findAllWithUrls().stream().map(ServicesMapper::toVO).toList();
  }

  /**
   * Creates a new service with the provided details.
   *
   * @param serviceVO the service data to create
   * @return the created service representation
   */
  public ServicesVO createService(ServicesVO serviceVO) {
    return ServicesMapper.toVO(servicesRepository.save(ServicesMapper.serviceToEntity(serviceVO)));
  }

  public ServicesVO updateService(Long id, ServicesVO serviceVO) {
    ServicesEntity existing =
        servicesRepository
            .findByIdWithUrls(id)
            .orElseThrow(() -> new ItemNotFoundException("Service with id " + id + " not found"));

    existing.setName(serviceVO.getName());
    existing.setDescription(serviceVO.getDescription());

    if (serviceVO.getUrls() != null) {

      for (UrlVO urlVO : serviceVO.getUrls()) {

        // validar formato URL
        if (urlVO.getUrl() == null || !urlVO.getUrl().matches("^(http|https)://.*$")) {

          throw new IllegalArgumentException("Invalid URL format: " + urlVO.getUrl());
        }

        // SI YA EXISTE -> UPDATE
        if (urlVO.getIdUrl() != null) {

          UrlEntity existingUrl =
              existing.getUrls().stream()
                  .filter(u -> u.getIdUrl().equals(urlVO.getIdUrl()))
                  .findFirst()
                  .orElseThrow(
                      () ->
                          new ItemNotFoundException(
                              "URL with id " + urlVO.getIdUrl() + " not found"));

          existingUrl.setNombre(urlVO.getNombre());
          existingUrl.setUrl(urlVO.getUrl());

        } else {

          // NUEVA URL
          UrlEntity newUrl = ServicesMapper.urlToEntity(urlVO);
          newUrl.setServicio(existing);

          existing.getUrls().add(newUrl);
        }
      }
    }

    return ServicesMapper.toVO(servicesRepository.save(existing));
  }

  public boolean existsByNameIgnoreCaseAndIdNot(String name, Long id) {
    return servicesRepository.existsByNameIgnoreCaseAndIdNot(name, id);
  }
}
