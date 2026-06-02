/*
 * Tecnológico de Monterrey — Campus Chihuahua
 * Desarrollo e Implantación de Sistemas de Software
 * TC3005B GPO500 - 2026
 * Autozone QA Automation
 */

package com.az_qa.backend.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public class ServicesVO {

  private Long id;

  @NotBlank(message = "Service name is required")
  @Size(max = 100, message = "Service name must be at most 100 characters")
  private String name;

  private String description;

  private List<UrlVO> urls;

  public ServicesVO() {}

  public ServicesVO(Long id, String name, String description, List<UrlVO> urls) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.urls = urls;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<UrlVO> getUrls() {
    return urls;
  }

  public void setUrls(List<UrlVO> urls) {
    this.urls = urls;
  }

  @Override
  public String toString() {
    return "ServicesVO{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", description='"
        + description
        + '\''
        + ", urls="
        + urls
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ServicesVO that = (ServicesVO) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    if (description != null ? !description.equals(that.description) : that.description != null)
      return false;
    return urls != null ? urls.equals(that.urls) : that.urls == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (urls != null ? urls.hashCode() : 0);
    return result;
  }
}
