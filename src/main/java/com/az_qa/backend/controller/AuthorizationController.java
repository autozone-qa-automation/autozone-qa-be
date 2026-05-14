package com.az_qa.backend.controller;

import com.az_qa.backend.security.JPADetailsUserService;
import com.az_qa.backend.security.JwtService;
import com.az_qa.backend.vo.CredentialsVO;
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

  @Autowired JwtService jwtService;

  @PostMapping("/api/v1/authentify") // Recibe email y password sin hashear
  public String autenticar(@RequestBody CredentialsVO credenciales) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            credenciales.getMail(), credenciales.getPassword()));

    final UserDetails detalleUsuario =
        detalleUsuariosService.loadUserByUsername(credenciales.getMail());
    return jwtService.generarToken(detalleUsuario.getUsername());
  }
}
