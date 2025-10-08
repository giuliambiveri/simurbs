package ch.usi.inf.bsc.sa4.lab02spring.utilsTests;

import static ch.usi.inf.bsc.sa4.lab02spring.model.Currency.CHF;
import static ch.usi.inf.bsc.sa4.lab02spring.utils.CreateSimulationDTOUtils.eliminateResidentialBlockRestrictions;
import static ch.usi.inf.bsc.sa4.lab02spring.utils.CreateSimulationDTOUtils.filterInvalidRestrictions;
import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.CreateSimulationDTO;
import ch.usi.inf.bsc.sa4.lab02spring.model.BlockType;
import ch.usi.inf.bsc.sa4.lab02spring.model.Coordinates;
import ch.usi.inf.bsc.sa4.lab02spring.model.Money;
import ch.usi.inf.bsc.sa4.lab02spring.utils.CreateSimulationDTOUtils;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class CreateSimulationDTOUtilsTests {

  @Test
  @DisplayName(
      "Method isValidCreateSimulationDTO should return true, given a valid CreateSimulationDTO")
  public void testIsValidCreateSimulationDTO() {
    // arrange/setup
    Money avgIncome = new Money(2555.4);
    Money commuterCost = new Money(561.0);
    String name = "Bellinzona";

    // act/exercise
    boolean result =
        CreateSimulationDTOUtils.isValidCreateSimulationDTO(
            new CreateSimulationDTO(avgIncome, commuterCost, name, ""));

    // assert/verify
    assertTrue(result);
  }

  @Test
  @DisplayName(
      "Method isValidCreateSimulationDTO should return false, given a CreateSimulationDTO with invalid fields")
  public void testIsNotValidCreateSimulationDTO() {
    // arrange/setup
    Money avgIncome = new Money(2555.4);
    Money commuterCost = new Money(561.0);
    String name = "Bellinzona";

    // act/exercise
    boolean result =
        CreateSimulationDTOUtils.isValidCreateSimulationDTO(
            new CreateSimulationDTO(
                avgIncome, commuterCost, name, new Money(-10.0), new Money(-10.0), ""));

    // assert/verify
    assertFalse(result);
  }

  @Test
  @DisplayName(
      "Method isValidCreateSimulationDTO should return true, given a valid CreateSimulationDTO with restrictions both equal to 0")
  public void testIsValidCreateSimulationDTOWithRestrictions() {
    // arrange/setup
    Money avgIncome = new Money(2555.4);
    Money commuterCost = new Money(561.0);
    String name = "Bellinzona";
    Money restriction1 = new Money(0.0, CHF);
    Money restriction2 = new Money(0.0, CHF);

    // act/exercise
    boolean result =
        CreateSimulationDTOUtils.isValidCreateSimulationDTO(
            new CreateSimulationDTO(avgIncome, commuterCost, name, restriction1, restriction2, ""));

    // assert/verify
    assertTrue(result);
  }

  @Test
  @DisplayName("Method createValidSimulation should return 2 given that radius is greater than 20")
  public void testCreatesValidSimulation() {
    // arrange/setup
    Money avgIncome = new Money(8273473287.00);
    Money commuterCost = new Money(2374.00);
    String name = "Bellinzona";
    Money restriction1 = new Money(0.0, CHF);
    Money restriction2 = new Money(0.0, CHF);

    // act/exercise
    Integer result =
        CreateSimulationDTOUtils.createsValidSimulation(
            new CreateSimulationDTO(avgIncome, commuterCost, name, restriction1, restriction2, ""));

    // assert/verify
    assertEquals(2, result);
  }

  @Test
  @DisplayName(
      "Method getTransportationCostLimits should return the localTransportCostRestriction value")
  public void testGetTransportationCostLimits() {
    // arrange/setup
    Money avgIncome = new Money(8273473287.00);
    Money commuterCost = new Money(2374.00);
    String name = "Bellinzona";
    Map<Coordinates, Double> localTransportCostRestriction;
    CreateSimulationDTO simulationDTO =
        new CreateSimulationDTO(
            avgIncome,
            commuterCost,
            name,
            "[{\"coordinates\":\"Coordinates[x=2, y=2]\",\"blockType\":\"WATERBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=0, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=0, y=-1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=0, y=1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=0, y=-2]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=0, y=2]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=-1, y=2]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-1, y=1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=1, y=-2]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-1, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=1, y=-1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=1, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=-1, y=-1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=1, y=1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=-1, y=-2]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=1, y=2]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-2, y=2]\",\"blockType\":\"WATERBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-2, y=1]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=2, y=-2]\",\"blockType\":\"WATERBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-2, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=2, y=-1]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=2, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=-2, y=-1]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=2, y=1]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-2, y=-2]\",\"blockType\":\"WATERBLOCK\",\"blockTypeRestriction\":\"NONE\"}]");

    // act/exercise
    localTransportCostRestriction =
        CreateSimulationDTOUtils.getTransportationCostLimits(simulationDTO);
    // assert/verify
    assertNotNull(simulationDTO.restrictionsJSON());
    assertInstanceOf(Map.class, localTransportCostRestriction);
  }

  @Test
  @DisplayName(
      "Method getConstructionCostLimits should return the localTransportCostRestriction value")
  public void testGetConstructionCostLimits() {
    // arrange/setup
    Money avgIncome = new Money(8273473287.00);
    Money commuterCost = new Money(2374.00);
    String name = "Bellinzona";
    Map<Coordinates, Double> localConstructionCostLimits;
    CreateSimulationDTO simulationDTO =
        new CreateSimulationDTO(
            avgIncome,
            commuterCost,
            name,
            "[{\"coordinates\":\"Coordinates[x=2, y=2]\",\"blockType\":\"WATERBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=0, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=0, y=-1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=0, y=1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=0, y=-2]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=0, y=2]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=-1, y=2]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-1, y=1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=1, y=-2]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-1, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=1, y=-1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=1, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=-1, y=-1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=1, y=1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=-1, y=-2]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=1, y=2]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-2, y=2]\",\"blockType\":\"WATERBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-2, y=1]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=2, y=-2]\",\"blockType\":\"WATERBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-2, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=2, y=-1]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=2, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=-2, y=-1]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=2, y=1]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-2, y=-2]\",\"blockType\":\"WATERBLOCK\",\"blockTypeRestriction\":\"NONE\"}]");

    // act/exercise
    localConstructionCostLimits = CreateSimulationDTOUtils.getConstructionCostLimits(simulationDTO);
    // assert/verify
    assertNotNull(simulationDTO.restrictionsJSON());
    assertInstanceOf(Map.class, localConstructionCostLimits);
  }

  @Test
  @DisplayName(
      "Method getConstructionCostLimits has restrictionJSON set to null and returns an empty hashmap")
  public void testGetConstructionCostLimitsNoRestrictionJSON() {
    // arrange/setup
    Money avgIncome = new Money(8273473287.00);
    Money commuterCost = new Money(2374.00);
    String name = "Bellinzona";
    Map<Coordinates, Double> localConstructionCostLimits;
    CreateSimulationDTO simulationDTO =
        new CreateSimulationDTO(avgIncome, commuterCost, name, null);
    // act/exercise
    localConstructionCostLimits = CreateSimulationDTOUtils.getConstructionCostLimits(simulationDTO);
    // assert/verify
    assertNull(simulationDTO.restrictionsJSON());
    assertInstanceOf(Map.class, localConstructionCostLimits);
    assertTrue(localConstructionCostLimits.isEmpty());
  }

  @Test
  @DisplayName(
      "Method getTransportationCostLimits has restrictionJSON set to null and returns an empty hashmap")
  public void testGetTransportationCostLimitsNoRestrictionJSON() {
    // arrange/setup
    Money avgIncome = new Money(8273473287.00);
    Money commuterCost = new Money(2374.00);
    String name = "Bellinzona";
    Map<Coordinates, Double> localTransportationCostLimits;
    CreateSimulationDTO simulationDTO =
        new CreateSimulationDTO(avgIncome, commuterCost, name, null);
    // act/exercise
    localTransportationCostLimits =
        CreateSimulationDTOUtils.getTransportationCostLimits(simulationDTO);
    // assert/verify
    assertNull(simulationDTO.restrictionsJSON());
    assertInstanceOf(Map.class, localTransportationCostLimits);
    assertTrue(localTransportationCostLimits.isEmpty());
  }

  @Test
  @DisplayName("Method GetRentCostLimits should return the localRentGradientRestriction value")
  public void testGetRentCostLimits() {
    // arrange/setup
    Money avgIncome = new Money(8273473287.00);
    Money commuterCost = new Money(2374.00);
    String name = "Bellinzona";
    Map<Coordinates, Double> localRentGradientRestriction;
    CreateSimulationDTO simulationDTO =
        new CreateSimulationDTO(
            avgIncome,
            commuterCost,
            name,
            "[{\"coordinates\":\"Coordinates[x=2, y=2]\",\"blockType\":\"WATERBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=0, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=0, y=-1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=0, y=1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=0, y=-2]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=0, y=2]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=-1, y=2]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-1, y=1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=1, y=-2]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-1, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=1, y=-1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=1, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=-1, y=-1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=1, y=1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=-1, y=-2]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=1, y=2]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-2, y=2]\",\"blockType\":\"WATERBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-2, y=1]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=2, y=-2]\",\"blockType\":\"WATERBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-2, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=2, y=-1]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=2, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=-2, y=-1]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=2, y=1]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-2, y=-2]\",\"blockType\":\"WATERBLOCK\",\"blockTypeRestriction\":\"NONE\"}]");

    // act/exercise
    localRentGradientRestriction = CreateSimulationDTOUtils.getRentCostLimits(simulationDTO);
    // assert/verify
    assertNotNull(simulationDTO.restrictionsJSON());
    assertInstanceOf(Map.class, localRentGradientRestriction);
  }

  @Test
  @DisplayName(
      "Method GetRentCostLimits should return a empty hashmap because restrictionJSON is null")
  public void testGetRentCostLimitsNoRestrictionJSON() {
    // arrange/setup
    Money avgIncome = new Money(8273473287.00);
    Money commuterCost = new Money(2374.00);
    String name = "Bellinzona";
    Map<Coordinates, Double> localRentGradientRestriction;
    CreateSimulationDTO simulationDTO =
        new CreateSimulationDTO(avgIncome, commuterCost, name, null);
    // act/exercise
    localRentGradientRestriction = CreateSimulationDTOUtils.getRentCostLimits(simulationDTO);
    // assert/verify
    assertNull(simulationDTO.restrictionsJSON());
    assertInstanceOf(Map.class, localRentGradientRestriction);
    assertTrue(localRentGradientRestriction.isEmpty());
  }

  @Test
  @DisplayName("Method GetBlockTypeLimits should return the blockTypeRestrictions value")
  public void testGetBlockTypeLimits() {
    // arrange/setup
    Money avgIncome = new Money(8273473287.00);
    Money commuterCost = new Money(2374.00);
    String name = "Bellinzona";
    Map<Coordinates, BlockType> blockTypeRestrictions;
    CreateSimulationDTO simulationDTO =
        new CreateSimulationDTO(
            avgIncome,
            commuterCost,
            name,
            "[{\"coordinates\":\"Coordinates[x=2, y=2]\",\"blockType\":\"WATERBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=0, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=0, y=-1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=0, y=1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=0, y=-2]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=0, y=2]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=-1, y=2]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-1, y=1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=1, y=-2]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-1, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=1, y=-1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=1, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=-1, y=-1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=1, y=1]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=-1, y=-2]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=1, y=2]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-2, y=2]\",\"blockType\":\"WATERBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-2, y=1]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=2, y=-2]\",\"blockType\":\"WATERBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-2, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=2, y=-1]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=2, y=0]\",\"blockType\":\"RESIDENTIALBLOCK\",\"blockTypeRestriction\":\"NONE\",\"localLimitRentCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitConstructionCost\":{\"value\":0,\"currency\":\"CHF\"},\"localLimitTransportationCost\":{\"value\":0,\"currency\":\"CHF\"}},{\"coordinates\":\"Coordinates[x=-2, y=-1]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=2, y=1]\",\"blockType\":\"PARKBLOCK\",\"blockTypeRestriction\":\"NONE\"},{\"coordinates\":\"Coordinates[x=-2, y=-2]\",\"blockType\":\"WATERBLOCK\",\"blockTypeRestriction\":\"NONE\"}]");

    // act/exercise
    blockTypeRestrictions = CreateSimulationDTOUtils.getBlockTypeLimits(simulationDTO);
    // assert/verify
    assertNotNull(simulationDTO.restrictionsJSON());
    assertInstanceOf(Map.class, blockTypeRestrictions);
  }

  @Test
  @DisplayName(
      "Method isValidCreateSimulationDTO should return true, "
          + "given a valid CreateSimulationDTO with name = Varese 1")
  public void testIsValidName() {
    // arrange/setup
    Money avgIncome = new Money(2555.4);
    Money commuterCost = new Money(561.0);
    String name = "Varese 1";

    // act/exercise
    boolean result =
        CreateSimulationDTOUtils.isValidCreateSimulationDTO(
            new CreateSimulationDTO(avgIncome, commuterCost, name, ""));

    // assert/verify
    assertTrue(result);
  }

  @Test
  @DisplayName(
      "Method isValidCreateSimulationDTO should return false, "
          + "given a CreateSimulationDTO with avgIncome = 0.0")
  public void testInvalidAvgIncome() {
    // arrange/setup
    Money avgIncome = new Money(0.0);
    Money commuterCost = new Money(345.0);
    String name = "Gallarate";

    // act/exercise
    boolean result =
        CreateSimulationDTOUtils.isValidCreateSimulationDTO(
            new CreateSimulationDTO(avgIncome, commuterCost, name, ""));

    // assert/verify
    assertFalse(result);
  }

  @Test
  @DisplayName(
      "Method isValidCreateSimulationDTO should return false, "
          + "given a CreateSimulationDTO with commuterCost = 0.0")
  public void testInvalidCommuterCost() {
    // arrange/setup
    Money avgIncome = new Money(1255.4);
    Money commuterCost = new Money(0.0);
    String name = "Taverne";

    // act/exercise
    boolean result =
        CreateSimulationDTOUtils.isValidCreateSimulationDTO(
            new CreateSimulationDTO(avgIncome, commuterCost, name, ""));

    // assert/verify
    assertFalse(result);
  }

  @Test
  @DisplayName(
      "Method isValidCreateSimulationDTO should return false, given a CreateSimulationDTO with name = null")
  public void testNullName() {
    // arrange/setup
    Money avgIncome = new Money(4322.0);
    Money commuterCost = new Money(2000.0);
    String name = null;

    // act/exercise
    boolean result =
        CreateSimulationDTOUtils.isValidCreateSimulationDTO(
            new CreateSimulationDTO(avgIncome, commuterCost, name, ""));

    // assert/verify
    assertFalse(result);
  }

  @Test
  @DisplayName(
      "Method isValidCreateSimulationDTO should return false, "
          + "given a CreateSimulationDTO with name = Va##rese")
  public void testInvalidName() {
    // arrange/setup
    Money avgIncome = new Money(4322.0);
    Money commuterCost = new Money(2000.0);
    String name = "Va##rese";

    // act/exercise
    boolean result =
        CreateSimulationDTOUtils.isValidCreateSimulationDTO(
            new CreateSimulationDTO(avgIncome, commuterCost, name, ""));

    // assert/verify
    assertFalse(result);
  }

  @Nested
  class MapCheckers {
    Map<Coordinates, BlockType> expectedOutput;
    private static final int defaultCityEdge = 8;

    @BeforeEach
    void setUp() {
      expectedOutput = new HashMap<>();
      expectedOutput.put(new Coordinates(-4, 1), BlockType.WATERBLOCK);
      expectedOutput.put(new Coordinates(-1, -5), BlockType.PARKBLOCK);
      expectedOutput.put(new Coordinates(8, -1), BlockType.WATERBLOCK);
      expectedOutput.put(new Coordinates(5, 4), BlockType.PARKBLOCK);
      expectedOutput.put(new Coordinates(-1, -7), BlockType.WATERBLOCK);
      expectedOutput.put(new Coordinates(8, 8), BlockType.PARKBLOCK);
      expectedOutput.put(new Coordinates(-8, -8), BlockType.WATERBLOCK);
    }

    @Test
    @DisplayName(
        "Tests that filterInvalidRestriction removes correctly the entries with coordinates out of bounds")
    public void testFilterInvalidRestrictions() {
      // setup
      Map<Coordinates, BlockType> invalidMap = new HashMap<>(expectedOutput);
      invalidMap.put(new Coordinates(-11, 8), BlockType.RESIDENTIALBLOCK);
      invalidMap.put(new Coordinates(-5, 11), BlockType.RESIDENTIALBLOCK);
      invalidMap.put(new Coordinates(-11, 11), BlockType.RESIDENTIALBLOCK);

      // act
      Map<Coordinates, BlockType> filteredMap =
          filterInvalidRestrictions(defaultCityEdge, invalidMap);

      // assert
      assertEquals(expectedOutput, filteredMap, "Maps do not match expected filtered results.");
    }

    @Test
    @DisplayName(
        "Tests that EliminateResidentialBlockRestrictions actually removes all the RESIDENTIALBLOCK "
            + "restrictions from the type restrictions map")
    public void testEliminateResidentialBlockRestrictions() {
      // setup
      Map<Coordinates, BlockType> blockTypeRestrictions = new HashMap<>(expectedOutput);
      blockTypeRestrictions.put(new Coordinates(-1, 8), BlockType.RESIDENTIALBLOCK);
      blockTypeRestrictions.put(new Coordinates(-5, -1), BlockType.RESIDENTIALBLOCK);
      blockTypeRestrictions.put(new Coordinates(-2, 7), BlockType.RESIDENTIALBLOCK);
      blockTypeRestrictions.put(new Coordinates(0, 0), BlockType.WATERBLOCK);
      blockTypeRestrictions.put(new Coordinates(0, 0), BlockType.PARKBLOCK);

      Map<Coordinates, BlockType> result =
          eliminateResidentialBlockRestrictions(blockTypeRestrictions);
      assertEquals(expectedOutput, result);
    }
  }
}
