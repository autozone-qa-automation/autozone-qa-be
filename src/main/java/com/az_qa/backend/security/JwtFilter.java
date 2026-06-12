package com.az_qa.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filtro de seguridad personalizado para la interceptación y validación de tokens JWT.
 * Hereda de {@link OncePerRequestFilter} para garantizar una única ejecución por cada
 * solicitud HTTP entrante. Se encarga de extraer el token del encabezado {@code Authorization},
 * validar su integridad y vigencia, y establecer la autenticación en el contexto de seguridad
 * de Spring Security ({@link SecurityContextHolder}) si el token es válido.
 *
 * @author az_qa
 * @version 1.0
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

  /**
   * Prefijo estándar requerido para identificar los tokens de tipo Bearer en el encabezado.
   */
  private static final String PREFIJO_TOKEN_JW_STRING = "Bearer "; // es con Bearer yippeee!

  /**
   * Nombre del encabezado HTTP estándar que transporta las credenciales de autenticación.
   */
  private static final String NOMBRE_ENCABEZADO_AUTORIZACION = "Authorization";

  /**
   * Servicio especializado para la decodificación, extracción y validación de los tokens JSON Web Tokens.
   */
  @Autowired private JwtService jwtService;

  /**
   * Contexto de la aplicación de Spring utilizado para la resolución perezosa (lazy) de Beans.
   */
  @Autowired private ApplicationContext contextoAplicacion;

  /**
   * Recupera dinámicamente la instancia del servicio de detalles de usuario desde el contexto.
   * Se utiliza este enfoque bajo demanda para mitigar y evitar problemas potenciales de
   * dependencia circular durante la inicialización temprana del contexto de seguridad de Spring.
   *
   * @return La instancia registrada de {@link JPADetailsUserService}.
   */
  private JPADetailsUserService getJPADetalleUsuariosService() {
    return contextoAplicacion.getBean(JPADetailsUserService.class);
  }

  /**
   * Intercepta la solicitud HTTP, extrae el token JWT si está presente y autentica al usuario.
   * El flujo operativo interno sigue los siguientes pasos:
   * Busca el encabezado {@code Authorization} y verifica que inicie con {@code "Bearer "}.
   * Aísla la cadena del token y extrae el correo electrónico (subject) del usuario.
   * Si el usuario es extraído con éxito y no existe una autenticación previa en el hilo actual,
   * se cargan los detalles del usuario desde la base de datos a través de {@link JPADetailsUserService}.
   * Valida formalmente el token contra los detalles del usuario.
   * Si es plenamente válido, construye un {@link UsernamePasswordAuthenticationToken},
   * le asocia los detalles de la solicitud HTTP y lo inyecta en el {@link SecurityContextHolder}
   * Finalmente, cede el control al siguiente eslabón de la cadena de filtros ({@link FilterChain}).
   *
   * @param request El objeto {@link HttpServletRequest} que contiene la información de la petición HTTP.
   * @param response El objeto {@link HttpServletResponse} para gestionar la respuesta HTTP.
   * @param filterChain El mecanismo {@link FilterChain} para invocar el resto de los filtros en la cadena.
   * @throws ServletException Si ocurre un error interno propio del contenedor de servlets.
   * @throws IOException Si ocurre un error de entrada o salida en la lectura/escritura de los flujos de la petición.
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String encabezadoAutorizacion = request.getHeader(NOMBRE_ENCABEZADO_AUTORIZACION);
    String token = null;
    String mailUsuario = null;

    if (encabezadoAutorizacion != null
        && encabezadoAutorizacion.startsWith(PREFIJO_TOKEN_JW_STRING)) {
      token = encabezadoAutorizacion.substring(PREFIJO_TOKEN_JW_STRING.length());
      mailUsuario = jwtService.extraerMailUsuario(token); // Devolverá null de forma segura si falla
    }

    if (mailUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails detallesUsuario = getJPADetalleUsuariosService().loadUserByUsername(mailUsuario);

      if (jwtService.validarToken(token, detallesUsuario)) {
        UsernamePasswordAuthenticationToken tokenAutenticado =
            new UsernamePasswordAuthenticationToken(
                detallesUsuario, null, detallesUsuario.getAuthorities());
        tokenAutenticado.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(tokenAutenticado);
      }
    }

    filterChain.doFilter(request, response);
  }
}
