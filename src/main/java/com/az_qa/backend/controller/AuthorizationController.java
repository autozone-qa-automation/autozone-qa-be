package com.az_qa.backend.controller;

import com.az_qa.backend.mapper.UserMapper;
import com.az_qa.backend.repository.UsersRepository;
import com.az_qa.backend.security.JPADetailsUserService;
import com.az_qa.backend.security.JwtService;
import com.az_qa.backend.vo.AuthResponseVO;
import com.az_qa.backend.vo.CredentialsVO;
import com.az_qa.backend.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {
  @Autowired AuthenticationManager authenticationManager;

  @Autowired JPADetailsUserService detalleUsuariosService;

  @Autowired UsersRepository usersRepository;

  @Autowired JwtService jwtService;

  @PostMapping("/api/v1/authentify") // Recibe email y password sin hashear
  public AuthResponseVO autenticar(@RequestBody CredentialsVO credenciales) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            credenciales.getMail(), credenciales.getPassword()));

    final UserDetails detalleUsuario =
        detalleUsuariosService.loadUserByUsername(credenciales.getMail());
    String token = jwtService.generarToken(detalleUsuario.getUsername());

    UserVO user = UserMapper.toVO(usersRepository.findByEmail(credenciales.getMail()).orElse(null));
    if (user != null) {
      user.setPassword(null);
    }

    return new AuthResponseVO(token, user);
  }
}
