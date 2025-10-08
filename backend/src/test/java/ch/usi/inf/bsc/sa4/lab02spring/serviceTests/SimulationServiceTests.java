package ch.usi.inf.bsc.sa4.lab02spring.serviceTests;

import static ch.usi.inf.bsc.sa4.lab02spring.utils.CityGenerator.createNewCity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.usi.inf.bsc.sa4.lab02spring.model.*;
import ch.usi.inf.bsc.sa4.lab02spring.repository.SimulationRepository;
import ch.usi.inf.bsc.sa4.lab02spring.repository.UserRepository;
import ch.usi.inf.bsc.sa4.lab02spring.service.CityService;
import ch.usi.inf.bsc.sa4.lab02spring.service.SimulationService;
import ch.usi.inf.bsc.sa4.lab02spring.service.UserService;
import ch.usi.inf.bsc.sa4.lab02spring.utils.CityCreationParameters;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

@SpringBootTest
@DisplayName("Testing the Simulation service")
public class SimulationServiceTests {

  @Value("${simurbs.frontend.url}")
  private String frontendUrl;

  @Autowired private final CityService cityService;

  @Autowired
  public SimulationServiceTests(CityService cityService) {
    this.cityService = cityService;
  }

  @Autowired private UserService userService;
  @Autowired private SimulationService simulationService;
  @MockBean private SimulationRepository simulationRepository;
  @MockBean private UserRepository userRepository;

  private OAuth2AuthenticationToken mockToken;

  private OAuth2AuthenticationToken createMockOAuth2Token(
      String userId, String email, String name) {
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("sub", userId);
    attributes.put("email", email);
    attributes.put("name", name);
    OAuth2User mockOAuth2User = mock(OAuth2User.class);
    when(mockOAuth2User.getAttributes()).thenReturn(attributes);
    when(mockOAuth2User.getName()).thenReturn(userId);
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    return new OAuth2AuthenticationToken(mockOAuth2User, authorities, "client-id");
  }

  Money avgIncome = new Money(5000.0);
  Money commuterCost = new Money(30.0);
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
        new Simulation(avgIncome, commuterCost, name, user, id, creationDate, 0, null, false);

    this.mockToken =
        createMockOAuth2Token(
            user.getId(), user.getEmail(), user.getFirstName() + " " + user.getLastName());

