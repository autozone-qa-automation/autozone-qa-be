package com.az_qa.backend.vo;

import jakarta.validation.constraints.NotBlank;

public class FeatureVO {
  private Long id;

  @NotBlank(message = "Feature name is required")
  private String featureName;

  @NotBlank(message = "Feature description is required")
  private String featureDescription;

  @NotBlank(message = "Feature service is required")
  private String featureService;

  public FeatureVO() {}

  public FeatureVO(Long id, String featureName, String featureDescription, String featureService) {
    this.id = id;
    this.featureName = featureName;
    this.featureDescription = featureDescription;
    this.featureService = featureService;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFeatureName() {
    return featureName;
  }

  public void setFeatureName(String featureName) {
    this.featureName = featureName;
  }

  public String getFeatureDescription() {
    return featureDescription;
  }

  public void setFeatureDescription(String featureDescription) {
    this.featureDescription = featureDescription;
  }

  public String getFeatureService() {
    return featureService;
  }

  public void setFeatureService(String featureService) {
    this.featureService = featureService;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    FeatureVO that = (FeatureVO) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (featureName != null ? !featureName.equals(that.featureName) : that.featureName != null)
      return false;
    if (featureDescription != null
        ? !featureDescription.equals(that.featureDescription)
        : that.featureDescription != null) return false;
    return featureService != null
        ? featureService.equals(that.featureService)
        : that.featureService == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (featureName != null ? featureName.hashCode() : 0);
    result = 31 * result + (featureDescription != null ? featureDescription.hashCode() : 0);
    result = 31 * result + (featureService != null ? featureService.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "FeatureVO{"
        + "id="
        + id
        + ", featureName='"
        + featureName
        + '\''
        + ", featureDescription='"
        + featureDescription
        + '\''
        + ", featureService='"
        + featureService
        + '\''
        + '}';
  }
}
