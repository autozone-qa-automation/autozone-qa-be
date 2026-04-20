/*
 * Tecnológico de Monterrey — Campus Chihuahua
 * Desarrollo e Implantación de Sistemas de Software
 * TC3005B GPO500 - 2026
 * Autozone QA Automation
 */

package com.az_qa.backend.controller;

import com.az_qa.backend.service.ServicesService;
import com.az_qa.backend.vo.ServicesVO;
import com.az_qa.backend.vo.UrlVO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/services")
@Validated
public class ServiceController {

  @Autowired
  private ServicesService servicesService;

  @GetMapping
  public ResponseEntity<List<ServicesVO>> getAllServices() {
    List<ServicesVO> services = servicesService.getAllServices();
    return new ResponseEntity<>(services, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ServicesVO> getServiceById(@PathVariable Long id) {
    try {
      ServicesVO service = servicesService.getServiceById(id);
      return new ResponseEntity<>(service, HttpStatus.OK);
    } catch (RuntimeException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/test/{id}")
  public ResponseEntity<ServicesVO> getServiceByIdTest(@PathVariable Long id) {
    List<UrlVO> urls = new ArrayList<>();
    urls.add(new UrlVO(1L, "Produccion", "https://prod.autozone.com"));
    urls.add(new UrlVO(2L, "QA", "https://qa.autozone.com"));
    urls.add(new UrlVO(3L, "Dev", "https://dev.autozone.com"));

    ServicesVO service = new ServicesVO(id, "Service Test", "Description Test", urls);
    return new ResponseEntity<>(service, HttpStatus.OK);
  }
}