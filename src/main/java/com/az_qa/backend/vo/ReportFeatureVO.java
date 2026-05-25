/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.vo;

import java.util.List;

/** Value Object representing a feature and its test cases within a report. */
public class ReportFeatureVO {

  private String featureName;
  private List<String> testCases;

  public ReportFeatureVO() {}

  public ReportFeatureVO(String featureName, List<String> testCases) {
    this.featureName = featureName;
    this.testCases = testCases;
  }

  public String getFeatureName() {
    return featureName;
  }

  public void setFeatureName(String featureName) {
    this.featureName = featureName;
  }

  public List<String> getTestCases() {
    return testCases;
  }

  public void setTestCases(List<String> testCases) {
    this.testCases = testCases;
  }
}
