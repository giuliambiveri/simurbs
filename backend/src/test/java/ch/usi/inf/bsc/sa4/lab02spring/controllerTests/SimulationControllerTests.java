package ch.usi.inf.bsc.sa4.lab02spring.controllerTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.usi.inf.bsc.sa4.lab02spring.controller.SimulationController;
import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.CreateSimulationDTO;
import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.ListSimulationDTO;
import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.SimulationDTO;
import ch.usi.inf.bsc.sa4.lab02spring.model.City;
import ch.usi.inf.bsc.sa4.lab02spring.model.Money;
import ch.usi.inf.bsc.sa4.lab02spring.model.Simulation;
import ch.usi.inf.bsc.sa4.lab02spring.model.User;
import ch.usi.inf.bsc.sa4.lab02spring.service.CityService;
import ch.usi.inf.bsc.sa4.lab02spring.service.SimulationService;
import ch.usi.inf.bsc.sa4.lab02spring.utils.CityCreationParameters;
import ch.usi.inf.bsc.sa4.lab02spring.utils.OAuth2util;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

@SpringBootTest
@AutoConfigureMockMvc
public class SimulationControllerTests {

  @Value("${simurbs.frontend.url}")
  private String frontendUrl;

  @MockBean private SimulationService simulationService;

  @Autowired
  public SimulationControllerTests(CityService cityService) {
    this.cityService = cityService;
  }

  @Autowired private final CityService cityService;
  private SimulationController simulationController;

  private Simulation expectedResult;

  private User user;

  private OAuth2AuthenticationToken mockToken;
  private List<Simulation> listPublicSimulations;

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

  @BeforeEach
  public void setup() {
    // Setup test data
    Money avgIncome = new Money(5000.0);
    Money commuterCost = new Money(30.0);
    String name = "London";
    String userId = "ab123_s248sa";
    this.user = new User(userId, "Michelangelo", "Bettini", "bettim@usi.ch");

    // date is equal to 30/11/2015 23:45:59.1234 UTC+1
    ZonedDateTime creationDate =
        ZonedDateTime.of(2015, 11, 30, 23, 45, 59, 1234, ZoneId.of("UTC+1"));

    Integer views = 0;

    this.expectedResult =
        new Simulation(avgIncome, commuterCost, name, user, creationDate, views, null, false);
    CityCreationParameters parameters =
        new CityCreationParameters(avgIncome.getValue(), commuterCost.getValue());
    City storedCity = cityService.createCity(parameters);
    expectedResult.setSimulatedCity(storedCity);
    this.simulationController = new SimulationController(simulationService);
    this.mockToken =
        createMockOAuth2Token(
            user.getId(), user.getEmail(), user.getFirstName() + " " + user.getLastName());
    this.listPublicSimulations =
        Arrays.asList(
            new Simulation(
                    new Money(1000.0),
                    new Money(50.0),
                    "Test City 1",
                    user,
                    creationDate,
                    views,
                    null,
                    false)
                .setPublic(true),
            new Simulation(
                    new Money(2000.0),
                    new Money(100.0),
                    "Test City 2",
                    user,
                    creationDate,
                    views,
                    null,
                    false)
                .setPublic(true),
            new Simulation(
                    new Money(3000.0),
                    new Money(200.0),
                    "Test City 3",
                    user,
                    creationDate,
                    views,
                    null,
                    false)
                .setPublic(true));
    listPublicSimulations.forEach(
        simulation -> {
          City sCity =
              cityService.createCity(
                  new CityCreationParameters(
                      simulation.getAverageIncome().getValue(),
                      simulation.getCommuterCosts().getValue()));
          simulation.setSimulatedCity(sCity);
        });
    this.mockToken =
        createMockOAuth2Token(
            user.getId(), user.getEmail(), user.getFirstName() + " " + user.getLastName());
  }

