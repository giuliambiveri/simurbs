package ch.usi.inf.bsc.sa4.lab02spring.configuration;

import ch.usi.inf.bsc.sa4.lab02spring.model.User;
import ch.usi.inf.bsc.sa4.lab02spring.service.UserService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

/**
 * Configuration class that enables web security and configures a custom filter to handle user
 * creation based on OAuth2 authentication information. The filter checks if a user, identified by
 * an OAuth2 authentication token, exists in the system. If the user does not exist, it is created
 * and stored using the provided user service.
 */
@Configuration
@EnableWebSecurity
public class UserCreationFilter implements Filter {
  private final UserService userService;

  /**
   * Constructs the UserCreationFilter with a user service. The user service is used to check for
   * existing users and to create new ones if necessary.
   *
   * @param userService the user service to handle user data operations.
   */
  public UserCreationFilter(UserService userService) {
    this.userService = userService;
  }

  /**
   * Processes incoming requests to check and create a user based on the authentication token. It
   * extracts the authentication information from the SecurityContext, checks if the user exists,
   * and creates a new user if they do not exist in the system.
   *
   * @param servletRequest the request object provided by the servlet container.
   * @param servletResponse the response object provided by the servlet container.
   * @param filterChain the filter chain provided by the servlet container, for passing control to
   *     the next filter.
   * @throws IOException if an I/O error occurs during request processing.
   * @throws ServletException if the request could not be handled.
   */
  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    final HttpServletRequest request = (HttpServletRequest) servletRequest;
    final HttpServletResponse response = (HttpServletResponse) servletResponse;

    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = null;
    if (context.getAuthentication() != null) {
      authentication = context.getAuthentication();
    }
    // Here the variable principal is defined inside the if statement
    if (authentication != null
        && authentication.isAuthenticated()
        && authentication.getPrincipal() instanceof OAuth2AuthenticatedPrincipal principal) {

      String userId = principal.getAttribute("sub");
      Optional<User> serviceResponse = userService.getById(userId);

      if (serviceResponse.isEmpty()) {
        String givenName = principal.getAttribute("given_name");
        String familyName = principal.getAttribute("family_name");
        String email = principal.getAttribute("email");
        User newUser = new User(userId, givenName, familyName, email);
        userService.storeUser(newUser);
      }
    }

    filterChain.doFilter(request, response);
  }
}
