package ch.usi.inf.bsc.sa4.lab02spring.modelTests;

import static ch.usi.inf.bsc.sa4.lab02spring.model.BlockType.RESIDENTIALBLOCK;
import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.bsc.sa4.lab02spring.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ResidentialBlock Class Tests")
public class ResidentialBlockTests {

  /** Default null money value */
  private static final Money NULL_MONEY = new Money(0.0);

  private ResidentialBlock residentialBlock;
  private Money cost;

  private Building building;

  private ResidentialBlock residentialBlockWithLimits;

  @BeforeEach
  public void setUp() {
    building = new Building(1, BuildingType.HOUSE, 200L);
    cost = NULL_MONEY;
    residentialBlock =
        new ResidentialBlock(
            building,
            cost,
            new Money(7.0),
            new Money(15.0),
            new Money(30.0),
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY);
    residentialBlockWithLimits =
        new ResidentialBlock(
            building,
            cost,
            new Money(15.0),
            new Money(7.0),
            new Money(30.0),
            new Money(560000.5),
            NULL_MONEY,
            new Money(3400.125));
  }

  @Test
  @DisplayName("Check block type")
  public void testGetBlockType() {
    ResidentialBlock rb =
        new ResidentialBlock(
            this.building,
            this.cost,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY);
    assertEquals(RESIDENTIALBLOCK, rb.getBlockType());
  }

  @Test
  @DisplayName("Verify building type")
  public void testGetBuildingType() {
    assertNotNull(residentialBlock.getBuildingType());
  }

  @Test
  @DisplayName("Getter for local max rent cost works")
  public void testGetLocalMaxRentCost() {
    Money expectedRentCost = new Money(3400.125, Currency.CHF);
    assertEquals(expectedRentCost, residentialBlockWithLimits.getLocalLimitRentCost());
  }

  @Test
  @DisplayName("Test whether getter for constructionCost field works")
  public void testGetConstructionCost() {
    assertEquals(new Money(30.0), residentialBlockWithLimits.getConstructionCost());
  }

  @Test
  @DisplayName("Getter for local max construction cost works")
  public void testGetLocalMaxConstructionCost() {
    assertEquals(new Money(560000.5), residentialBlockWithLimits.getLocalLimitConstructionCost());
  }

  @Test
  @DisplayName("Ensure rent cost value matches expected cost")
  public void testGetRentCost() {
    Money rentCost = residentialBlock.getRentCost();
    assertEquals(0.0, rentCost.getValue());
  }

  @Test
  @DisplayName("getBuilding returns the correct Building object")
  public void testGetBuilding() {
    Building actualBuilding = residentialBlock.getBuilding();

    assertEquals(building.getBuildingType(), actualBuilding.getBuildingType());
    assertEquals(building.getHeight(), actualBuilding.getHeight());
  }

  @Test
  @DisplayName("Setter for building works")
  public void testSetBuilding() {
    Building newBuilding = new Building(100, BuildingType.CONDO, 14000L);

    residentialBlock.setBuilding(newBuilding);

    assertEquals(newBuilding, residentialBlock.getBuilding());
  }

  @Test
  @DisplayName("Setter for rent cost works")
  public void testSetRentCost() {
    Money newRentCost = new Money(5243.25, Currency.CHF);

    residentialBlock.setRentCost(newRentCost);

    assertEquals(newRentCost, residentialBlock.getRentCost());
  }

  @Test
  @DisplayName("Setter for local max rent cost works")
  public void testSetLocalMaxRentCost() {
    Money newRentCost = new Money(8272.125, Currency.CHF);
    residentialBlockWithLimits.setLocalLimitRentCost(newRentCost);

    assertEquals(newRentCost, residentialBlockWithLimits.getLocalLimitRentCost());
  }

  @Test
  @DisplayName("Setter for local max rent cost works")
  public void testSetLocalMaxConstructionCost() {
    residentialBlockWithLimits.setLocalLimitConstructionCost(new Money(8923.25));

    assertEquals(new Money(8923.25), residentialBlockWithLimits.getLocalLimitConstructionCost());
  }

