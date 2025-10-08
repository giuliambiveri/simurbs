package ch.usi.inf.bsc.sa4.lab02spring.controller;

import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.CheckAuthenticationDTO;
import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.UserDTO;
import ch.usi.inf.bsc.sa4.lab02spring.model.User;
import ch.usi.inf.bsc.sa4.lab02spring.service.SimulationService;
import ch.usi.inf.bsc.sa4.lab02spring.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The controller for users. */
@RestController
@RequestMapping("/users")
public class UserController {

  private final SimulationService simulationService;
  private final UserService userService;

  public UserController(UserService userService, SimulationService simulationService) {
    this.userService = userService;
    this.simulationService = simulationService;
  }

  /**
   * Returns the list of existing users.
   *
   * @return a list of existing users.
   */
  @GetMapping
  public ResponseEntity<List<UserDTO>> getAll() {
    final List<UserDTO> listUsersDTO = userService.getAll().stream().toList();
    return ResponseEntity.ok(listUsersDTO);
  }

  /**
   * Returns the logged-in user's data based on SWITCH EDU's profile
   *
   * @param authentication a token with the user's data and login information.
   * @return a 200 OK with the logged-in user's DTO.
   */
  @GetMapping("/profile")
  public ResponseEntity<UserDTO> index(OAuth2AuthenticationToken authentication) {
    final String id = authentication.getPrincipal().getAttribute("sub");
    final String firstName = authentication.getPrincipal().getAttribute("given_name");
    final String lastName = authentication.getPrincipal().getAttribute("family_name");
    final String email = authentication.getPrincipal().getAttribute("email");

    final double averageCommuter = simulationService.averageCommuter(id);
    final double averageIncome = simulationService.averageIncome(id);
    final double averagePopulation = simulationService.averagePopulationSize(id);
    final Long count = simulationService.countSimulationsByUser(id);

    return ResponseEntity.ok(
        new UserDTO(
            id,
            firstName,
            lastName,
            email,
            averageCommuter,
            averageIncome,
            averagePopulation,
            count));
  }

  /**
   * Returns a user's profile information with a given id
   *
   * @param id a path variable containing the user's id.
   * @return a 200 OK with the user DTO if the user exists, a 404 NOT FOUND otherwise.
   */
  @GetMapping("/{id}/profile")
  public ResponseEntity<UserDTO> getUserProfile(@PathVariable String id) {

    final double averageCommuter = simulationService.averageCommuter(id);
    final double averageIncome = simulationService.averageIncome(id);
    final double averagePopulation = simulationService.averagePopulationSize(id);
    final Long count = simulationService.countSimulationsByUser(id);

    Optional<User> userOptional = userService.getById(id);
    if (userOptional.isPresent()) {
      final UserDTO userDTO =
          new UserDTO(userOptional.get(), averageCommuter, averageIncome, averagePopulation, count);
      return ResponseEntity.ok(userDTO);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Returns an object that tells whether user requesting is logged in or not
   *
   * @param authentication a token with the current user's data and login information.
   * @return a 200 OK with a DTO telling whether the calling user is logged in or not.
   */
  @GetMapping("/check-authentication")
  public ResponseEntity<CheckAuthenticationDTO> checkAuthentication(Authentication authentication) {
    boolean isAuthenticated =
        authentication instanceof OAuth2AuthenticationToken && authentication.isAuthenticated();
    return ResponseEntity.ok(new CheckAuthenticationDTO(isAuthenticated));
  }
}
