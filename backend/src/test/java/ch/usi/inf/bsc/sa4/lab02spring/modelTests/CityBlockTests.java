package ch.usi.inf.bsc.sa4.lab02spring.modelTests;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.bsc.sa4.lab02spring.model.*;
import java.security.SecureRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CityBlock class tests")
public class CityBlockTests {
  private static final SecureRandom random = new SecureRandom();

  @Test
  @DisplayName("The getter for block type")
  public void testGetBlockType() {
    CityBlock cityBlock =
        new ResidentialBlock(
            new Building(1, BuildingType.SKYSCRAPER, (long) random.nextInt(10000)),
            new Money(0.0, Currency.CHF),
            new Money(0.0, Currency.CHF),
            new Money(0.0, Currency.CHF),
            new Money(0.0, Currency.CHF),
            new Money(0.0, Currency.CHF),
            new Money(0.0, Currency.CHF),
            new Money(0.0, Currency.CHF));
    assertDoesNotThrow(() -> assertEquals(BlockType.RESIDENTIALBLOCK, cityBlock.getBlockType()));
  }
}
