package ch.usi.inf.bsc.sa4.lab02spring.modelTests;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.bsc.sa4.lab02spring.model.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("City class tests")
public class CityTests {

  /*
   A method to build a test city by creating a list of city blocks and a city object.

   @implNote
  *     <pre>City is defined with following params:
  *  Size: 100
  *  Id: "test"
  *  Population: 1000
  *  Blocks: 10
  *  cityBlockMap containing 2 blocks:
  *      block 1: ResidentialBlock with Coordinates (5, 10)
  *      block 2: NonResidentialBlock with Coordinates (6, 7)
  * </pre>
  */

  /** Default null money value */
  private static final Money NULL_MONEY = new Money(0.0);

  private City city;

  public static City testCity() {
    Map<Coordinates, CityBlock> testBlocksMap = new HashMap<>();
    testBlocksMap.put(
        new Coordinates(5, 10),
        new ResidentialBlock(
            new Building(1, BuildingType.HOUSE, 35L),
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY));
    testBlocksMap.put(new Coordinates(6, 7), new NonResidentialBlock(BlockType.WATERBLOCK));
    return new City(
        100, "test", 1000L, 10, testBlocksMap, new Money(6500.25), new Money(762000.375));
  }

  @BeforeEach
  public void setUp() {
    city = testCity();
  }

  @Test
  @DisplayName("Id getter for the City")
  public void testGetId() {
    assertEquals("test", city.getId());
  }

  @Test
  @DisplayName("Population getter for the City")
  public void testGetPopulation() {
    assertEquals(Long.valueOf(1000), city.getPopulation());
  }

  @Test
  @DisplayName("Radius getter for the City")
  public void testGetRadius() {
    assertEquals(Integer.valueOf(100), city.getRadius());
  }

  @Test
  @DisplayName("Block count getter for the City")
  public void testGetBlockCounter() {
    assertEquals(Integer.valueOf(10), city.getBlockCounter());
  }

  @Test
  @DisplayName("Getter for the City blocks in City")
  public void getBlocks() {
    assertEquals(
        BlockType.RESIDENTIALBLOCK, city.getBlocks().get(new Coordinates(5, 10)).getBlockType());
    assertEquals(BlockType.WATERBLOCK, city.getBlocks().get(new Coordinates(6, 7)).getBlockType());
    assertFalse(city.getBlocks().containsKey(new Coordinates(100, 9000)));
  }

  @Test
  @DisplayName("Getter for global max rent cost works")
  public void getGlobalMaxRentCost() {
    assertEquals(6500.25, city.getGlobalMaxRentCost().getValue());
  }

  @Test
  @DisplayName("Getter for global max construction cost works")
  public void getGlobalMaxConstructionCost() {
    assertEquals(762000.375, city.getGlobalMaxConstructionCost().getValue());
  }

  @Test
  @DisplayName("Checks that the equals function returns true on 2 equal cities")
  public void equals() {
    City sameCity = new City(city);
    assertTrue(city.equals(city));
    assertTrue(city.equals(sameCity));
  }

  @Test
  @DisplayName("Setter for global max rent cost works")
  public void setGlobalMaxRentCost() {
    city.setGlobalMaxRentCost(new Money(81274.25));

    assertEquals(81274.25, city.getGlobalMaxRentCost().getValue());
  }

  @Test
  @DisplayName("Setter for global max construction cost works")
  public void setGlobalMaxConstructionCost() {
    city.setGlobalMaxConstructionCost(new Money(918491.75));

    assertEquals(918491.75, city.getGlobalMaxConstructionCost().getValue());
  }

  @Test
  @DisplayName("Checks that the equals function returns false on 2 different cities")
  public void notEquals() {
    Map<Coordinates, CityBlock> blockMap = new HashMap<>();
    blockMap.put(
        new Coordinates(5, 11),
        new ResidentialBlock(
            new Building(1, BuildingType.HOUSE, 20L),
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY));
    blockMap.put(new Coordinates(6, 7), new NonResidentialBlock(BlockType.PARKBLOCK));
    City otherCity1 = new City(city);
    City otherCity2 = new City(city);
    City otherCity3 = new City(city);
    City otherCity4 = new City(city);
    otherCity1.setPopulation(1L);
    otherCity2.setRadius(2);
    otherCity3.setBlockCounter(11);
    otherCity4.setCityBlockMap(blockMap);
    Object differentObject = new Object();
    assertFalse(city.equals(differentObject));
    assertFalse(city.equals(null));
    assertFalse(city.equals(otherCity1));
    assertFalse(city.equals(otherCity2));
    assertFalse(city.equals(otherCity3));
    assertFalse(city.equals(otherCity4));
    assertTrue(city.equals(city));
  }

  @Test
  @DisplayName("Ensures that a null object cannot be copied")
  public void testCopyNullObject() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new City(null);
        },
        "Cannot copy a null object.");
  }

  @Test
  @DisplayName("Ensures equal objects have equal hash codes")
  public void testEqualHashCodes() {
    City city1 = new City(city);
    assertEquals(city.hashCode(), city1.hashCode());
  }

  @Test
  @DisplayName("Ensures equal objects have equal hash codes")
  public void testCitySecondaryConstructor() {
    Map<Coordinates, CityBlock> blockMap = new HashMap<>();
    City createdCity =
        new City(1500, 87263L, 17, blockMap, new Money(6500.25), new Money(762000.375));
    assertNotNull(createdCity);
  }
}
