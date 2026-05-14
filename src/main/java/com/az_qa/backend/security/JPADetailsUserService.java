package com.az_qa.backend.security;

import com.az_qa.backend.entity.UserEntity;
import com.az_qa.backend.repository.UsersRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JPADetailsUserService implements UserDetailsService {
  @Autowired UsersRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String emailUsuario) throws UsernameNotFoundException {
    // el email se trata como username aqui, por eso el nombre de las excepciones
    Optional<UserEntity> usuario = userRepository.findByEmail(emailUsuario);

    if (usuario.isPresent()) {
      return new JPADetailsUser(usuario.get());
    } else {
      throw new UsernameNotFoundException("Usuario no encontrado");
    }
  }
}
