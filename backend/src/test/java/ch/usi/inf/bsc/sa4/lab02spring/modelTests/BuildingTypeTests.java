package ch.usi.inf.bsc.sa4.lab02spring.modelTests;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.bsc.sa4.lab02spring.model.BuildingType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BuildingTypeTests {

  /** We check if the SKYSCRAPER exists */
  @Test
  @DisplayName("Verify SKYSCRAPER type exists")
  public void testSkyscraperType() {
    assertDoesNotThrow(
        () -> {
          BuildingType type = BuildingType.SKYSCRAPER;
          assertNotNull(type, "SKYSCRAPER type should exist");
        });
  }

  /** We check if the House exists */
  @Test
  @DisplayName("Verify HOUSE type exists")
  public void testHouseType() {
    assertDoesNotThrow(
        () -> {
          BuildingType type = BuildingType.HOUSE;
          assertNotNull(type, "HOUSE type should exist");
        });
  }

  /** We check if the CONDO exists */
  @Test
  @DisplayName("Verify CONDO type exists")
  public void testCondoType() {
    assertDoesNotThrow(
        () -> {
          BuildingType type = BuildingType.CONDO;
          assertNotNull(type, "CONDO type should exist");
        });
  }
}
