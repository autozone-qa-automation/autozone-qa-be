package com.az_qa.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Servicio encargado de la gestión integral de los JSON Web Tokens (JWT).
 * Provee las utilidades necesarias para la creación, firma, decodificación y
 * validación de tokens criptográficos de acceso. La clase se apoya en la especificación
 * de firma HMAC y centraliza el manejo de excepciones de seguridad derivadas de tokens
 * malformados, alterados o expirados.
 *
 * @author az_qa
 * @version 1.0
 */
@Component
public class JwtService {

  /**
   * Clave secreta codificada en Base64 inyectada desde las propiedades del entorno.
   * Se utiliza como la semilla criptográfica para firmar y verificar la autenticidad de los tokens.
   */
  @Value("${JWT_KEY}")
  private String secretoJwt;

  /**
   * Genera un token JWT firmado para un usuario específico utilizando su correo electrónico.
   * El token resultante posee las siguientes propiedades predeterminadas:
   * Subject (Sub): Establecido con el email del usuario.
   * Issued At (Iat): La fecha y hora exacta de emisión en el servidor.
   * Expiration (Exp):Configurado para expirar exactamente 1 hora (3600000 ms) después de su emisión.
   * Firma: Cifrado mediante el algoritmo HMAC derivado de la clave digital del sistema.
   *
   * @param emailUsuario El correo electrónico del usuario que actuará como identificador principal (Subject).
   * @return Una cadena de texto (String) compacta que representa el JWT estructurado.
   */
  public String generarToken(String emailUsuario) {
    Map<String, Object> condiciones = new HashMap<>();
    return Jwts.builder()
        .claims()
        .add(condiciones)
        .and()
        .subject(emailUsuario)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // dura 1 hora el token
        .signWith(generarLlaveDigital())
        .compact();
  }

  /**
   * Decodifica la clave secreta en Base64 y genera la clave criptográfica digital requerida.
   *
   * @return Una instancia de {@link SecretKey} válida para firmas HMAC.
   */
  private SecretKey generarLlaveDigital() {
    byte[] llave = Decoders.BASE64.decode(secretoJwt);
    return Keys.hmacShaKeyFor(llave);
  }

  /**
   * Extrae el correo electrónico (Subject) contenido en el cuerpo del token.
   *
   * @param token El token JWT del cual se desea extraer la información.
   * @return El correo electrónico del usuario, o {@code null} si el token no es válido o está expirado.
   */
  public String extraerMailUsuario(String token) {
    return extraerConcesionEspecifica(token, Claims::getSubject);
  }

  /**
   * Extrae la fecha de expiración del token especificado.
   *
   * @param token El token JWT del cual se desea extraer la información.
   * @return Un objeto {@link Date} con el tiempo de expiración, o {@code null} si ocurre un error de validación.
   */
  public Date extraerExpiracion(String token) {
    return extraerConcesionEspecifica(token, Claims::getExpiration);
  }

  /**
   * Método utilitario genérico que abstrae la extracción de cualquier propiedad (Claim) interna del token.
   *
   * @param <T> El tipo de dato esperado del valor de la propiedad a extraer.
   * @param token El token JWT objeto de análisis.
   * @param resolvedorConcesion Una función funcional de mapeo ({@link Function}) que indica qué Claim recuperar.
   * @return El valor mapeado de tipo {@code T}, o {@code null} si las concesiones no pudieron ser extraídas.
   */
  private <T> T extraerConcesionEspecifica(String token, Function<Claims, T> resolvedorConcesion) {
    final Claims concesiones = extraerConcesiones(token);
    if (concesiones == null) {
      return null;
    }
    return resolvedorConcesion.apply(concesiones);
  }

  /**
   * Realiza el parseo y validación estricta de la firma criptográfica del token para retornar su Payload (Claims).
   * Refuerzo de Seguridad:Este método intercepta de forma segura cualquier intento de alteración
   * atrapando excepciones críticas. Si la firma es inválida, el formato es erróneo o el token ya expiró,
   * el método no propaga el error ni interrumpe el flujo, sino que retorna {@code null}.
   *
   * @param token El token JWT crudo a evaluar.
   * @return El objeto {@link Claims} con los datos del token si es legítimo; de lo contrario {@code null}.
   */
  private Claims extraerConcesiones(String token) {
    try {
      return Jwts.parser()
          .verifyWith(generarLlaveDigital())
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (ExpiredJwtException
        | MalformedJwtException
        | SignatureException
        | IllegalArgumentException e) {
      return null;
    }
  }

  /**
   * Comprueba si el token proporcionado ha superado su tiempo de vida útil configurado.
   *
   * @param token El token JWT a verificar.
   * @return {@code true} si el token ha expirado o si es ilegible/inválido; {@code false} en caso contrario.
   */
  public boolean haExpiradoToken(String token) {
    Date expiracion = extraerExpiracion(token);
    if (expiracion == null) {
      return true;
    }
    return expiracion.before(new Date());
  }

  /**
   * Valida formalmente si un token es apto para autenticar las peticiones del usuario en evaluación.
   * Un token se considera completamente válido únicamente cuando:
   * El identificador extraído (email) no es nulo.
   * Coincide exactamente con el identificador registrado en los detalles del usuario ({@link UserDetails#getUsername()}).
   * El token no ha expirado cronológicamente ({@link #haExpiradoToken(String)}).
   * @param token El token JWT que presenta la solicitud HTTP.
   * @param detallesUsuario La información de identidad del usuario cargada desde el almacén de datos.
   * @return {@code true} si el token cumple todas las condiciones de autenticidad y vigencia; {@code false} de lo contrario.
   */
  public boolean validarToken(String token, UserDetails detallesUsuario) {
    final String nombreUsuario = extraerMailUsuario(token);
    if (nombreUsuario == null) {
      return false;
    }
    return (nombreUsuario.equals(detallesUsuario.getUsername()) && !haExpiradoToken(token));
  }
}
