/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.vo;

import java.util.List;

/** Value Object representing a service and its features within a report. */
public class ReportServiceVO {

  private String serviceName;
  private List<ReportFeatureVO> features;

  public ReportServiceVO() {}

  public ReportServiceVO(String serviceName, List<ReportFeatureVO> features) {
    this.serviceName = serviceName;
    this.features = features;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public List<ReportFeatureVO> getFeatures() {
    return features;
  }

  public void setFeatures(List<ReportFeatureVO> features) {
    this.features = features;
  }
}
