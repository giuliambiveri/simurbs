package ch.usi.inf.bsc.sa4.lab02spring.serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.UserDTO;
import ch.usi.inf.bsc.sa4.lab02spring.model.Money;
import ch.usi.inf.bsc.sa4.lab02spring.model.Simulation;
import ch.usi.inf.bsc.sa4.lab02spring.model.User;
import ch.usi.inf.bsc.sa4.lab02spring.repository.SimulationRepository;
import ch.usi.inf.bsc.sa4.lab02spring.service.SimulationService;
import ch.usi.inf.bsc.sa4.lab02spring.service.UserService;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@DisplayName("Testing the User service")
public class UserServiceTests {
  @Autowired private UserService userService;
  @Autowired private SimulationService simulationService;
  @MockBean private SimulationRepository simulationRepository;

  double avgIncome = 5000;
  double commuterCost = 30;
  Integer populationSize = 100000;
  String name = "London";
  String id = "ab123_s248sa";
  ZonedDateTime creationDate = ZonedDateTime.of(2015, 11, 30, 23, 45, 59, 1234, ZoneId.of("UTC+1"));

  private User user;
  private Simulation simulation;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    this.user = new User("b76-njda-23", "Lamberto", "Ragnolini", "lambertoragnolini@gmail.com");
    this.simulation =
        new Simulation(
            new Money(avgIncome),
            new Money(commuterCost),
            name,
            user,
            id,
            creationDate,
            0,
            null,
            false);
  }

  @Nested
  @DisplayName("Tests for storing and retrieving users")
  class StoreAndRetrieveUsersTests {

    private User newUser1;
    private User newUser2;
    private User newUser3;

    UserDTO userDTO1;
    UserDTO userDTO2;
    UserDTO userDTO3;

    @BeforeEach
    public void setUp() {
      newUser1 = new User("b70-njds-40", "Giacomo", "Verdi", "giacomoverdi99@gmail.com");
      newUser2 = new User("b70-njds-41", "Giacomino", "Rossi", "giacominorossi00@gmail.com");
      newUser3 = new User("b70-njds-42", "Giacomone", "Bianchi", "giacomonebianchi01@gmail.com");

      double first = 100.0;
      double second = 150.0;
      double third = 200.0;
      Long fourth = 300L;

      userService.storeUser(newUser1);
      userService.storeUser(newUser2);
      userService.storeUser(newUser3);

      userDTO1 = new UserDTO(newUser1, first, second, third, fourth);
      userDTO2 = new UserDTO(newUser2, first, second, third, fourth);
      userDTO3 = new UserDTO(newUser3, first, second, third, fourth);
    }

    @Test
    @DisplayName("List contains User 1")
    public void testListAllContainsUser1() {
      List<UserDTO> usersList = userService.getAll();
      assertNotNull(usersList);
    }

    @Test
    @DisplayName("List contains User 2")
    public void testListAllContainsUser2() {
      List<UserDTO> usersList = userService.getAll();
      assertNotNull(usersList);
    }

    @Test
    @DisplayName("List contains User 3")
    public void testListAllContainsUser3() {
      List<UserDTO> usersList = userService.getAll();
      assertNotNull(usersList);
    }
  }

  @Test
  @DisplayName("Testing storeUser function")
  public void testStoreUser() {
    User newUser1 = new User("b70-njds-40", "Giacomo", "Verdi", "giacomoverdi99@gmail.com");
    User newUser2 = userService.storeUser(newUser1);
    assertEquals(newUser1, newUser2);
  }
}
