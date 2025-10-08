package ch.usi.inf.bsc.sa4.lab02spring.modelTests;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.bsc.sa4.lab02spring.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BuildingTest {

  private Building skyscraper;
  private Building house;
  private Building condo;

  @BeforeEach
  public void setUp() {
    long peopleInBuilding = 500;
    this.skyscraper = new Building(50, BuildingType.SKYSCRAPER, peopleInBuilding);
    this.house = new Building(2, BuildingType.HOUSE, peopleInBuilding);
    this.condo = new Building(10, BuildingType.CONDO, peopleInBuilding);
  }

  /** test if after the creation of a skyscraper the fields are correct */
  @Test
  @DisplayName("Create a skyscraper and verify its properties")
  public void testSkyscraperCreation() {
    assertAll(
        () ->
            assertEquals(
                Integer.valueOf(50),
                skyscraper.getHeight(),
                "Height should match the " + "specified number of floors"),
        () ->
            assertEquals(
                BuildingType.SKYSCRAPER,
                skyscraper.getBuildingType(),
                "Building type " + "should be SKYSCRAPER"),
        () ->
            assertTrue(
                skyscraper.getPeople() >= 0 && skyscraper.getPeople() <= 10000,
                "People in " + "building should " + "be between 0 and 10000"));
  }

  /** test if after the creation of a house the fields are correct */
  @Test
  @DisplayName("Create a house and verify its properties")
  public void testHouseCreation() {
    assertAll(
        () ->
            assertEquals(
                Integer.valueOf(2),
                house.getHeight(),
                "Height should match the specified " + "number of floors"),
        () ->
            assertEquals(
                BuildingType.HOUSE, house.getBuildingType(), "Building type should be HOUSE"),
        () -> assertTrue(house.getPeople() >= 0 && house.getPeople() <= 10000));
  }

  /** test if after the creation of a condo the fields are correct */
  @Test
  @DisplayName("Create a condo and verify its properties")
  public void testCondoCreation() {
    assertAll(
        () ->
            assertEquals(
                Integer.valueOf(10),
                condo.getHeight(),
                "Height should match the specified " + "number of floors"),
        () ->
            assertEquals(
                BuildingType.CONDO, condo.getBuildingType(), "Building type should be CONDO"),
        () -> assertTrue(condo.getPeople() >= 0 && condo.getPeople() <= 10000));
  }

  @Test
  @DisplayName("Checks that the equals function returns true on 2 equal Buildings")
  public void equals() {
    Building sameBuilding = new Building(skyscraper);
    sameBuilding.setHeight(sameBuilding.getHeight());
    sameBuilding.setPeople(sameBuilding.getPeople());
    assertTrue(skyscraper.equals(skyscraper));
    assertTrue(skyscraper.equals(sameBuilding));
    assertFalse(skyscraper.equals(null));
    assertFalse(skyscraper.equals(new String("Hello World")));
  }

  @Test
  @DisplayName("Checks that the equals function returns false on 2 different buildings")
  public void notEquals() {
    Building otherBuilding1 = new Building(skyscraper);
    Building otherBuilding2 = new Building(skyscraper);
    Building otherBuilding3 = new Building(house);
    otherBuilding3.setHeight(skyscraper.getHeight());
    otherBuilding3.setPeople(skyscraper.getPeople());
    otherBuilding1.setPeople(2L);
    otherBuilding2.setHeight(2);
    assertFalse(skyscraper.equals(null));
    assertFalse(skyscraper.equals(otherBuilding1));
    assertFalse(skyscraper.equals(otherBuilding2));
    assertFalse(skyscraper.equals(otherBuilding3));
  }

  @Test
  @DisplayName("Checks function hashCode()")
  public void testHashCode() {
    Building B1 = new Building(skyscraper);
    Building B2 = new Building(skyscraper);
    Building B3 = new Building(skyscraper);
    int h1 = B1.hashCode();
    int h2 = B2.hashCode();
    int h3 = B3.hashCode();
    assertEquals(h1, h2, h3);
  }

  @Test
  @DisplayName("Checks the constructor when building in input is NULL")
  public void testNullBuilding() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new Building(null);
        });
  }
}
