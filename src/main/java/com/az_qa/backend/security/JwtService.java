package com.az_qa.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

  @Value("${JWT_KEY}")
  private String secretoJwt;

  public String generarToken(String emailUsuario) {
    Map<String, Object> concesiones = new HashMap<>();
    return Jwts.builder()
        .claims()
        .add(concesiones)
        .and()
        .subject(emailUsuario)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // dura 1 hora el token
        .signWith(generarLlaveDigital()) // usa la llave digital para firmar el token
        .compact();
  }

  private SecretKey generarLlaveDigital() {
    byte[] llave = Decoders.BASE64.decode(secretoJwt);
    return Keys.hmacShaKeyFor(llave);
  }

  public String extraerMailUsuario(String token) {
    return extraerConcesionEspecifica(token, Claims::getSubject);
  }

  public Date extraerExpiracion(String token) {
    return extraerConcesionEspecifica(token, Claims::getExpiration);
  }

  private <T> T extraerConcesionEspecifica(String token, Function<Claims, T> resolvedorConcesion) {
    final Claims concesiones = extraerConcesiones(token);
    return resolvedorConcesion.apply(concesiones);
  }

  private Claims extraerConcesiones(String token) {
    return Jwts.parser()
        .verifyWith(generarLlaveDigital())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public boolean haExpiradoToken(String token) {
    return extraerExpiracion(token).before(new Date());
  }

  public boolean validarToken(String token, UserDetails detallesUsuario) {
    final String nombreUsuario = extraerMailUsuario(token);
    return (nombreUsuario.equals(detallesUsuario.getUsername()) && !haExpiradoToken(token));
  }
}
