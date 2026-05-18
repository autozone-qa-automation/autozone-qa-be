package com.az_qa.backend.security;

import com.az_qa.backend.entity.UserEntity;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JPADetailsUser implements UserDetails {
  // UserDetails es una interfaz de Spring Security,
  // los nombres de los metodos que overrideamos no se pueden cambiar.

  private final UserEntity usuario;

  public JPADetailsUser(UserEntity usuario) {
    this.usuario = usuario;
  }

  @Override
  public String getUsername() {
    return usuario.getEmail();
  }

  @Override
  public String getPassword() {
    return usuario.getPassword();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    return List.of( // lista con el nombres del permiso
        new SimpleGrantedAuthority(usuario.getRole().getPermission().name()));
  }
}
