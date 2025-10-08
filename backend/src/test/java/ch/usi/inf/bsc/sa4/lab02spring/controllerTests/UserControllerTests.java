package ch.usi.inf.bsc.sa4.lab02spring.controllerTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.usi.inf.bsc.sa4.lab02spring.controller.UserController;
import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.CheckAuthenticationDTO;
import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.UserDTO;
import ch.usi.inf.bsc.sa4.lab02spring.model.User;
import ch.usi.inf.bsc.sa4.lab02spring.repository.UserRepository;
import ch.usi.inf.bsc.sa4.lab02spring.service.SimulationService;
import ch.usi.inf.bsc.sa4.lab02spring.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

@DisplayName("In the User controller")
public class UserControllerTests {
  @Test
  @DisplayName("Method getUserProfile should return the profile of the user searched")
  public void testGetUserProfile() {
    String id = "3524djf3542";
    String firstName = "Giulia";
    String lastName = "Ambiveri";
    String email = "giuliambiveri@gmail.com";
    double first = 0.0;
    double second = 0.0;
    double third = 0.0;
    Long fourth = 0L;

    User result = new User(id, firstName, lastName, email);
    UserDTO expectedUserDTO = new UserDTO(result, first, second, third, fourth);

    UserService userService = mock(UserService.class);
    SimulationService simulationService = mock(SimulationService.class);
    when(userService.getById(id)).thenReturn(Optional.of(result));

    UserController userController = new UserController(userService, simulationService);
    ResponseEntity<UserDTO> responseEntity = userController.getUserProfile(id);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(expectedUserDTO, responseEntity.getBody());
  }

  @Test
  @DisplayName("Method getUserProfile should return 404 when the user does not exist")
  public void testGetUserProfileUserNotFound() {
    String id = "00000";

    UserService userService = mock(UserService.class);
    when(userService.getById(id)).thenReturn(Optional.empty());

    SimulationService simulationService = mock(SimulationService.class);
    UserController userController = new UserController(userService, simulationService);
    ResponseEntity<UserDTO> responseEntity = userController.getUserProfile(id);

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    assertNull(responseEntity.getBody());
  }

  @Test
  @DisplayName("Method index should return the profile of the authenticated user")
  public void testIndex() {
    String id = "2383dfah289";
    String firstName = "John";
    String lastName = "Travolta";
    String email = "john.travolta@gmail.com";

    double first = 0.0;
    double second = 0.0;
    double third = 0.0;
    Long fourth = 0L;

    User result = new User(id, firstName, lastName, email);
    UserDTO expectedUserDTO = new UserDTO(result, first, second, third, fourth);

    OAuth2User principal = mock(OAuth2User.class);
    when(principal.getAttribute("sub")).thenReturn(id);
    when(principal.getAttribute("given_name")).thenReturn(firstName);
    when(principal.getAttribute("family_name")).thenReturn(lastName);
    when(principal.getAttribute("email")).thenReturn(email);

    OAuth2AuthenticationToken authentication = mock(OAuth2AuthenticationToken.class);
    when(authentication.getPrincipal()).thenReturn(principal);

    UserService userService = mock(UserService.class);
    UserRepository userRepository = mock(UserRepository.class);
    when(userRepository.findById(id)).thenReturn(Optional.of(result));

    SimulationService simulationService = mock(SimulationService.class);
    UserController userController = new UserController(userService, simulationService);
    ResponseEntity<UserDTO> responseEntity = userController.index(authentication);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }

  @Test
  @DisplayName("The getAll method should return all the users")
  public void testGetAllUsers() {
    String id = "3524djf3542";
    String firstName = "Giulia";
    String lastName = "Ambiveri";
    String email = "giuliambiveri@gmail.com";

    String id1 = "387qh873";
    String firstName1 = "Alessandro";
    String lastName1 = "DellaFlora";
    String email1 = "AlessandroDellaFlora@gmail.com";

    String id2 = "8q63j038d9q";
    String firstName2 = "Lamberto";
    String lastName2 = "Ragnolini";
    String email2 = "Ragnolini@gmail.com";

    double first = 100.0;
    double second = 150.0;
    double third = 200.0;
    Long fourth = 300L;

    User user = new User(id, firstName, lastName, email);
    UserDTO userDTO = new UserDTO(user, first, second, third, fourth);

    User user1 = new User(id1, firstName1, lastName1, email1);
    UserDTO userDTO1 = new UserDTO(user1, first, second, third, fourth);

    User user2 = new User(id2, firstName2, lastName2, email2);
    UserDTO userDTO2 = new UserDTO(user2, first, second, third, fourth);

    List<User> resultList = new ArrayList<>();
    resultList.add(user);
    resultList.add(user1);
    resultList.add(user2);

    List<UserDTO> expectedList = new ArrayList<>();
    expectedList.add(userDTO);
    expectedList.add(userDTO1);
    expectedList.add(userDTO2);

    SimulationService simulationService = mock(SimulationService.class);
    UserService userService = mock(UserService.class);
    when(userService.getAll()).thenReturn(expectedList);

    UserController userController = new UserController(userService, simulationService);
    ResponseEntity<List<UserDTO>> responseEntity = userController.getAll();

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(expectedList, responseEntity.getBody());
  }

  @Test
  @DisplayName("Method getById should return the profile of the user with the specified Id")
  public void testCheckAuthentication() {
    String id = "3524djf3542";
    String firstName = "Giulia";
    String lastName = "Ambiveri";
    String email = "giuliambiveri@gmail.com";
    double first = 100.0;
    double second = 150.0;
    double third = 200.0;
    Long fourth = 300L;

    User result = new User(id, firstName, lastName, email);
    UserDTO expectedUserDTO = new UserDTO(result, first, second, third, fourth);

    OAuth2AuthenticationToken authentication = mock(OAuth2AuthenticationToken.class);

    SimulationService simulationService = mock(SimulationService.class);
    UserService userService = mock(UserService.class);

    UserController userController = new UserController(userService, simulationService);
    ResponseEntity<CheckAuthenticationDTO> responseEntity =
        userController.checkAuthentication(authentication);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }
}