    this.simulation.setPublic(true);
    List<Simulation> publicSimulations =
        Arrays.asList(
            new Simulation(
                    new Money(1000.0),
                    new Money(50.0),
                    "Test 1",
                    user,
                    ZonedDateTime.now(),
                    0,
                    null,
                    false)
                .setPublic(true),
            new Simulation(
                    new Money(2000.0),
                    new Money(100.0),
                    "Test 2",
                    user,
                    ZonedDateTime.now(),
                    0,
                    null,
                    false)
                .setPublic(true));
    Mockito.when(simulationRepository.findByUserIdAndIsPublic(user.getId(), true))
        .thenReturn(publicSimulations);
    Mockito.when(simulationRepository.findByUser(user.getId()))
        .thenReturn(Collections.singletonList(simulation));
  }

  @Test
  @DisplayName("test that the stored simulation gets stored correctly")
  public void testStoreSimulation() {
    when(simulationRepository.save(any(Simulation.class))).thenReturn(simulation);
    Simulation result = simulationService.storeSimulation(simulation);
    assertEquals(simulation, result);
  }

  @Test
  @DisplayName(
      "Method getSimulationById should return Simulation(5000, 30, 100000, London, ab123_s248sa,null,false) if there is a correlated simulation to the Id")
  public void testGetSimulationById() {
    User storedUser = userService.storeUser(user);
    Simulation expected =
        new Simulation(avgIncome, commuterCost, name, storedUser, creationDate, 0, null, false);
    City storedCity =
        cityService.createCity(
            new CityCreationParameters(avgIncome.getValue(), commuterCost.getValue()));
    expected.setSimulatedCity(storedCity);
    when(simulationRepository.findById(simulation.getId())).thenReturn(Optional.of(simulation));

    // Act
    Optional<Simulation> result = simulationService.getSimulationById(simulation.getId());

    // Assert
    assertTrue(result.isPresent());
    assertEquals(simulation, result.get());
  }

  @Test
  @DisplayName(
      "Method getSimulationById should return Optional.empty() if there is not a correlated simulation to the Id")
  public void testGetSimulationByIdNotExists() {
    // Arrange
    String invalidId = "-1";

    // Act
    Optional<Simulation> result = simulationService.getSimulationById(invalidId);

    // Assert
    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName(
      "deleteSimulationById should delete the simulation, identified by the Id and the city this simulation is based on")
  public void testDeleteSimulationById() {
    City storedCity =
        cityService.createCity(
            new CityCreationParameters(avgIncome.getValue(), commuterCost.getValue()));
    simulation.setSimulatedCity(storedCity);
    String simId = simulation.getId();
    when(simulationRepository.findById(simulation.getId())).thenReturn(Optional.of(simulation));

    assertEquals(HttpStatus.OK, simulationService.deleteSimulationById(simId, true));
    assertEquals(HttpStatus.OK, simulationService.deleteSimulationById(simId, false));
  }

  @Test
  @DisplayName(
      "Method deleteSimulationById should return HttpStatus.NOT_FOUND when simulation is not found")
  public void testDeleteSimulationByIdNotFound() {
    // Arrange
    String simId = "6634d2477d9264405731ccde";

    // Mocking behavior
    when(simulationRepository.findById(simId)).thenReturn(Optional.empty());

    // Act
    HttpStatus result = simulationService.deleteSimulationById(simId, false);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, result);
  }

  @Test
  @DisplayName(
      "Method deleteSimulationById should return HttpStatus.INTERNAL_SERVER_ERROR when an exception occurs")
  public void testDeleteSimulationByIdException() {
    // Arrange
    String simId = "6634d2477d9264405731ccde";

    // Mocking behavior
    when(simulationRepository.findById(simId))
        .thenThrow(new RuntimeException("SimulatedCity exception"));

    // Act
    HttpStatus result = simulationService.deleteSimulationById(simId, false);

    // Assert
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result);
  }

  @Test
  @DisplayName("Method getPublicSimulation should return the simulation if it exists and is public")
  public void testGetPublicSimulationSuccess() {

    when(simulationRepository.findByIdAndIsPublic(simulation.getId(), true))
        .thenReturn(Optional.of(simulation));

    Simulation actualSimulation = simulationService.getPublicSimulation(simulation.getId());

    assertEquals(
        simulation,
        actualSimulation,
        "The public simulation retrieved should match the stored simulation");
  }

  @Test
  @DisplayName(
      "getPublicSimulation throws NoSuchElementException if simulation is not found or not public")
  void testGetPublicSimulationThrowsException() {
    String id = "non-existent-id";
    when(simulationRepository.findByIdAndIsPublic(id, true)).thenReturn(Optional.empty());
    Exception exception =
        assertThrows(
            NoSuchElementException.class,
            () -> {
              simulationService.getPublicSimulation(id);
            },
            "No public simulation found with ID should be thrown");

    String expectedMessage = "No public simulation found with ID " + id;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  @DisplayName("getPublicSimulationsByUserId should return the list with all public simulations")
  public void getPublicSimulationsByUserId() {

    List<Simulation> result = simulationService.getPublicSimulationsByUserId(user.getId());

    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.stream().allMatch(Simulation::isPublic));
  }

  @Test
  @DisplayName("Method generatePublicLink should return the correct public link for a given ID")
  public void testGeneratePublicLink() {

    String expectedId = id;

    String expectedLink = frontendUrl + "/simulation/public/" + expectedId;
    String actualLink = simulationService.generatePublicLink(expectedId);

    assertEquals(expectedLink, actualLink);
  }

  @Test
  @DisplayName("incViews should increase the number of views by 1")
  public void testIncViews() {
    // Arrange
    simulation.setViews(5);

    // Act
    simulationService.incViews(simulation);
    assertEquals(6, simulation.getViews());
  }

  @Test
  @DisplayName(
      "Method averagePopulationSize should return the average population size for all the simulations a user has")
  public void testAveragePopulationSize() {
    // Arrange
    String userId = "320r248br";
    Simulation sim1 =
        new Simulation(
            new Money(5000.0),
            new Money(30.0),
            "London",
            null,
            ZonedDateTime.now(),
            0,
            null,
            false);
    Simulation sim2 =
        new Simulation(
            new Money(6000.0),
            new Money(40.0),
            "New York",
            null,
            ZonedDateTime.now(),
            0,
            null,
            false);
    sim1.setSimulatedCity(createNewCity(new CityCreationParameters(6000.0, 200.0)));
    sim2.setSimulatedCity(createNewCity(new CityCreationParameters(5000.0, 150.0)));

    List<Simulation> simulations = Arrays.asList(sim1, sim2);
    when(simulationRepository.findByUserIdAndIsOldIsFalse(userId)).thenReturn(simulations);

    // Act
    double average = simulationService.averagePopulationSize(userId);

    // Assert
    assertEquals(364107.5, average);
  }

  @Test
  @DisplayName("Method averageIncome should return 0 because the List of simulations is empty")
  public void testAverageIncomePopulationSizeEmptyList() {
    // Arrange
    String userId = "987dj26d";
    List<Simulation> simulations = new ArrayList<>();
    when(simulationRepository.findByUserIdAndIsOldIsFalse(userId)).thenReturn(simulations);
    double average = simulationService.averagePopulationSize(userId);
    assertEquals(0, average);
  }

  @Test
  @DisplayName(
      "Method averageIncome should return the average income for all the simulations a user has")
  public void testAverageIncome() {
    // Arrange
    String userId = "9ygf2gd";
    List<Simulation> simulations =
        Arrays.asList(
            new Simulation(
                new Money(5000.0),
                new Money(30.0),
                "London",
                null,
                ZonedDateTime.now(),
                0,
                null,
                false),
            new Simulation(
                new Money(6000.0),
                new Money(40.0),
                "New York",
                null,
                ZonedDateTime.now(),
                0,
                null,
                false));
    when(simulationRepository.findByUserIdAndIsOldIsFalse(userId)).thenReturn(simulations);

    // Act
    double average = simulationService.averageIncome(userId);

    // Assert
    assertEquals(5500, average);
  }

  @Test
  @DisplayName("Method averageIncome should return 0 because the List of simulations is empty")
  public void testAverageIncomeIs0() {
    // Arrange
    String userId = "987dj26d";
    List<Simulation> simulations = new ArrayList<>();
    when(simulationRepository.findByUserIdAndIsOldIsFalse(userId)).thenReturn(simulations);
    double average = simulationService.averageIncome(userId);
    assertEquals(0, average);
  }

  @Test
  @DisplayName(
      "Method averageCommuter should return the average commuter cost for all the simulations a user has")
  public void testAverageCommuter() {
    // Arrange
    String userId = "987dj26d";
    List<Simulation> simulations =
        Arrays.asList(
            new Simulation(
                new Money(5000.0),
                new Money(30.0),
                "London",
                null,
                ZonedDateTime.now(),
                0,
                null,
                false),
            new Simulation(
                new Money(6000.0),
                new Money(40.0),
                "New York",
                null,
                ZonedDateTime.now(),
                0,
                null,
                false));
    when(simulationRepository.findByUserIdAndIsOldIsFalse(userId)).thenReturn(simulations);

    // Act
    double average = simulationService.averageCommuter(userId);

    // Assert
    assertEquals(35, average);
  }

  @Test
  @DisplayName("Method averageCommuter should return 0 because the List of simulations is empty")
  public void testAverageCommuterIs0() {
    // Arrange
    String userId = "987dj26d";
    List<Simulation> simulations = new ArrayList<>();
    when(simulationRepository.findByUserIdAndIsOldIsFalse(userId)).thenReturn(simulations);
    double average = simulationService.averageCommuter(userId);
    assertEquals(0, average);
  }

  @Test
  @DisplayName("isPublic field should switch between true and false to set the privacy")
  public void testToggleSimulationPrivacy() {
    // Arrange
    String simId = "sim123";
    Simulation mockSimulation =
        new Simulation(
            new Money(5000.0),
            new Money(30.0),
            "London",
            null,
            ZonedDateTime.now(),
            0,
            null,
            false);
    when(simulationRepository.findById(simId)).thenReturn(Optional.of(mockSimulation));
    simulationService.toggleSimulationPrivacy(simId);
    assertTrue(mockSimulation.isPublic()); // Asserting that the privacy has been toggled
  }

  @Test
  @DisplayName("isPublic field should switch between true and false to set the privacy")
  public void testToggleSimulationPrivacyToTrue() {
    // Arrange
    String simId = "sim123";
    Simulation mockSimulation =
        new Simulation(
            new Money(5000.0),
            new Money(30.0),
            "London",
            null,
            ZonedDateTime.now(),
            0,
            null,
            false);
    mockSimulation.setPublic(true);
    when(simulationRepository.findById(simId)).thenReturn(Optional.of(mockSimulation));
    simulationService.toggleSimulationPrivacy(simId);
    assertFalse(mockSimulation.isPublic());
  }

  @Test
  @DisplayName("Countsimulationbyuser should return the number of simulations that a user has")
  public void testCountSimulationsByUser() {
    String id = "345ac22rt";
    String firstName = "Giorgia";
    String lastName = "Lillo";
    String email = "lillog@usi.ch";
    User user = new User(id, firstName, lastName, email);

    String simId = "sim123";
    Simulation mockSimulation =
        new Simulation(
            new Money(5000.0),
            new Money(30.0),
            "London",
            null,
            ZonedDateTime.now(),
            0,
            null,
            false);
    when(simulationRepository.countByUserAndIsOldIsFalse(user.getId())).thenReturn(1L);
    Long simulationNumber = simulationService.countSimulationsByUser(id);
    assertEquals(1, simulationNumber);
  }

  @Test
  @DisplayName(
      "FindbyuserIdAndIsOlsIsFalse method returns a list of simulations of a specific user")
  public void testFindByUserIdAndIsOldIsFalse() {
    String id = "345ac22rt";
    String firstName = "Giorgia";
    String lastName = "Lillo";
    String email = "lillog@usi.ch";
    User user = new User(id, firstName, lastName, email);

    String simId = "sim123";
    Simulation mockSimulation =
        new Simulation(
            new Money(5000.0),
            new Money(30.0),
            "London",
            null,
            ZonedDateTime.now(),
            0,
            null,
            false);
    List<Simulation> simulations = new ArrayList<>();
    simulations.add(mockSimulation);
    when(simulationRepository.findByUserIdAndIsOldIsFalse(user.getId())).thenReturn(simulations);
    simulations = simulationService.findByUserIdAndIsOldIsFalse(user.getId());
    assertEquals(1, simulations.size());
  }

  @Test
  @DisplayName("findByUser method returns a list of simulations of a specific user")
  public void testFindByUser() {
    String id = "345ac22rt";
    String firstName = "Giorgia";
    String lastName = "Lillo";
    String email = "lillog@usi.ch";
    User user = new User(id, firstName, lastName, email);

    String simId = "sim123";
    Simulation mockSimulation =
        new Simulation(
            new Money(5000.0),
            new Money(30.0),
            "London",
            null,
            ZonedDateTime.now(),
            0,
            null,
            false);
    List<Simulation> simulations = new ArrayList<>();
    simulations.add(mockSimulation);
    when(simulationRepository.findByUser(user.getId())).thenReturn(simulations);
    simulations = simulationService.findByUser(user.getId());
    assertEquals(1, simulations.size());
  }
}
