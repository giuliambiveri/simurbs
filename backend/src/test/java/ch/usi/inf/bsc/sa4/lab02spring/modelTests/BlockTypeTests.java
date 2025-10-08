package ch.usi.inf.bsc.sa4.lab02spring.modelTests;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.bsc.sa4.lab02spring.model.BlockType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("In the BlockType model")
public class BlockTypeTests {

  @Test
  @DisplayName("Enum PARKBLOCK should return 'PARKBLOCK' as string representation")
  public void testParkBlockToString() {
    // act/exercise
    String result = BlockType.PARKBLOCK.toString();
    // assert/verify
    assertEquals("PARKBLOCK", result);
  }

  @Test
  @DisplayName("Enum WATERBLOCK should return 'WATERBLOCK' as string representation")
  public void testWaterBlockToString() {
    // act/exercise
    String result = BlockType.WATERBLOCK.toString();
    // assert/verify
    assertEquals("WATERBLOCK", result);
  }

  @Test
  @DisplayName("Enum RESIDENTIALBLOCK should return 'RESIDENTIALBLOCK' as string representation")
  public void testResidentialBlockToString() {
    // act/exercise
    String result = BlockType.RESIDENTIALBLOCK.toString();
    // assert/verify
    assertEquals("RESIDENTIALBLOCK", result);
  }

  @Test
  @DisplayName("Enum values should return correct array of block types")
  public void testValues() {
    // act/exercise
    BlockType[] result = BlockType.values();
    // assert/verify
    assertEquals(4, result.length);
    assertEquals(BlockType.PARKBLOCK, result[0]);
    assertEquals(BlockType.WATERBLOCK, result[1]);
    assertEquals(BlockType.RESIDENTIALBLOCK, result[2]);
    assertEquals(BlockType.NONE, result[3]);
  }

  @Test
  @DisplayName("Enum valueOf should return correct block type")
  public void testValueOf() {
    // act/exercise
    BlockType result = BlockType.valueOf("PARKBLOCK");
    // assert/verify
    assertEquals(BlockType.PARKBLOCK, result);
  }

  @Test
  @DisplayName("Enum valueOf should throw IllegalArgumentException for non-existent block type")
  public void testValueOfWithInvalidType() {
    // assert/verify
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          // act/exercise
          BlockType.valueOf("INVALID_BLOCK_TYPE");
        });
  }

  @Test
  @DisplayName("Enum equality check should return true for equal block types")
  public void testEqualityForEqualBlockTypes() {
    // act/exercise & assert/verify
    assertEquals(BlockType.PARKBLOCK, BlockType.PARKBLOCK);
  }

  @Test
  @DisplayName("Enum equality check should return false for different block types")
  public void testEqualityForDifferentBlockTypes() {
    // act/exercise & assert/verify
    assertNotEquals(BlockType.PARKBLOCK, BlockType.WATERBLOCK);
  }
}
