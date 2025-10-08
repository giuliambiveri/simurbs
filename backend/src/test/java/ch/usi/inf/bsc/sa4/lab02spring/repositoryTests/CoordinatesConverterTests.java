package ch.usi.inf.bsc.sa4.lab02spring.repositoryTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.usi.inf.bsc.sa4.lab02spring.model.Coordinates;
import ch.usi.inf.bsc.sa4.lab02spring.repository.CoordinatesConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Testing the correct behaviour of the CoordinatesConverter class")
public class CoordinatesConverterTests {

  @Test
  @DisplayName("Method testValidCoordinatesConverter should return '3,90' given Coordinates(3,90)")
  public void testValidCoordinatesConverter() {
    Coordinates coordinates = new Coordinates(3, 90);
    CoordinatesConverter converter = new CoordinatesConverter();
    String result = converter.convert(coordinates);
    assertEquals("Coordinates[x=3, y=90]", result);
  }
}
