package ch.usi.inf.bsc.sa4.lab02spring.modelTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import ch.usi.inf.bsc.sa4.lab02spring.model.Coordinates;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("In the Coordinates model")
public class CoordinatesTests {

  @Test
  @DisplayName("Method toString should return the string representation of coordinates")
  public void testToString() {
    // arrange/setup
    Integer x = 10;
    Integer y = 20;
    Coordinates coordinates = new Coordinates(x, y);
    // act/exercise
    String result = coordinates.toString();
    // assert/verify
    assertEquals(result + "", result);
  }

  @Test
  @DisplayName("Method equals should return true for equal coordinates")
  public void testEqualsForEqualCoordinates() {
    // arrange/setup
    Coordinates coordinates1 = new Coordinates(5, 10);
    Coordinates coordinates2 = new Coordinates(5, 10);
    // act/exercise & assert/verify
    assertEquals(coordinates1, coordinates2);
  }

  @Test
  @DisplayName("Method equals should return false for different coordinates")
  public void testEqualsForDifferentCoordinates() {
    // arrange/setup
    Coordinates coordinates1 = new Coordinates(5, 10);
    Coordinates coordinates2 = new Coordinates(10, 5);
    // act/exercise & assert/verify
    assertNotEquals(coordinates1, coordinates2);
  }

  @Test
  @DisplayName("Method equals should return false when comparing with null")
  public void testEqualsWithNull() {
    // arrange/setup
    Coordinates coordinates = new Coordinates(5, 10);
    // act/exercise & assert/verify
    assertNotEquals(null, coordinates);
  }

  @Test
  @DisplayName("Method equals should return false when comparing with different type")
  public void testEqualsWithDifferentType() {
    // arrange/setup
    Coordinates coordinates = new Coordinates(5, 10);
    // act/exercise & assert/verify
    assertNotEquals("SomeString", coordinates);
  }

  @Test
  @DisplayName("Method x should return the x-coordinate")
  public void testGetX() {
    // arrange/setup
    Integer x = 5;
    Integer y = 10;
    Coordinates coordinates = new Coordinates(x, y);
    // act/exercise
    Integer result = coordinates.x();
    // assert/verify
    assertEquals(x, result);
  }

  @Test
  @DisplayName("Method y should return the y-coordinate")
  public void testGetY() {
    // arrange/setup
    Integer x = 5;
    Integer y = 10;
    Coordinates coordinates = new Coordinates(x, y);
    // act/exercise
    Integer result = coordinates.y();
    // assert/verify
    assertEquals(y, result);
  }

  @Test
  @DisplayName("Method compareTo should return 0 for same coordinates")
  public void testCompareToSameCoordinates() {
    // arrange/setup
    Coordinates coordinates1 = new Coordinates(5, 10);
    Coordinates coordinates2 = new Coordinates(5, 10);

    // act/exercise & assert/verify
    assertEquals(0, coordinates1.compareTo(coordinates2));
  }

  @Test
  @DisplayName("Method compareTo should return -1 when x is different and y is same")
  public void testCompareToDifferentXSameY() {
    // arrange/setup
    Coordinates coordinates1 = new Coordinates(5, 10);
    Coordinates coordinates3 = new Coordinates(10, 5);

    // act/exercise & assert/verify
    assertEquals(-1, coordinates1.compareTo(coordinates3));
  }

  @Test
  @DisplayName("Method compareTo should return 1 when x and y are different")
  public void testCompareToDifferentXandY() {
    // arrange/setup
    Coordinates coordinates1 = new Coordinates(5, 10);
    Coordinates coordinates3 = new Coordinates(10, 5);

    // act/exercise & assert/verify
    assertEquals(1, coordinates3.compareTo(coordinates1));
  }
}