  @Test
  @DisplayName("Setter for local max rent cost works")
  public void testSetConstructionCost() {
    residentialBlockWithLimits.setConstructionCost(new Money(3000.0));

    assertEquals(3000.0, residentialBlockWithLimits.getConstructionCost().getValue());
  }

  @Test
  @DisplayName("getTransportation method returns the correct transportation cost")
  public void testGetTransportationCost() {
    Money actualCost = residentialBlock.getTransportationCost();
    assertEquals(new Money(15.0), actualCost);
  }

  @Test
  @DisplayName("Test setting new block type")
  public void testSetType() {
    BlockType newType = BlockType.WATERBLOCK;
    residentialBlock.setType(newType);
    assertEquals(newType, residentialBlock.getBlockType());
  }

  @Test
  @DisplayName("Test equals method with identical blocks")
  public void testEqualsIdenticalBlocks() {
    ResidentialBlock block1 =
        new ResidentialBlock(
            building, cost, NULL_MONEY, NULL_MONEY, NULL_MONEY, NULL_MONEY, NULL_MONEY, NULL_MONEY);
    ResidentialBlock block2 =
        new ResidentialBlock(
            building, cost, NULL_MONEY, NULL_MONEY, NULL_MONEY, NULL_MONEY, NULL_MONEY, NULL_MONEY);

    assertEquals(
        block1.equals(block2), true, "Identical ResidentialBlocks should be considered equal.");
  }

  @Test
  @DisplayName("Test equals method with different rent cost")
  public void testEqualsDifferentRentCost() {
    ResidentialBlock block1 =
        new ResidentialBlock(
            building, cost, NULL_MONEY, NULL_MONEY, NULL_MONEY, NULL_MONEY, NULL_MONEY, NULL_MONEY);
    Money differentCost = new Money(100.0, Currency.CHF);
    ResidentialBlock block2 =
        new ResidentialBlock(
            building,
            differentCost,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY);

    assertEquals(
        block1.equals(block2),
        false,
        "ResidentialBlocks with different rent costs should not be considered equal.");
  }

  @Test
  @DisplayName(
      "equals method should not identify two ResidentialBlocks as equal if they have different properties")
  public void testEqualsNotIdentical() {
    ResidentialBlock block1 =
        new ResidentialBlock(
            new Building(1, BuildingType.HOUSE, 100L),
            new Money(100.0, Currency.CHF),
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY);
    ResidentialBlock block2 =
        new ResidentialBlock(
            new Building(2, BuildingType.SKYSCRAPER, 200L),
            new Money(200.0, Currency.CHF),
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY);

    assertEquals(
        block1.equals(block2),
        false,
        "ResidentialBlocks with different buildings should not be considered equal.");
  }

  @Test
  @DisplayName("Test hashCode method")
  public void testHashCode() {
    ResidentialBlock block1 =
        new ResidentialBlock(
            building, cost, NULL_MONEY, NULL_MONEY, NULL_MONEY, NULL_MONEY, NULL_MONEY, NULL_MONEY);
    ResidentialBlock block2 =
        new ResidentialBlock(
            building, cost, NULL_MONEY, NULL_MONEY, NULL_MONEY, NULL_MONEY, NULL_MONEY, NULL_MONEY);

    assertEquals(
        block1.hashCode(),
        block2.hashCode(),
        "Hash codes of identical ResidentialBlocks should be equal.");
  }

  @Test
  @DisplayName("Constructor of Residentialblock works correctly")
  public void testConstructorResidentialBlockWithBlockType() {
    ResidentialBlock block1 =
        new ResidentialBlock(
            building,
            cost,
            new Money(15.0),
            new Money(15.0),
            new Money(15.0),
            new Money(15.0),
            new Money(15.0),
            new Money(15.0),
            RESIDENTIALBLOCK);

    assertNotNull(block1);
  }

