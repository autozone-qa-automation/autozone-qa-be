package com.az_qa.backend.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

  @Mock private JwtService jwtService;

  @Mock private ApplicationContext contextoAplicacion;

  @Mock private JPADetailsUserService jpaDetailsUserService;

  @Mock private FilterChain filterChain;

  @InjectMocks private JwtFilter jwtFilter;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();

    SecurityContextHolder.clearContext();
  }

  @Test
  void shouldContinueWhenAuthorizationHeaderIsMissing() throws Exception {

    jwtFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    verifyNoInteractions(jwtService);
  }

  @Test
  void shouldContinueWhenAuthorizationHeaderDoesNotStartWithBearer() throws Exception {

    request.addHeader("Authorization", "token123");

    jwtFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    verifyNoInteractions(jwtService);
  }

  @Test
  void shouldAuthenticateUserWhenTokenIsValid() throws Exception {

    UserDetails user = new User("test@test.com", "password", java.util.Collections.emptyList());

    request.addHeader("Authorization", "Bearer valid-token");

    when(contextoAplicacion.getBean(JPADetailsUserService.class)).thenReturn(jpaDetailsUserService);

    when(jwtService.extraerMailUsuario("valid-token")).thenReturn("test@test.com");

    when(jpaDetailsUserService.loadUserByUsername("test@test.com")).thenReturn(user);

    when(jwtService.validarToken("valid-token", user)).thenReturn(true);

    jwtFilter.doFilterInternal(request, response, filterChain);

    assertNotNull(SecurityContextHolder.getContext().getAuthentication());

    verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldNotAuthenticateWhenTokenIsInvalid() throws Exception {

    UserDetails user = new User("test@test.com", "password", java.util.Collections.emptyList());

    request.addHeader("Authorization", "Bearer invalid-token");

    when(contextoAplicacion.getBean(JPADetailsUserService.class)).thenReturn(jpaDetailsUserService);

    when(jwtService.extraerMailUsuario("invalid-token")).thenReturn("test@test.com");

    when(jpaDetailsUserService.loadUserByUsername("test@test.com")).thenReturn(user);

    when(jwtService.validarToken("invalid-token", user)).thenReturn(false);

    jwtFilter.doFilterInternal(request, response, filterChain);

    assertNull(SecurityContextHolder.getContext().getAuthentication());

    verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldSkipAuthenticationWhenAlreadyAuthenticated() throws Exception {

    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken("existing", null));

    request.addHeader("Authorization", "Bearer token");

    when(jwtService.extraerMailUsuario("token")).thenReturn("test@test.com");

    jwtFilter.doFilterInternal(request, response, filterChain);

    verify(jpaDetailsUserService, never()).loadUserByUsername(anyString());

    verify(filterChain).doFilter(request, response);
  }
}
