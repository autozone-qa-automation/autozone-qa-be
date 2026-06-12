package com.az_qa.backend.security;

import com.az_qa.backend.entity.UserEntity;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Adaptador que implementa la interfaz {@link UserDetails} de Spring Security.
 * Esta clase actúa como un puente entre la entidad de dominio orientada a la persistencia
 * ({@link UserEntity}) y el modelo de datos de usuario que Spring Security requiere
 * para llevar a cabo los procesos de autenticación y autorización.
 *
 * @author az_qa
 * @version 1.0
 */
public class JPADetailsUser implements UserDetails {

  /**
   * Instancia de la entidad de usuario de la base de datos.
   */
  private final UserEntity usuario;

  /**
   * Constructor único que inicializa el adaptador con la entidad del usuario autenticado.
   *
   * @param usuario La entidad {@link UserEntity} recuperada de la base de datos.
   */
  public JPADetailsUser(UserEntity usuario) {
    this.usuario = usuario;
  }

  /**
   * Devuelve el nombre de usuario utilizado para autenticar al usuario en el sistema.
   * En esta implementación, se utiliza la dirección de correo electrónico (email)
   * del usuario como su identificador único de inicio de sesión.
   *
   * @return El correo electrónico del usuario.
   */
  @Override
  public String getUsername() {
    return usuario.getEmail();
  }

  /**
   * Devuelve la contraseña cifrada utilizada para autenticar al usuario.
   *
   * @return La contraseña en formato hash guardada en la base de datos.
   */
  @Override
  public String getPassword() {
    return usuario.getPassword();
  }

  /**
   * Devuelve las autoridades y roles concedidos al usuario.
   * Este método realiza un mapeo desde el rol y permiso de la entidad hacia un objeto
   * {@link SimpleGrantedAuthority} con el prefijo {@code "ROLE_"} requerido por Spring Security,
   * de forma que las reglas {@code hasRole()} / {@code hasAnyRole()} funcionen correctamente.
   * Si el usuario no tiene un rol o permiso asignado en la base de datos,
   * se devuelve una lista vacía de forma segura para evitar excepciones de puntero nulo.
   *
   * @return Una colección de autoridades mapeadas que representan los roles del usuario.
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (usuario.getRole() == null || usuario.getRole().getPermission() == null) {
      return List.of();
    }

    String roleString = usuario.getRole().getPermission().name();

    return List.of(new SimpleGrantedAuthority("ROLE_" + roleString));
  }
}
