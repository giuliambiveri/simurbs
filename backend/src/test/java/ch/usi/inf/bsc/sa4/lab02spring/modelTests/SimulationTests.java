package ch.usi.inf.bsc.sa4.lab02spring.modelTests;

import static ch.usi.inf.bsc.sa4.lab02spring.utils.CityGenerator.createNewCity;
import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.bsc.sa4.lab02spring.model.*;
import ch.usi.inf.bsc.sa4.lab02spring.utils.CityCreationParameters;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Simulation Class Tests")
public class SimulationTests {

  /** Default null money value */
  private static final Money nullMoney = new Money(0.0);

  private User user;
  private Simulation simulation;
  private City city;

  @BeforeEach
  public void setUp() {
    user = new User("bskd-hdsn873", "Lamberto", "Lamberto", "lambertoragnolini@gmail.com");
    city = testCity();
    simulation =
        new Simulation(
            new Money(1000.0),
            new Money(50.0),
            "TestCity",
            user,
            ZonedDateTime.now(),
            0,
            null,
            false);
    simulation.setSimulatedCity(createNewCity(new CityCreationParameters(1000.0, 500.0)));
    simulation.setSimulatedCity(city);
  }

  private static City testCity() {
    Map<Coordinates, CityBlock> testBlocksMap = new HashMap<>();
    testBlocksMap.put(
        new Coordinates(5, 10),
        new ResidentialBlock(
            new Building(1, BuildingType.HOUSE, 345L),
            nullMoney,
            nullMoney,
            nullMoney,
            nullMoney,
            nullMoney,
            nullMoney,
            nullMoney));
    testBlocksMap.put(new Coordinates(6, 7), new NonResidentialBlock(BlockType.WATERBLOCK));
    return new City(100, "test", 1000L, 10, testBlocksMap);
  }

