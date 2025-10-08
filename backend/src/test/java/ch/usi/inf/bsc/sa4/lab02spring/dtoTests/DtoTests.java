package ch.usi.inf.bsc.sa4.lab02spring.dtoTests;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.*;
import ch.usi.inf.bsc.sa4.lab02spring.model.*;
import ch.usi.inf.bsc.sa4.lab02spring.service.CityService;
import ch.usi.inf.bsc.sa4.lab02spring.service.SimulationService;
import ch.usi.inf.bsc.sa4.lab02spring.service.UserService;
import ch.usi.inf.bsc.sa4.lab02spring.utils.CityCreationParameters;
import java.time.ZonedDateTime;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Testing the correct creation of all DTO's")
public class DtoTests {

  @Autowired private UserService userService;
  @Autowired private SimulationService simulationService;
  @Autowired private CityService cityService;

  private Simulation testSimulation;
  private Simulation simulation;
  private CreateSimulationDTO simDTO;
  private RestrictionsDTO restrDTO;
  private ListSimulationDTO listSimulationDTO;
  private User user;

  private Money avgIncome;
  private Money commuterCost;
  private String name;
  private String restrictedJSON;
  private BlockType blockType;
  private Coordinates coordinates;
  private City generatedCity;

  @BeforeEach
  public void setup() {
    this.user = new User("b76-njda-23", "Lamberto", "Ragnolini", "lambertoragnolini@gmail.com");
    this.avgIncome = new Money(6500.00);
    this.commuterCost = new Money(200.00);
    this.name = "Lamberto";
    this.restrictedJSON = "restrictedJSON";
    this.blockType = BlockType.WATERBLOCK;
    this.coordinates = new Coordinates(1, 1);
    this.simDTO = new CreateSimulationDTO(avgIncome, commuterCost, name, restrictedJSON);
    this.restrDTO = new RestrictionsDTO(coordinates, avgIncome, commuterCost, avgIncome, blockType);
    this.simulation =
        new Simulation(
            new Money(6500.0),
            new Money(200.0),
            "Lambione",
            user,
            ZonedDateTime.now(),
            0,
            null,
            false);
    CityCreationParameters cityCreationParameters = new CityCreationParameters(6500.0, 200.0);
    generatedCity = cityService.createCity(cityCreationParameters);
    this.simulation.setSimulatedCity(generatedCity);
    listSimulationDTO = new ListSimulationDTO(simulation);
  }

  @Test
  @DisplayName("test that the createSimulationDTO gets created correctly")
  public void testCreateSimulationDTO() {
    CreateSimulationDTO simDTO =
        new CreateSimulationDTO(avgIncome, commuterCost, name, restrictedJSON);

    assertEquals(this.simDTO, simDTO);
    assertAll(
        () -> assertEquals(avgIncome, simDTO.avgIncome()),
        () -> assertEquals(commuterCost, simDTO.commuterCost()),
        () -> assertEquals(name, simDTO.name()),
        () -> assertEquals(restrictedJSON, simDTO.restrictionsJSON()));
  }

  @Test
  @DisplayName("test that the createSimulationDTO gets created correctly")
  public void testRestrictionsDTO() {

    Coordinates wrongCoordinates = new Coordinates(2, 2);
    RestrictionsDTO restrictionDTO =
        new RestrictionsDTO(new Coordinates(1, 1), avgIncome, commuterCost, avgIncome, blockType);

    assertEquals(this.restrDTO, restrictionDTO);
    assertAll(
        () -> assertEquals(avgIncome, restrictionDTO.localLimitTransportationCost()),
        () -> assertEquals(commuterCost, restrictionDTO.localLimitConstructionCost()),
        () -> assertEquals(avgIncome, restrictionDTO.localLimitRentCost()),
        () -> assertEquals(coordinates, restrictionDTO.coordinates()),
        () -> assertEquals(0, restrictionDTO.coordinates().compareTo(coordinates)),
        () -> assertEquals(-1, restrictionDTO.coordinates().compareTo(wrongCoordinates)));
  }