  @Test
  @DisplayName("Method AddSimulation() should return the profile of the user searched")
  public void testAddSimulation() {
    // Mock CreateSimulationDTO
    CreateSimulationDTO newCreateSimulation =
        new CreateSimulationDTO(new Money(5000.0), new Money(500.0), "Florence", "");

    // Mock OAuth2AuthenticationToken
    OAuth2AuthenticationToken authentication = mock(OAuth2AuthenticationToken.class);

    // Mock SimulationService
    SimulationService simulationService = mock(SimulationService.class);

    User user = new User("727qn-72y", "Giovanni", "Abrile", "giovanniAbrile@gmail.com");

    // Mock the result
    Simulation simulationResult =
        new Simulation(
            new Money(5000.0),
            new Money(500.0),
            "Florence",
            user,
            ZonedDateTime.now(),
            0,
            null,
            false);
    City storedCity = cityService.createCity(new CityCreationParameters(5000.0, 500.0));
    simulationResult.setSimulatedCity(storedCity);

    // Mock the behavior of simulationService.createSimulation
    when(simulationService.createSimulation(
            eq(newCreateSimulation), any(OAuth2AuthenticationToken.class)))
        .thenReturn(simulationResult);

    // Create instance of the controller
    SimulationController simulationController = new SimulationController(simulationService);

    // Call the method to be tested
    ResponseEntity<SimulationDTO> responseEntity =
        simulationController.addSimulation(authentication, newCreateSimulation);

    // Assertions
    assertNotNull(responseEntity);
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
  }

  @Test
  @DisplayName("Method AddSimulation() should return status code 400 with invalid value(s)")
  public void testAddSimulationwithInvalidValues() {
    CreateSimulationDTO newCreateSimulation =
        new CreateSimulationDTO(new Money(0.0), new Money(500.0), "Florence", "");
    OAuth2AuthenticationToken authentication = mock(OAuth2AuthenticationToken.class);
    SimulationService simulationService = mock(SimulationService.class);
    // User user = new User("727qn-72y", "Giovanni", "Abrile", "giovanniAbrile@gmail.com");
    SimulationController simulationController = new SimulationController(simulationService);
    ResponseEntity<SimulationDTO> responseEntity =
        simulationController.addSimulation(authentication, newCreateSimulation);

    // Assertions
    assertNotNull(responseEntity);
    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    assertNull(responseEntity.getBody());
  }

  @Test
  @DisplayName(
      "Method getSimulation should return the simulation, given a correct Id with the client having "
          + "ownership over it")
  public void testGetSimulationSuccess() {
    // arrange/setup
    String simulationId = "0";
    when(simulationService.getSimulationById(simulationId)).thenReturn(Optional.of(expectedResult));
    when(OAuth2util.getUserID(mockToken)).thenReturn(user.getId());

    // act/exercise
    ResponseEntity<SimulationDTO> responseEntity =
        simulationController.getSimulation(mockToken, simulationId);

    // assert/verify
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals((new SimulationDTO(expectedResult)), responseEntity.getBody());
  }

