/*
 * Tecnológico de Monterrey — Campus Chihuahua
 * Desarrollo e Implantación de Sistemas de Software
 * TC3005B GPO500 - 2026
 * Autozone QA Automation
 */

package com.az_qa.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;
import org.springframework.data.domain.Persistable;

/**
 * Persistence entity that represents a URL record stored in the database.
 */
@Entity
@Table(name = "URLs")
public class UrlEntity implements Serializable, Persistable<Long> {

    /** Serializable version identifier. */
    private static final long serialVersionUID = 1739356800002L;

    /** Database identifier for the URL. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUrl")
    private Long idUrl;

    /** URL name. */
    @Column(name = "nombre")
    private String nombre;

    /** URL value. */
    @Column(name = "url")
    private String url;

    /** Service this URL belongs to. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idServicio")
    private ServicesEntity servicio;

    /** Entity new-state flag used by Spring Data persistence semantics. */
    @Transient
    private boolean isNew = false;

    /**
     * Creates an empty URL entity.
     */
    public UrlEntity() {
    }

    /**
     * Creates a URL entity with all supported fields.
     *
     * @param idUrl    URL identifier
     * @param nombre   URL name
     * @param url      URL value
     * @param servicio service this URL belongs to
     */
    public UrlEntity(Long idUrl, String nombre, String url, ServicesEntity servicio) {
        this.idUrl = idUrl;
        this.nombre = nombre;
        this.url = url;
        this.servicio = servicio;
    }

    /**
     * Returns the URL identifier.
     *
     * @return URL id
     */
    @Override
    public Long getId() {
        return idUrl;
    }

    /**
     * Indicates whether this entity should be treated as new by Spring Data.
     *
     * @return {@code true} when the entity is new; otherwise {@code false}
     */
    @Override
    public boolean isNew() {
        return isNew;
    }

    /**
     * Updates the new-state flag used by Spring Data.
     *
     * @param isNew {@code true} when the entity should be treated as new
     */
    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    /** Marks the entity as not new after it is persisted or loaded. */
    @PostPersist
    @PostLoad
    public void markNotNew() {
        this.isNew = false;
    }

    /**
     * Returns the URL identifier.
     *
     * @return URL id
     */
    public Long getIdUrl() {
        return idUrl;
    }

    /**
     * Sets the URL identifier.
     *
     * @param idUrl URL id
     */
    public void setIdUrl(Long idUrl) {
        this.idUrl = idUrl;
    }

    /**
     * Returns the URL name.
     *
     * @return URL name
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the URL name.
     *
     * @param nombre URL name
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Returns the URL value.
     *
     * @return URL value
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL value.
     *
     * @param url URL value
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Returns the service this URL belongs to.
     *
     * @return service entity
     */
    public ServicesEntity getServicio() {
        return servicio;
    }

    /**
     * Sets the service this URL belongs to.
     *
     * @param servicio service entity
     */
    public void setServicio(ServicesEntity servicio) {
        this.servicio = servicio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UrlEntity that = (UrlEntity) o;

        if (idUrl != null ? !idUrl.equals(that.idUrl) : that.idUrl != null)
            return false;
        if (nombre != null ? !nombre.equals(that.nombre) : that.nombre != null)
            return false;
        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        int result = idUrl != null ? idUrl.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UrlEntity{"
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
}