  @Test
  @DisplayName("test that the ListSimulationDTO gets created correctly")
  public void testListSimulationDTO() {

    CityCreationParameters cityCreationParameters = new CityCreationParameters(6500.0, 200.0);
    City generatedCity = cityService.createCity(cityCreationParameters);
    this.simulation.setSimulatedCity(generatedCity);
    ListSimulationDTO listSimulationDTO = new ListSimulationDTO(simulation);

    assertEquals(this.listSimulationDTO, listSimulationDTO);
  }

  @Test
  @DisplayName("test that the ValidCheckAuthenticationDTO is created correctly")
  public void testValidCheckAuthenticationDTO() {
    boolean loggedIn = true;
    CheckAuthenticationDTO checkAuthenticationDTO = new CheckAuthenticationDTO(loggedIn);
    assertNotNull(checkAuthenticationDTO);
  }

  @Test
  @DisplayName(
      "Method testInvalidObject should return true given an object that is not a SimulationDTO")
  public void testInvalidObject() {
    SimulationDTO simDTO =
        new SimulationDTO(
            "", new Money(5000.0), new Money(260.0), "Varese", generatedCity, false, null, 0, null);
    Coordinates object = new Coordinates(1, 1);
    boolean result = simDTO.equals(object);
    assertFalse(result);
  }

  @Test
  @DisplayName("Method testNullSimulation should return true given a null object")
  public void testNullSimulation() {
    SimulationDTO simDTO =
        new SimulationDTO(
            "", new Money(5000.0), new Money(260.0), "Varese", generatedCity, false, null, 0, null);
    SimulationDTO object = null;
    boolean result = simDTO.equals(object);
    assertFalse(result);
  }

  @Test
  @DisplayName("Method testSameSimulationDTO should return true given the same SimulationDTO")
  public void testSameSimulationDTO() {
    SimulationDTO simDTO =
        new SimulationDTO(
            "", new Money(5000.0), new Money(260.0), "Varese", generatedCity, false, null, 0, null);
    SimulationDTO object = simDTO;
    boolean result = simDTO.equals(object);
    assertTrue(result);
  }

  @Test
  @DisplayName("Method testequalSimulation should return true given two identical simulationDTO")
  public void testEqualSimulationDTO() {
    SimulationDTO simDTO =
        new SimulationDTO(
            "", new Money(5000.0), new Money(260.0), "Varese", generatedCity, false, null, 0, null);
    SimulationDTO object =
        new SimulationDTO(
            "", new Money(5000.0), new Money(260.0), "Varese", generatedCity, false, null, 0, null);
    boolean result = simDTO.equals(object);
    assertTrue(result);
  }

  @Test
  @DisplayName(
      "Method testDifferentAverageIncome should return true given two simulationDTO with different "
          + "averageIncome")
  public void testDifferentAverageIncome() {
    SimulationDTO simDTO =
        new SimulationDTO(
            "", new Money(5000.0), new Money(260.0), "Varese", generatedCity, false, null, 0, null);
    SimulationDTO object =
        new SimulationDTO(
            "", new Money(5001.0), new Money(260.0), "Varese", generatedCity, false, null, 0, null);
    boolean result = simDTO.equals(object);
    assertFalse(result);
  }

  @Test
  @DisplayName(
      "Method testDifferentCommuterCost should return true given two simulationDTO with different "
          + "commuterCost")
  public void testDifferentCommuterCost() {
    SimulationDTO simDTO =
        new SimulationDTO(
            "", new Money(5000.0), new Money(260.0), "Varese", generatedCity, false, null, 0, null);
    SimulationDTO object =
        new SimulationDTO(
            "", new Money(5000.0), new Money(10.0), "Varese", generatedCity, false, null, 0, null);
    boolean result = simDTO.equals(object);
    assertFalse(result);
  }

  @Test
  @DisplayName(
      "Method testDifferentName should return true given two simulationDTO with different "
          + "name")
  public void testDifferentName() {
    SimulationDTO simDTO =
        new SimulationDTO(
            "", new Money(5000.0), new Money(260.0), "Varese", generatedCity, false, null, 0, null);
    SimulationDTO object =
        new SimulationDTO(
            "",
            new Money(5000.0),
            new Money(260.0),
            "Gallarate",
            generatedCity,
            false,
            null,
            0,
            null);
    boolean result = simDTO.equals(object);
    assertFalse(result);
  }

