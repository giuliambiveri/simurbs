package ch.usi.inf.bsc.sa4.lab02spring.modelTests;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.bsc.sa4.lab02spring.model.BlockType;
import ch.usi.inf.bsc.sa4.lab02spring.model.Coordinates;
import ch.usi.inf.bsc.sa4.lab02spring.model.NonResidentialBlock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("In the NonResidentialBlock class")
public class NonResidentialBlockTests {

  @Test
  @DisplayName("method getBlocktype should return the type of the park block")
  public void parkBlockCase() {
    // arrange/setup
    BlockType Park = BlockType.PARKBLOCK;
    // act/exercise
    BlockType ParkType = new NonResidentialBlock(Park).getBlockType();
    // assert/verify
    assertEquals(BlockType.PARKBLOCK, ParkType);
  }

  @Test
  @DisplayName("method getBlocktype should return the type of the water block")
  public void waterBlockCase() {
    // arrange/setup
    BlockType Water = BlockType.WATERBLOCK;
    // act/exercise
    BlockType WaterType = new NonResidentialBlock(Water).getBlockType();
    // assert/verify
    assertEquals(BlockType.WATERBLOCK, WaterType);
  }

  @Test
  @DisplayName(
      "equals method should correctly identify two non-residential blocks as equal when they have the "
          + "same type")
  public void testEqualsMethodSameType() {
    // arrange
    NonResidentialBlock block1 = new NonResidentialBlock(BlockType.PARKBLOCK);
    NonResidentialBlock block2 = new NonResidentialBlock(BlockType.PARKBLOCK);

    // act & assert
    assertEquals(block1, block2, "Blocks with the same type should be considered equal.");
  }

  @Test
  @DisplayName(
      "equals method should correctly identify two non-residential blocks as not equal when they have "
          + "different types")
  public void testEqualsMethodDifferentType() {
    // arrange
    NonResidentialBlock block1 = new NonResidentialBlock(BlockType.PARKBLOCK);
    NonResidentialBlock block2 = new NonResidentialBlock(BlockType.WATERBLOCK);

    // act & assert
    assertNotEquals(block1, block2, "Blocks with different types should not be considered equal.");
  }

  @Test
  @DisplayName("Test that the secondary constructor works properly")
  public void testSecondaryConstructor() {
    NonResidentialBlock block1 = new NonResidentialBlock(BlockType.PARKBLOCK, BlockType.WATERBLOCK);
    assertNotNull(block1);
  }

  @Test
  @DisplayName("Test that the setType sets correctly the type")
  public void testSetType() {
    NonResidentialBlock block1 = new NonResidentialBlock(BlockType.PARKBLOCK);
    block1.setType(BlockType.WATERBLOCK);
    assertEquals(BlockType.WATERBLOCK, block1.getBlockType());
  }

  @Test
  @DisplayName(
      "Test that the method getBlockTypeRestriction returns the correct block type restriction")
  public void testGetBlockTypeRestriction() {
    NonResidentialBlock block1 = new NonResidentialBlock(BlockType.PARKBLOCK, BlockType.WATERBLOCK);
    assertEquals(block1.getBlockType(), BlockType.PARKBLOCK);
  }

  @Test
  @DisplayName("Test that equals method returns true if the object reference are the same")
  public void testEqualsMethodSameObject() {
    NonResidentialBlock block1 = new NonResidentialBlock(BlockType.PARKBLOCK, BlockType.WATERBLOCK);
    assertEquals(block1, block1);
  }

  @Test
  @DisplayName("Test that equals method returns false if the object null")
  public void testEqualsMethodNullObject() {
    NonResidentialBlock block1 = new NonResidentialBlock(BlockType.PARKBLOCK, BlockType.WATERBLOCK);
    assertNotEquals(null, block1);
  }

  @Test
  @DisplayName("Test that equals method returns false if the object is not of the same instance")
  public void testEqualsMethodDifferentObject() {
    NonResidentialBlock block1 = new NonResidentialBlock(BlockType.PARKBLOCK, BlockType.WATERBLOCK);
    Coordinates block2 = new Coordinates(1, 2);
    assertNotEquals(block1, block2);
  }
}
