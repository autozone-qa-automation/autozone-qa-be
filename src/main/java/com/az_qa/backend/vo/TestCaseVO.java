/*

Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.vo;

import com.az_qa.backend.enumeration.TestCaseType;

public class TestCaseVO {

  private Long id;
  private String title;
  private Long relatedFeature;
  private String description;
  private TestCaseType type;
  private String preconditions;
  private String postconditions;
  private String inputs;
  private String steps;

  public TestCaseVO() {
  }

  public TestCaseVO(
      Long id,
      String title,
      Long relatedFeature,
      String description,
      TestCaseType type,
      String preconditions,
      String postconditions,
      String inputs,
      String steps) {

    if (title == null || title.isEmpty()) {
      throw new IllegalArgumentException("Title cannot be null or empty");
    }

    if (relatedFeature == null) {
      throw new IllegalArgumentException("Related feature cannot be null");
    }

    if (description == null || description.isEmpty()) {
      throw new IllegalArgumentException("Description cannot be null or empty");
    }

    if (type == null) {
      throw new IllegalArgumentException("Test case type cannot be null");
    }

    if (preconditions == null || preconditions.isEmpty()) {
      throw new IllegalArgumentException("Preconditions cannot be null or empty");
    }

    if (postconditions == null || postconditions.isEmpty()) {
      throw new IllegalArgumentException("Postconditions cannot be null or empty");
    }

    if (inputs == null || inputs.isEmpty()) {
      throw new IllegalArgumentException("Inputs cannot be null or empty");
    }

    if (steps == null || steps.isEmpty()) {
      throw new IllegalArgumentException("Steps cannot be null or empty");
    }

    this.id = id;
    this.title = title;
    this.relatedFeature = relatedFeature;
    this.description = description;
    this.type = type;
    this.preconditions = preconditions;
    this.postconditions = postconditions;
    this.inputs = inputs;
    this.steps = steps;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public Long getRelatedFeature() {
    return relatedFeature;
  }

  public String getDescription() {
    return description;
  }

  public TestCaseType getType() {
    return type;
  }

  public String getPreconditions() {
    return preconditions;
  }

  public String getPostconditions() {
    return postconditions;
  }

  public String getInputs() {
    return inputs;
  }

  public String getSteps() {
    return steps;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setRelatedFeature(Long relatedFeature) {
    this.relatedFeature = relatedFeature;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setType(TestCaseType type) {
    this.type = type;
  }

  public void setPreconditions(String preconditions) {
    this.preconditions = preconditions;
  }

  public void setPostconditions(String postconditions) {
    this.postconditions = postconditions;
  }

  public void setInputs(String inputs) {
    this.inputs = inputs;
  }

  public void setSteps(String steps) {
    this.steps = steps;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    TestCaseVO that = (TestCaseVO) o;

    if (id != null ? !id.equals(that.id) : that.id != null)
      return false;
    if (title != null ? !title.equals(that.title) : that.title != null)
      return false;
    if (relatedFeature != null
        ? !relatedFeature.equals(that.relatedFeature)
        : that.relatedFeature != null)
      return false;
    if (description != null ? !description.equals(that.description) : that.description != null)
      return false;
    if (type != null ? !type.equals(that.type) : that.type != null)
      return false;
    if (preconditions != null
        ? !preconditions.equals(that.preconditions)
        : that.preconditions != null)
      return false;
    if (postconditions != null
        ? !postconditions.equals(that.postconditions)
        : that.postconditions != null)
      return false;
    if (inputs != null ? !inputs.equals(that.inputs) : that.inputs != null)
      return false;
    return steps != null ? steps.equals(that.steps) : that.steps == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (relatedFeature != null ? relatedFeature.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (preconditions != null ? preconditions.hashCode() : 0);
    result = 31 * result + (postconditions != null ? postconditions.hashCode() : 0);
    result = 31 * result + (inputs != null ? inputs.hashCode() : 0);
    result = 31 * result + (steps != null ? steps.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "TestCaseVO{"
        + "id="
        + id
        + ", title='"
        + title
        + '\''
        + ", relatedFeature="
        + relatedFeature
        + ", description='"
        + description
        + '\''
        + ", type="
        + type
        + ", preconditions='"
        + preconditions
        + '\''
        + ", postconditions='"
        + postconditions
        + '\''
        + ", inputs='"
        + inputs
        + '\''
        + ", steps="
        + steps
        + '}';
  }
}
