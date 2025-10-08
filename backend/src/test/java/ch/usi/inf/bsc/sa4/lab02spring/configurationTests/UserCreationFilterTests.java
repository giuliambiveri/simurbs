package ch.usi.inf.bsc.sa4.lab02spring.configurationTests;

import static org.mockito.Mockito.*;

import ch.usi.inf.bsc.sa4.lab02spring.configuration.UserCreationFilter;
import ch.usi.inf.bsc.sa4.lab02spring.model.User;
import ch.usi.inf.bsc.sa4.lab02spring.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

@ExtendWith(MockitoExtension.class)
public class UserCreationFilterTests {

  @Mock private UserService userService;

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @Mock private FilterChain filterChain;

  @Mock private SecurityContext securityContext;

  @Mock private Authentication authentication;

  @Mock private OAuth2AuthenticatedPrincipal principal;

  @InjectMocks private UserCreationFilter userCreationFilter;

  @BeforeEach
  public void setup() {
    SecurityContextHolder.setContext(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(principal);
    when(principal.getAttribute("sub")).thenReturn("peach");
  }

  @Test
  @DisplayName("Create new user if ID doesn't exist")
  public void testUserCreationIfNotExist() throws Exception {
    when(principal.getAttribute("given_name")).thenReturn("Peach");
    when(principal.getAttribute("family_name")).thenReturn("Liu");
    when(principal.getAttribute("email")).thenReturn("peach.liu@usi.ch");
    when(userService.getById("peach")).thenReturn(Optional.empty());

    userCreationFilter.doFilter(request, response, filterChain);

    verify(userService).storeUser(any(User.class));
    verify(filterChain).doFilter(request, response);
  }

  @Test
  @DisplayName("Should not create user if already exists")
  void testNotUserCreationIfExist() throws Exception {
    User newUser = new User("peach", "Peach", "Liu", "peach.liu@usi.ch");
    when(userService.getById("peach")).thenReturn(Optional.of(newUser));

    userCreationFilter.doFilter(request, response, filterChain);

    verify(userService, never()).storeUser(any(User.class));
    verify(filterChain).doFilter(request, response);
  }
}
