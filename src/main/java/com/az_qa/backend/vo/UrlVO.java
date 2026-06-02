/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.vo;

public class UrlVO {

  private Long idUrl;
  private String nombre;
  private String url;

  public UrlVO() {}

  public UrlVO(Long idUrl, String nombre, String url) {
    this.idUrl = idUrl;
    this.nombre = nombre;
    this.url = url;
  }

  public Long getIdUrl() {
    return idUrl;
  }

  public void setIdUrl(Long idUrl) {
    this.idUrl = idUrl;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "UrlVO{"
        + "idUrl="
        + idUrl
        + ", nombre='"
        + nombre
        + '\''
        + ", url='"
        + url
        + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UrlVO that = (UrlVO) o;

    if (idUrl != null ? !idUrl.equals(that.idUrl) : that.idUrl != null) return false;
    if (nombre != null ? !nombre.equals(that.nombre) : that.nombre != null) return false;
    return url != null ? url.equals(that.url) : that.url == null;
  }

  @Override
  public int hashCode() {
    int result = idUrl != null ? idUrl.hashCode() : 0;
    result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
    result = 31 * result + (url != null ? url.hashCode() : 0);
    return result;
  }
}
