package com.az_qa.backend.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final String PREFIJO_TOKEN_JW_STRING = "Bearer "; //es con Bearer yippeee!
    private static final String NOMBRE_ENCABEZADO_AUTORIZACION = "Authorization";

    @Autowired private JwtService jwtService;
    @Autowired private ApplicationContext contextoAplicacion;

    private JPADetailsUserService getJPADetalleUsuariosService() {
        return contextoAplicacion.getBean(JPADetailsUserService.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String encabezadoAutorizacion = request.getHeader(NOMBRE_ENCABEZADO_AUTORIZACION);
        String token = null;
        String mailUsuario = null;

        if (encabezadoAutorizacion != null && encabezadoAutorizacion.startsWith(PREFIJO_TOKEN_JW_STRING)) {
            token = encabezadoAutorizacion.substring(PREFIJO_TOKEN_JW_STRING.length());
            mailUsuario = jwtService.extraerMailUsuario(token);
        }

        if (mailUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails detallesUsuario = getJPADetalleUsuariosService().loadUserByUsername(mailUsuario);

            if (jwtService.validarToken(token, detallesUsuario)) {
                UsernamePasswordAuthenticationToken tokenAutenticado = new UsernamePasswordAuthenticationToken(detallesUsuario,
                        null, detallesUsuario.getAuthorities());
                tokenAutenticado.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(tokenAutenticado);
            }
        }

        filterChain.doFilter(request, response);
    }
}