  @Test
  @DisplayName("Throw IllegalArgumentException for null city name")
  public void testNullCityNameThrowsException() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new Simulation(
                new Money(1000.0),
                new Money(50.0),
                null,
                user,
                ZonedDateTime.now(),
                0,
                null,
                false),
        "City name must be non-null and not empty.");
  }

  @Test
  @DisplayName("Throw IllegalArgumentException for empty city name")
  public void testEmptyCityNameThrowsException() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new Simulation(
                new Money(1000.0), new Money(50.0), "", user, ZonedDateTime.now(), 0, null, false),
        "City name must be non-null and not empty.");
  }

  @Test
  @DisplayName("getAverageIncome returns the correct average income")
  public void testGetAverageIncome() {
    assertEquals(new Money(1000.0), simulation.getAverageIncome());
  }

  @Test
  @DisplayName("getCommuterCosts returns the correct commuter costs")
  public void testGetCommuterCosts() {
    assertEquals(new Money(50.0), simulation.getCommuterCosts());
  }

  @Test
  @DisplayName("getName returns the correct city name")
  public void testGetName() {
    assertEquals("TestCity", simulation.getName());
  }

  @Test
  @DisplayName("getSimulatedCity returns a non-null City object")
  public void testGetSimulatedCity() {
    assertNotNull(simulation.getSimulatedCity());
  }

  @Test
  @DisplayName("isPublic returns false")
  public void testIsPublic() {
    assertFalse(simulation.isPublic());
  }

  @Test
  @DisplayName("getCreationDate returns a non-null ZonedDateTime object")
  public void testGetCreationDate() {
    assertNotNull(simulation.getCreationDate());
  }

  @Test
  @DisplayName("getSetCreationDate returns a non-null ZonedDateTime object")
  public void testSetCreationDate() {
    simulation.setCreationDate(ZonedDateTime.of(2024, 5, 6, 12, 0, 0, 0, ZoneId.of("Europe/Rome")));
    assertEquals(
        ZonedDateTime.of(2024, 5, 6, 12, 0, 0, 0, ZoneId.of("Europe/Rome")),
        simulation.getCreationDate());
  }

  @Test
  @DisplayName("getViews returns 0")
  public void testGetViews() {
    assertEquals(0, simulation.getViews());
  }

  @Test
  @DisplayName("getOldSimulation returns null")
  public void testGetOldSimulation() {
    assertNull(simulation.getOldSimulation());
  }

  @Test
  @DisplayName("isOld returns true")
  public void testIsOld() {
    assertFalse(simulation.isOld());
  }

  @Test
  @DisplayName("Average Income is a money type with the correct value")
  public void testGetAverageIncomeWithGetter() {
    assertEquals(new Money(1000.0), simulation.getAverageIncome());
  }

  @Test
  @DisplayName("setName sets a new name to the simulation")
  public void testSetName() {
    simulation.setName("Lambione");
    assertEquals("Lambione", simulation.getName());
  }

  @Test
  @DisplayName("setAverageIncome sets a new average income to the simulation")
  public void testSetAverageIncome() {
    simulation.setAverageIncome(new Money(2000.0));
    assertEquals(new Money(2000.0), simulation.getAverageIncome());
  }

  @Test
  @DisplayName("setAverageIncome sets a new average income to the simulation")
  public void testSetCommuterCosts() {
    simulation.setCommuterCosts(new Money(3000.0));
    assertEquals(new Money(3000.0), simulation.getCommuterCosts());
  }

  @Test
  @DisplayName("setAverageIncome sets a new average income to the simulation")
  public void testSetCreationDateWithSameDate() {
    ZonedDateTime date = ZonedDateTime.now();
    simulation.setCreationDate(date);
    assertEquals(date, simulation.getCreationDate());
  }

  @Test
  @DisplayName("setAverageIncome sets a new average income to the simulation")
  public void testSetOldSimulation() {

    Simulation oldSimulation =
        new Simulation(
            new Money(2000.0),
            new Money(100.0),
            "otherCity",
            user,
            ZonedDateTime.now(),
            0,
            null,
            false);
    oldSimulation.setSimulatedCity(createNewCity(new CityCreationParameters(1000.0, 500.0)));
    oldSimulation.setSimulatedCity(city);

    simulation.setOldSimulation(oldSimulation);
    assertEquals(oldSimulation, simulation.getOldSimulation());
    assertNotNull(simulation.getOldSimulation());
  }

  @Test
  @DisplayName("setOld sets the value of isOld of the simulation either to true or to false")
  public void testSetOld() {
    simulation.setOld(true);
    assertTrue(simulation.isOld());
  }

  @Test
  @DisplayName("equals method returns true if a simulation is equal to the other")
  public void testEqualsMethodReturnsTrue() {
    assertTrue(simulation.equals(simulation));
  }

  @Test
  @DisplayName("equals method returns true if a simulation is equal to the other")
  public void testEqualsMethodReturnsFalse() {
    assertFalse(simulation.equals(null));
  }

  @Test
  @DisplayName("equals method returns true if a simulation is equal to the other")
  public void testEqualsMethodReturnsFalseDifferentObject() {
    assertFalse(simulation.equals(new Coordinates(1, 2)));
  }

  @Test
  @DisplayName("equals method returns true because all fields are equal")
  public void testEqualsMethodReturnsTrueByCheckingAllFields() {
    Simulation newSimulation =
        new Simulation(
            new Money(1000.0),
            new Money(50.0),
            "TestCity",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    Simulation newSimulation2 =
        new Simulation(
            new Money(1000.0),
            new Money(50.0),
            "TestCity",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    Simulation newSimulation3 =
        new Simulation(
            new Money(1000.0),
            new Money(50.0),
            "TestCity",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    newSimulation.setSimulatedCity(city);
    newSimulation2.setSimulatedCity(city);
    newSimulation3.setSimulatedCity(city);
    newSimulation.setOldSimulation(newSimulation3);
    newSimulation2.setOldSimulation(newSimulation3);
    newSimulation2.setPublic(false);
    newSimulation.setPublic(false);

    assertNotNull(newSimulation.getOldSimulation());
    assertTrue(newSimulation.equals(newSimulation2));
  }

  @Test
  @DisplayName("equals method returns false because a field is different")
  public void testEqualsMethodReturnsFalseIdIsDifferent() {
    Simulation newSimulation =
        new Simulation(
            new Money(1000.0),
            new Money(50.0),
            "Alessandro",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    Simulation newSimulation2 =
        new Simulation(
            new Money(1000.0),
            new Money(50.0),
            "TestCity",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    Simulation newSimulation3 =
        new Simulation(
            new Money(1000.0),
            new Money(50.0),
            "TestCity",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    newSimulation.setSimulatedCity(city);
    newSimulation2.setSimulatedCity(city);
    newSimulation3.setSimulatedCity(city);
    newSimulation.setOldSimulation(newSimulation3);
    newSimulation2.setOldSimulation(newSimulation3);
    newSimulation2.setPublic(false);
    newSimulation.setPublic(false);

    assertNotNull(newSimulation.getOldSimulation());
    assertNotEquals(newSimulation, newSimulation2);
  }

  @Test
  @DisplayName("equals method returns false because a field is different")
  public void testEqualsMethodReturnsFalseAverageIncomeIsDifferent() {
    Simulation newSimulation =
        new Simulation(
            new Money(2000.0),
            new Money(50.0),
            "Alessandro",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    Simulation newSimulation2 =
        new Simulation(
            new Money(1000.0),
            new Money(50.0),
            "Alessandro",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    Simulation newSimulation3 =
        new Simulation(
            new Money(1000.0),
            new Money(50.0),
            "TestCity",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    newSimulation.setSimulatedCity(city);
    newSimulation2.setSimulatedCity(city);
    newSimulation3.setSimulatedCity(city);
    newSimulation.setOldSimulation(newSimulation3);
    newSimulation2.setOldSimulation(newSimulation3);
    newSimulation2.setPublic(false);
    newSimulation.setPublic(false);

    assertNotNull(newSimulation.getOldSimulation());
    assertNotEquals(newSimulation, newSimulation2);
  }

  @Test
  @DisplayName("equals method returns false because a field is different")
  public void testEqualsMethodReturnsFalseCommuterCostIsDifferent() {
    Simulation newSimulation =
        new Simulation(
            new Money(2000.0),
            new Money(50.0),
            "Alessandro",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    Simulation newSimulation2 =
        new Simulation(
            new Money(2000.0),
            new Money(100.0),
            "Alessandro",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    Simulation newSimulation3 =
        new Simulation(
            new Money(1000.0),
            new Money(50.0),
            "TestCity",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    newSimulation.setSimulatedCity(city);
    newSimulation2.setSimulatedCity(city);
    newSimulation3.setSimulatedCity(city);
    newSimulation.setOldSimulation(newSimulation3);
    newSimulation2.setOldSimulation(newSimulation3);
    newSimulation2.setPublic(false);
    newSimulation.setPublic(false);

    assertNotNull(newSimulation.getOldSimulation());
    assertNotEquals(newSimulation, newSimulation2);
  }

  @Test
  @DisplayName("equals method returns false because a field is different")
  public void testEqualsMethodReturnsFalseUserIsDifferent() {
    User user1 = new User("8237basd772", "Alesandro", "della", "alessandro@gmail.com");
    Simulation newSimulation =
        new Simulation(
            new Money(2000.0),
            new Money(100.0),
            "Alessandro",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    Simulation newSimulation2 =
        new Simulation(
            new Money(2000.0),
            new Money(100.0),
            "Alessandro",
            user1,
            ZonedDateTime.now(),
            0,
            null,
            true);

    Simulation newSimulation3 =
        new Simulation(
            new Money(1000.0),
            new Money(50.0),
            "TestCity",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    newSimulation.setSimulatedCity(city);
    newSimulation2.setSimulatedCity(city);
    newSimulation3.setSimulatedCity(city);
    newSimulation.setOldSimulation(newSimulation3);
    newSimulation2.setOldSimulation(newSimulation3);
    newSimulation2.setPublic(false);
    newSimulation.setPublic(false);

    assertNotNull(newSimulation.getOldSimulation());
    assertNotEquals(newSimulation, newSimulation2);
  }

  @Test
  @DisplayName("equals method returns false because a field is different")
  public void testEqualsMethodReturnsFalseIsOldIsDifferent() {
    Simulation newSimulation =
        new Simulation(
            new Money(2000.0),
            new Money(100.0),
            "Alessandro",
            user,
            ZonedDateTime.now(),
            0,
            null,
            false);

    Simulation newSimulation2 =
        new Simulation(
            new Money(2000.0),
            new Money(100.0),
            "Alessandro",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    Simulation newSimulation3 =
        new Simulation(
            new Money(1000.0),
            new Money(50.0),
            "TestCity",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    newSimulation.setSimulatedCity(city);
    newSimulation2.setSimulatedCity(city);
    newSimulation3.setSimulatedCity(city);
    newSimulation.setOldSimulation(newSimulation3);
    newSimulation2.setOldSimulation(newSimulation3);
    newSimulation.setPublic(false);

    assertNotNull(newSimulation.getOldSimulation());
    assertNotEquals(newSimulation, newSimulation2);
  }

  @Test
  @DisplayName("equals method returns false because a field is different")
  public void testEqualsMethodReturnsFalseViewsIsDifferent() {
    Simulation newSimulation =
        new Simulation(
            new Money(2000.0),
            new Money(100.0),
            "Alessandro",
            user,
            ZonedDateTime.now(),
            100,
            null,
            true);

    Simulation newSimulation2 =
        new Simulation(
            new Money(2000.0),
            new Money(100.0),
            "Alessandro",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    Simulation newSimulation3 =
        new Simulation(
            new Money(1000.0),
            new Money(50.0),
            "TestCity",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    newSimulation.setSimulatedCity(city);
    newSimulation2.setSimulatedCity(city);
    newSimulation3.setSimulatedCity(city);
    newSimulation.setOldSimulation(newSimulation3);
    newSimulation2.setOldSimulation(newSimulation3);
    newSimulation.setPublic(false);

    assertNotNull(newSimulation.getOldSimulation());
    assertNotEquals(newSimulation, newSimulation2);
  }

  @Test
  @DisplayName("equals method returns false because a field is different")
  public void testEqualsMethodReturnsFalseOldSimulationIsDifferent() {
    Simulation newSimulation =
        new Simulation(
            new Money(2000.0),
            new Money(100.0),
            "Alessandro",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    Simulation newSimulation2 =
        new Simulation(
            new Money(2000.0),
            new Money(100.0),
            "Alessandro",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    Simulation newSimulation3 =
        new Simulation(
            new Money(1000.0),
            new Money(50.0),
            "TestCity",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    newSimulation.setSimulatedCity(city);
    newSimulation2.setSimulatedCity(city);
    newSimulation3.setSimulatedCity(city);
    newSimulation.setOldSimulation(newSimulation3);
    newSimulation2.setOldSimulation(newSimulation);
    newSimulation.setPublic(false);
    newSimulation2.setPublic(false);

    assertNotNull(newSimulation.getOldSimulation());
    assertNotEquals(newSimulation, newSimulation2);
  }

  @Test
  @DisplayName("Throw IllegalArgumentException for null simulation")
  public void testNullSimulationThrowsException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Simulation(null),
        "Simulation should not be null");
  }

  @Test
  @DisplayName("setAverageIncome sets a new average income to the simulation")
  public void testSetOldSimulationReturnsNull() {

    Simulation oldSimulation =
        new Simulation(
            new Money(2000.0),
            new Money(100.0),
            "otherCity",
            user,
            ZonedDateTime.now(),
            0,
            null,
            false);
    oldSimulation.setSimulatedCity(createNewCity(new CityCreationParameters(1000.0, 500.0)));
    oldSimulation.setSimulatedCity(city);

    simulation.setOldSimulation(null);
    assertNull(simulation.getOldSimulation());
  }

  @Test
  @DisplayName("Main constructor oldSImulation is null so creates a new Simulation for it")
  public void testMainConstructorOldSimulationIsDifferent() {
    Simulation newSimulation =
        new Simulation(
            new Money(2000.0),
            new Money(100.0),
            "Alessandro",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    Simulation newSimulation3 =
        new Simulation(
            new Money(1000.0),
            new Money(50.0),
            "TestCity",
            user,
            ZonedDateTime.now(),
            0,
            null,
            true);

    newSimulation.setSimulatedCity(city);
    newSimulation3.setSimulatedCity(city);
    newSimulation.setOldSimulation(newSimulation3);
    newSimulation3.setOldSimulation(newSimulation);
    newSimulation.setPublic(false);

    assertNotNull(newSimulation.getOldSimulation());
  }
}