  @Test
  @DisplayName(
      "Method getSimulation should not return the simulation, given an incorrect Id but with the "
          + "client having ownership over it")
  public void testGetSimulationNotFound() {
    // arrange/setup
    String simulationId = "-1";
    when(simulationService.getSimulationById(simulationId)).thenReturn(Optional.empty());
    when(OAuth2util.getUserID(mockToken)).thenReturn(user.getId());

    // act/exercise
    ResponseEntity<SimulationDTO> responseEntity =
        simulationController.getSimulation(mockToken, simulationId);

    // assert/verify
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

  @Test
  @DisplayName(
      "Method getSimulation should not return the simulation, given a correct Id but with the client "
          + "not having ownership over it")
  public void testGetSimulationUserNotOwner() {
    // arrange/setup
    User anotherUser =
        new User("abc12_3s248s", "Michelangelo", "Buonarroti", "michelangelobuonarroti@gmail.com");
    String simulationId = "0";
    when(simulationService.getSimulationById(simulationId)).thenReturn(Optional.of(expectedResult));
    when(OAuth2util.getUserID(mockToken)).thenReturn(anotherUser.getId());

    // act/exercise
    ResponseEntity<SimulationDTO> responseEntity =
        simulationController.getSimulation(mockToken, simulationId);

    // assert/verify
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

  @Test
  @DisplayName(
      "Method updateSimulation should return the updated city simulation as a new object of type simulation, it should not edit the current one")
  public void testUpdateSimulation() {
    // arrange/setup
    String simulationId = "0";
    CreateSimulationDTO createSimulationDTO =
        new CreateSimulationDTO(new Money(6134.0), new Money(354.0), "Varese", "");
    Simulation expected =
        new Simulation(
            new Money(6143.0),
            new Money(354.0),
            "Varese",
            user,
            ZonedDateTime.now(),
            0,
            null,
            false);
    City storedCity = cityService.createCity(new CityCreationParameters(6143.0, 354.0));
    expected.setSimulatedCity(storedCity);
    when(simulationService.getSimulationById(simulationId)).thenReturn(Optional.of(expectedResult));
    //    when(simulationService.storeSimulation(expectedResult)).thenReturn(expected);
    when(OAuth2util.getUserID(mockToken)).thenReturn(user.getId());
    when(simulationService.editSimulation(
            createSimulationDTO, expectedResult, mockToken, true, false))
        .thenReturn(expected);

    // act/exercise
    ResponseEntity<SimulationDTO> responseEntity =
        simulationController.updateSimulation(
            mockToken, simulationId, true, false, createSimulationDTO);

    // assert/verify
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals((new SimulationDTO(expected)), responseEntity.getBody());
  }

  @Test
  @DisplayName(
      "Method updateSimulation should not return the simulation, given an incorrect Id but with the client "
          + "having ownership over it")
  public void testUpdateSimulationNotFound() {
    // arrange/setup
    String simulationId = "-1";
    CreateSimulationDTO createSimulationDTO =
        new CreateSimulationDTO(new Money(3467.0), new Money(786.0), "Taverne", "");
    when(simulationService.getSimulationById(simulationId)).thenReturn(Optional.empty());
    when(OAuth2util.getUserID(mockToken)).thenReturn(user.getId());

    // act/exercise
    ResponseEntity<SimulationDTO> responseEntity =
        simulationController.updateSimulation(
            mockToken, simulationId, false, false, createSimulationDTO);

    // assert/verify
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

  @Test
  @DisplayName(
      "Method updateSimulation should not update the simulation, given a correct Id but with the client "
          + "not having ownership over it")
  public void testUpdateSimulationUserNotOwner() {
    // arrange/setup
    User anotherUser =
        new User("abc12_3s248s", "Michelangelo", "Buonarroti", "michelangelobuonarroti@gmail.com");
    String simulationId = "0";
    CreateSimulationDTO updateSimulationDTO =
        new CreateSimulationDTO(new Money(5000.0), new Money(100.0), "New City Name", "");
    when(simulationService.getSimulationById(simulationId)).thenReturn(Optional.of(expectedResult));
    when(OAuth2util.getUserID(mockToken)).thenReturn(anotherUser.getId());

    // act/exercise
    ResponseEntity<SimulationDTO> responseEntity =
        simulationController.updateSimulation(
            mockToken, simulationId, false, false, updateSimulationDTO);

    // assert/verify
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

  @Test
  @DisplayName(
      "Method updateSimulation should not return the simulation, given an invalid Simulation")
  public void testUpdateInvalidSimulation() {
    // arrange/setup
    String simulationId = "0";
    CreateSimulationDTO createSimulationDTO =
        new CreateSimulationDTO(new Money(3467.0), new Money(0.0), "Taverne", "");
    when(simulationService.getSimulationById(simulationId)).thenReturn(Optional.of(expectedResult));
    when(OAuth2util.getUserID(mockToken)).thenReturn(user.getId());

    // act/exercise
    ResponseEntity<SimulationDTO> responseEntity =
        simulationController.updateSimulation(
            mockToken, simulationId, false, false, createSimulationDTO);

    // assert/verify
    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
  }

  @Test
  @DisplayName(
      "Method getMySimulations should return a list of simulations owned by the authenticated user")
  public void testGetMySimulations() {
    List<Simulation> simulations =
        Arrays.asList(
            new Simulation(
                new Money(5000.0),
                new Money(500.0),
                "City1",
                user,
                ZonedDateTime.now(),
                0,
                null,
                false),
            new Simulation(
                new Money(6000.0),
                new Money(600.0),
                "City2",
                user,
                ZonedDateTime.now(),
                0,
                null,
                false));

    when(simulationService.findByUser(user.getId())).thenReturn(simulations);

    ResponseEntity<List<ListSimulationDTO>> responseEntity =
        simulationController.getMySimulations(mockToken);

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
  }

  @Test
  @DisplayName("Method toggleSimulation should toggle the privacy of a simulation")
  public void testToggleSimulation() {
    String simulationId = "0";
    Simulation expected = expectedResult;
    expected.setPublic(false);

    when(simulationService.getSimulationById(simulationId)).thenReturn(Optional.of(expected));
    when(simulationService.storeSimulation(expected)).thenReturn(expected);
    when(OAuth2util.getUserID(mockToken)).thenReturn(expected.getUser().getId());

    ResponseEntity<SimulationDTO> responseEntity =
        simulationController.toggleSimulation(mockToken, simulationId);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertTrue(responseEntity.getBody().isPublic());
  }

  @Test
  @DisplayName(
      "Method toggleSimulation should return HttpStatus.OK when user is authorized and the simulation exists")
  public void testToggleSimulation_WhenSimulationExistsAndUserIsAuthorized() {
    // Arrange
    String simulationId = "3524djf3542";
    User user =
        new User("abc12_3s248s", "Michelangelo", "Buonarroti", "michelangelobuonarroti@gmail.com");
    Simulation expectedSimulation =
        new Simulation(
            new Money(5000.0),
            new Money(500.0),
            "CityColo",
            user,
            ZonedDateTime.now(),
            0,
            null,
            false);
    CityCreationParameters params = new CityCreationParameters(5000.0, 500.0);
    City storedCity = cityService.createCity(params);
    expectedSimulation.setSimulatedCity(storedCity);

    expectedSimulation.setPublic(false); // Assuming initially private

    when(simulationService.getSimulationById(simulationId))
        .thenReturn(Optional.of(expectedSimulation));
    when(simulationService.storeSimulation(any())).thenReturn(expectedSimulation);
    when(OAuth2util.getUserID(mockToken))
        .thenReturn(user.getId()); // Assuming user.getId() returns the ID of the user object

    // Act
    ResponseEntity<SimulationDTO> responseEntity =
        simulationController.toggleSimulation(mockToken, simulationId);

    // Assert
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertTrue(responseEntity.getBody().isPublic()); // Assuming it toggles from private to public
  }

  @Test
  @DisplayName(
      "Method toggleSimulation should return HttpStatus.NOT_FOUND when the simulation doesn't exist")
  public void testToggleSimulation_WhenSimulationDoesNotExist() {
    // Arrange
    String simulationId = "non_existing_id";

    when(simulationService.getSimulationById(simulationId)).thenReturn(Optional.empty());

    // Act
    ResponseEntity<SimulationDTO> responseEntity =
        simulationController.toggleSimulation(mockToken, simulationId);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

  @Test
  @DisplayName(
      "Method toggleSimulation should return HttpStatus.NOT_FOUND when user is not authorized")
  public void testToggleSimulation_WhenUserIsNotAuthorized() {
    // Arrange
    String simulationId = "0";
    Simulation expectedSimulation =
        new Simulation(
            new Money(5000.0),
            new Money(500.0),
            "City1",
            user,
            ZonedDateTime.now(),
            0,
            null,
            false);
    expectedSimulation.setPublic(false); // Assuming initially private

    when(simulationService.getSimulationById(simulationId))
        .thenReturn(Optional.of(expectedSimulation));
    when(OAuth2util.getUserID(mockToken)).thenReturn("different_user_id");

    // Act
    ResponseEntity<SimulationDTO> responseEntity =
        simulationController.toggleSimulation(mockToken, simulationId);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

  @Test
  @DisplayName(
      "Method getPublicSimulation should return HttpStatus.NOT_FOUND when simulation is not found")
  public void testGetPublicSimulation_WhenSimulationNotFound() {
    // Arrange
    String simulationId = "non_existent_id";
    when(simulationService.getSimulationById(simulationId)).thenReturn(Optional.empty());

    // Act
    ResponseEntity<SimulationDTO> responseEntity =
        simulationController.getPublicSimulation(simulationId);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

  @Test
  @DisplayName(
      "Method getPublicSimulation should return HttpStatus.NOT_FOUND when simulation is not public")
  public void testGetPublicSimulation_WhenSimulationIsNotPublic() {
    // Arrange
    String simulationId = "3524djf3542";
    Simulation privateSimulation =
        new Simulation(
            new Money(5000.0),
            new Money(500.0),
            "City1",
            user,
            ZonedDateTime.now(),
            0,
            null,
            false);
    privateSimulation.setPublic(false);

    when(simulationService.getSimulationById(simulationId))
        .thenReturn(Optional.of(privateSimulation));

    // Act
    ResponseEntity<SimulationDTO> responseEntity =
        simulationController.getPublicSimulation(simulationId);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

  @Test
  @DisplayName(
      "Method getPublicSimulation should return HttpStatus.OK and return SimulationDTO when simulation is public")
  public void testGetPublicSimulation_WhenSimulationIsPublic() {
    // Arrange
    String simulationId = "3524djf3542";
    // Create a mock City object
    City city = new City(10, "City1", 10000L, 1000, new HashMap<>());
    Simulation publicSimulation =
        new Simulation(
            new Money(5000.0),
            new Money(500.0),
            "City1",
            user,
            ZonedDateTime.now(),
            0,
            null,
            false);
    publicSimulation.setPublic(true);
    publicSimulation.setSimulatedCity(city);

    when(simulationService.getSimulationById(simulationId))
        .thenReturn(Optional.of(publicSimulation));

    // Act
    ResponseEntity<SimulationDTO> responseEntity =
        simulationController.getPublicSimulation(simulationId);

    // Assert
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());

    // Verify that the returned SimulationDTO has the expected properties
    SimulationDTO simulationDTO = responseEntity.getBody();
    assertEquals(publicSimulation.getAverageIncome(), simulationDTO.averageIncome());
    assertEquals(publicSimulation.getCommuterCosts(), simulationDTO.commuterCosts());
    assertEquals(publicSimulation.getName(), simulationDTO.name());
    assertEquals(city, simulationDTO.city()); // Check if the City object matches
    assertEquals(publicSimulation.isPublic(), simulationDTO.isPublic());
    assertEquals(publicSimulation.getCreationDate(), simulationDTO.creationDate());
    assertEquals(publicSimulation.getViews(), simulationDTO.views());
    assertEquals(publicSimulation.getOldSimulation(), simulationDTO.oldSimulation());
  }

  @Test
  @DisplayName("Method makePublic successfully generates a public link")
  public void testMakePublicLinkSuccess() {

    String simulationId = "0";
    String expectedLink = frontendUrl + "/simulation/public/0";
    Simulation expected = expectedResult;
    when(simulationService.getPublicSimulation(simulationId)).thenReturn(expected);
    when(simulationService.generatePublicLink(simulationId)).thenReturn(expectedLink);

    ResponseEntity<?> response = simulationController.makePublic(simulationId);

    assertEquals(ResponseEntity.ok(Map.of("publicLink", expectedLink)), response);
  }

  @Test
  @DisplayName(
      "getAllPublicSimulations returns a list of SimulationDTOs when there are public simulations for the user")
  public void testGetAllPublicSimulationsNonEmptyList() {
    String userId = "ab123_s248sa";
    when(simulationService.getPublicSimulationsByUserId(userId)).thenReturn(listPublicSimulations);

    ResponseEntity<List<SimulationDTO>> response =
        simulationController.getAllPublicSimulations(userId);

    assertNotNull(response.getBody());
    assertEquals(3, response.getBody().size());
  }

  @Test
  @DisplayName(
      "getAllPublicSimulations returns an empty list when there are no public simulations for the user")
  public void testGetAllPublicSimulationsEmptyList() {

    String userId = "ab123_s248sa";
    when(simulationService.getPublicSimulationsByUserId(userId))
        .thenReturn(Collections.emptyList());

    ResponseEntity<List<SimulationDTO>> response =
        simulationController.getAllPublicSimulations(userId);

    assertNotNull(response.getBody());
    assertTrue(response.getBody().isEmpty());
  }

  @Test
  @DisplayName("makePublic returns not found for invalid simulation ID")
  public void testMakePublicNotFound() {

    String invalidId = "-1";
    when(simulationService.getPublicSimulation(invalidId)).thenThrow(new NoSuchElementException());

    ResponseEntity<?> response = simulationController.makePublic(invalidId);

    assertEquals(ResponseEntity.notFound().build(), response);
  }

  @Test
  @DisplayName(
      "Method deleteSimulation should return HttpStatus.NOT_FOUND when simulation does not exist")
  public void testDeleteSimulation_WhenSimulationDoesNotExist() {
    // Arrange
    String simulationId = "0";
    when(simulationService.getSimulationById(simulationId)).thenReturn(Optional.empty());

    // Act
    ResponseEntity<?> responseEntity =
        simulationController.deleteSimulation(mockToken, simulationId);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    // No need to verify deleteSimulationById was not called, since we're mocking that it returns
    // empty Optional
  }

  @Test
  @DisplayName(
      "Method deleteSimulation should return HttpStatus.NOT_FOUND when user is not authorized")
  public void testDeleteSimulation_WhenUserIsNotAuthorized() {
    // Arrange
    String simulationId = "0";
    Simulation expectedSimulation =
        new Simulation(
            new Money(5000.0),
            new Money(500.0),
            "City1",
            user,
            ZonedDateTime.now(),
            0,
            null,
            false);
    expectedSimulation.setPublic(false); // Assuming initially private

    when(simulationService.getSimulationById(simulationId))
        .thenReturn(Optional.of(expectedSimulation));
    when(OAuth2util.getUserID(mockToken)).thenReturn("different_user_id");

    // Act
    ResponseEntity<?> responseEntity =
        simulationController.deleteSimulation(mockToken, simulationId);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    // No need to verify deleteSimulationById was not called, since user is not authorized
  }

  @Test
  @DisplayName(
      "Method deleteSimulation should delete simulation and return HttpStatus.OK when user is authorized")
  public void testDeleteSimulation_WhenUserIsAuthorized() {
    // Arrange
    String simulationId = "3524djf3542";
    User user =
        new User("abc12_3s248s", "Michelangelo", "Buonarroti", "michelangelobuonarroti@gmail.com");
    String userId = "abc12_3s248s";
    Simulation expectedSimulation =
        new Simulation(
            new Money(5000.0),
            new Money(500.0),
            "City1",
            user,
            ZonedDateTime.now(),
            0,
            null,
            false);
    expectedSimulation.setPublic(false); // Assuming initially private

    when(simulationService.getSimulationById(simulationId))
        .thenReturn(Optional.of(expectedSimulation));
    when(OAuth2util.getUserID(mockToken)).thenReturn(userId);

    // Act
    ResponseEntity<?> responseEntity =
        simulationController.deleteSimulation(mockToken, simulationId);

    // Assert
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }
}