  @Test
  @DisplayName("getTransportation ")
  public void testGetBlockTypeRestriction() {
    ResidentialBlock block1 =
        new ResidentialBlock(
            building,
            cost,
            new Money(15.0),
            new Money(15.0),
            new Money(15.0),
            new Money(15.0),
            new Money(15.0),
            new Money(15.0),
            RESIDENTIALBLOCK);

    assertEquals(RESIDENTIALBLOCK, block1.getBlockType());
    assertEquals(new Money(15.0), block1.getLocalLimitTransportationCost());
  }

  @Test
  @DisplayName("Getter for land price works")
  public void testGetLandPrice() {
    Money expectedLandPrice = new Money(7.0);
    assertEquals(expectedLandPrice, residentialBlock.getLandPrice());
  }

  @Test
  @DisplayName("Setter for land price works")
  public void testSetLandPrice() {
    Money newLandPrice = new Money(10000.0, Currency.CHF);
    residentialBlock.setLandPrice(newLandPrice);
    assertEquals(newLandPrice, residentialBlock.getLandPrice());
  }

  @Test
  @DisplayName("Setter for block type restriction works")
  public void testSetBlockTypeRestriction() {
    BlockType newBlockTypeRestriction = BlockType.WATERBLOCK;
    residentialBlock.setBlockTypeRestriction(newBlockTypeRestriction);
    assertEquals(newBlockTypeRestriction, residentialBlock.getBlockTypeRestriction());
  }

  @Test
  @DisplayName(" equals method should return false because other object is null")
  public void testEqualsReturnsFalseObjectNull() {
    ResidentialBlock block1 =
        new ResidentialBlock(
            new Building(1, BuildingType.HOUSE, 100L),
            new Money(100.0, Currency.CHF),
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY);
    ResidentialBlock block2 = null;
    assertFalse(block1.equals(block2));
  }

  @Test
  @DisplayName(" equals method should return false because other object is not the same type")
  public void testEqualsReturnsFalseObjectNotTheSameType() {
    ResidentialBlock block1 =
        new ResidentialBlock(
            new Building(1, BuildingType.HOUSE, 100L),
            new Money(100.0, Currency.CHF),
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY);
    Coordinates block2 = new Coordinates(1, 2);
    assertFalse(block1.equals(block2));
  }

  @Test
  @DisplayName(" equals method should return false because other object is not the same type")
  public void testEqualsReturnsFalseDifferentFields() {
    ResidentialBlock block1 =
        new ResidentialBlock(
            new Building(1, BuildingType.HOUSE, 100L),
            new Money(100.0, Currency.CHF),
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY);

    ResidentialBlock block2 =
        new ResidentialBlock(
            new Building(1, BuildingType.HOUSE, 100L),
            new Money(100.0, Currency.CHF),
            NULL_MONEY,
            new Money(15.0),
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY);

    ResidentialBlock block3 =
        new ResidentialBlock(
            new Building(1, BuildingType.HOUSE, 100L),
            new Money(100.0, Currency.CHF),
            NULL_MONEY,
            NULL_MONEY,
            new Money(15.0),
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY);

    ResidentialBlock block4 =
        new ResidentialBlock(
            new Building(1, BuildingType.HOUSE, 100L),
            new Money(100.0, Currency.CHF),
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            new Money(15.0),
            NULL_MONEY,
            NULL_MONEY);

    ResidentialBlock block6 =
        new ResidentialBlock(
            new Building(1, BuildingType.HOUSE, 100L),
            new Money(100.0, Currency.CHF),
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            new Money(15.0));

    ResidentialBlock block7 =
        new ResidentialBlock(
            new Building(1, BuildingType.HOUSE, 100L),
            NULL_MONEY,
            new Money(100.0),
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY);

    ResidentialBlock block8 =
        new ResidentialBlock(
            new Building(1, BuildingType.HOUSE, 100L),
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY,
            NULL_MONEY);

    assertAll(
        () -> assertFalse(block1.equals(block2)),
        () -> assertFalse(block1.equals(block2)),
        () -> assertFalse(block1.equals(block3)),
        () -> assertFalse(block1.equals(block4)),
        () -> assertFalse(block1.equals(block6)),
        () -> assertFalse(block1.equals(block7)),
        () -> assertFalse(block1.equals(block8)));
  }
}