  @Test
  @DisplayName(
      "Method testDifferentCity should return true given two simulationDTO with different "
          + "City")
  public void testDifferentCity() {
    SimulationDTO simDTO =
        new SimulationDTO(
            "", new Money(5000.0), new Money(260.0), "Varese", generatedCity, false, null, 0, null);
    SimulationDTO object =
        new SimulationDTO(
            "",
            new Money(5000.0),
            new Money(260.0),
            "Varese",
            cityService.createCity(new CityCreationParameters(6500.0, 210.0)),
            false,
            null,
            0,
            null);
    boolean result = simDTO.equals(object);
    assertFalse(result);
  }

  @Test
  @DisplayName(
      "Method testDifferentPublic should return true given two simulationDTO with different "
          + "public")
  public void testDifferentPublic() {
    SimulationDTO simDTO =
        new SimulationDTO(
            "", new Money(5000.0), new Money(260.0), "Varese", generatedCity, false, null, 0, null);
    SimulationDTO object =
        new SimulationDTO(
            "", new Money(5000.0), new Money(260.0), "Varese", generatedCity, true, null, 0, null);
    boolean result = simDTO.equals(object);
    assertFalse(result);
  }

  @Test
  @DisplayName(
      "Method testDifferentZoneDateTime should return true given two simulationDTO with different "
          + "ZoneDateTime")
  public void testDifferentZoneDateTime() {
    SimulationDTO simDTO =
        new SimulationDTO(
            "",
            new Money(5000.0),
            new Money(260.0),
            "Varese",
            generatedCity,
            false,
            ZonedDateTime.now(),
            0,
            null);
    SimulationDTO object =
        new SimulationDTO(
            "",
            new Money(5000.0),
            new Money(260.0),
            "Varese",
            generatedCity,
            false,
            ZonedDateTime.now(),
            0,
            null);
    boolean result = simDTO.equals(object);
    // do not change, locally it gives error, on gitlab it works
    assertFalse(result);
  }

  @Test
  @DisplayName(
      "Method testDifferentView should return true given two simulationDTO with different "
          + "view")
  public void testDifferentView() {
    SimulationDTO simDTO =
        new SimulationDTO(
            "",
            new Money(5000.0),
            new Money(260.0),
            "Varese",
            generatedCity,
            false,
            ZonedDateTime.now(),
            10,
            null);
    SimulationDTO object =
        new SimulationDTO(
            "",
            new Money(5000.0),
            new Money(260.0),
            "Varese",
            generatedCity,
            false,
            ZonedDateTime.now(),
            0,
            null);
    boolean result = simDTO.equals(object);
    assertFalse(result);
  }

  @Test
  @DisplayName(
      "Method testDifferentOldSimulation should return true given two simulationDTO with different "
          + "oldSimulation")
  public void testDifferentOldSimulation() {
    SimulationDTO simDTO =
        new SimulationDTO(
            "",
            new Money(5000.0),
            new Money(260.0),
            "Varese",
            generatedCity,
            false,
            null,
            0,
            simulation);
    SimulationDTO object =
        new SimulationDTO(
            "", new Money(5000.0), new Money(260.0), "Varese", generatedCity, false, null, 0, null);
    boolean result = simDTO.equals(object);
    assertFalse(result);
  }

  @Test
  @DisplayName("Method testHashCode should return true given a SimulationDTO")
  public void testHashCode() {
    Money averageIncome = new Money(5000.0);
    Money commuterCost = new Money(260.0);
    String name = "Varese";
    boolean isPublic = false;
    ZonedDateTime dateTime = null;
    Integer views = 0;
    SimulationDTO simDTO =
        new SimulationDTO(
            "",
            averageIncome,
            commuterCost,
            name,
            generatedCity,
            isPublic,
            dateTime,
            views,
            simulation);

    assertEquals(
        simDTO.hashCode(),
        Objects.hash(
            averageIncome,
            commuterCost,
            name,
            generatedCity,
            isPublic,
            dateTime,
            views,
            simulation));
  }
}
