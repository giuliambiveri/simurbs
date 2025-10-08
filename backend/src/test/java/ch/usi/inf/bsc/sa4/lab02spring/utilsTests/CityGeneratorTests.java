package ch.usi.inf.bsc.sa4.lab02spring.utilsTests;

import static ch.usi.inf.bsc.sa4.lab02spring.utils.CityGenerator.*;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.bsc.sa4.lab02spring.model.*;
import ch.usi.inf.bsc.sa4.lab02spring.utils.CityCreationParameters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Test the CityGenerator class")
public class CityGeneratorTests {

  /** Default null money value */
  private static final Money nullMoney = new Money(0.0);

  /** coefficient for converting km^2 in m^2 */
  private static final int CONVERSION_COEFFICIENT = 1_000_000;

  private static final Integer defaultDistance = 0;
  private static final Double defaultWage = 6000.0;
  private static final Double defaultCommuterCost = 200.0;
  private static final Double nullCommuterCost = 0.0;
  private static final int defaultCityEdge = 14;
  private static final Coordinates defaultCoords = new Coordinates(0, 0);
  private Map<Coordinates, Integer> heightOnDistance;
  private Map<Coordinates, Double> rentOnDistance;
  private Map<Coordinates, Double> landPricePerBlock;
  private Map<Coordinates, Long> densityPerBlock;
  private Map<Coordinates, Double> localTransportationCostRestrictions;
  private Map<Coordinates, Double> localConstructionCostRestriction;
  private Map<Coordinates, Double> localRentGradientRestrictions;

  @BeforeEach
  public void setUp() {
    heightOnDistance = new HashMap<>();
    rentOnDistance = new HashMap<>();
    densityPerBlock = new HashMap<>();
    landPricePerBlock = new HashMap<>();
    localTransportationCostRestrictions = new HashMap<>();
    localConstructionCostRestriction = new HashMap<>();
    localRentGradientRestrictions = new HashMap<>();
  }

  static Stream<Arguments> housingRentArguments() {
    return Stream.of(
        Arguments.of(0, 6000.0, 200.0, 28.528468),
        Arguments.of(1, 6000.0, 200.0, 25.813625),
        Arguments.of(2, 6000.0, 200.0, 23.357134),
        Arguments.of(3, 6000.0, 200.0, 21.134409),
        Arguments.of(4, 6000.0, 200.0, 19.123204),
        Arguments.of(5, 6000.0, 200.0, 17.303390),
        Arguments.of(6, 6000.0, 200.0, 15.656755),
        Arguments.of(7, 6000.0, 200.0, 14.166818),
        Arguments.of(8, 6000.0, 200.0, 12.818667),
        Arguments.of(0, 7525.0, 2872.0, 61.154439),
        Arguments.of(1, 6931.0, 4753.0, 4.220284),
        Arguments.of(0, 1684.0, 2278.0, 3.296636),
        Arguments.of(1, 2377.0, 2476.0, 1.351753),
        Arguments.of(2, 8416.0, 2278.0, 9.785507),
        Arguments.of(1, 10000.0, 793.0, 141.797851),
        Arguments.of(18, 8317.0, 397.0, 2.550788),
        Arguments.of(1, 7921.0, 397.0, 61.123870),
        Arguments.of(2, 991.0, 1.0, 2.328915),
        Arguments.of(0, 1684.0, 991.0, 3.296636),
        Arguments.of(1, 8317.0, 1189.0, 50.144180),
        Arguments.of(20, 6535.0, 100.0, 13.713798),
        Arguments.of(3, 3763.0, 1189.0, 1.566592),
        Arguments.of(0, 3961.0, 496.0, 10.292366),
        Arguments.of(0, 3664.0, 4753.0, 8.872020),
        Arguments.of(0, 1981.0, 2278.0, 3.824403),
        Arguments.of(1, 4555.0, 1.0, 13.844700),
        Arguments.of(2, 8020.0, 3070.0, 3.636066),
        Arguments.of(3, 6634.0, 1.0, 39.110949),
        Arguments.of(0, 100.0, 3862.0, 1.493172),
        Arguments.of(17, 9604.0, 1.0, 171.468994));
  }

  static Stream<Arguments> efficiencyHeightArgumentsWithNoRestrictions() {
    return Stream.of(
        Arguments.of(0, 6000.0, 200.0, new Coordinates(0, 0), emptyMap(), emptyMap(), 0.0, 150),
        Arguments.of(1, 6000.0, 200.0, new Coordinates(0, 0), emptyMap(), emptyMap(), 0.0, 150),
        Arguments.of(2, 6000.0, 200.0, new Coordinates(0, 0), emptyMap(), emptyMap(), 0.0, 150),
        Arguments.of(3, 6000.0, 200.0, new Coordinates(0, 0), emptyMap(), emptyMap(), 0.0, 150),
        Arguments.of(4, 6000.0, 200.0, new Coordinates(0, 0), emptyMap(), emptyMap(), 0.0, 136),
        Arguments.of(5, 6000.0, 200.0, new Coordinates(0, 0), emptyMap(), emptyMap(), 0.0, 111),
        Arguments.of(6, 6000.0, 200.0, new Coordinates(0, 0), emptyMap(), emptyMap(), 0.0, 91),
        Arguments.of(7, 6000.0, 200.0, new Coordinates(0, 0), emptyMap(), emptyMap(), 0.0, 75),
        Arguments.of(8, 6000.0, 200.0, new Coordinates(0, 0), emptyMap(), emptyMap(), 0.0, 61),
        Arguments.of(0, 8812.0, 4258.0, new Coordinates(0, 0), emptyMap(), emptyMap(), 0.0, 150),
        Arguments.of(1, 9505.0, 1981.0, new Coordinates(0, 0), emptyMap(), emptyMap(), 0.0, 150),
        Arguments.of(15, 9208.0, 298.0, new Coordinates(0, 0), emptyMap(), emptyMap(), 0.0, 86),
        Arguments.of(1, 7624.0, 1288.0, new Coordinates(0, 0), emptyMap(), emptyMap(), 0.0, 150),
        Arguments.of(0, 6832.0, 2278.0, new Coordinates(0, 0), emptyMap(), emptyMap(), 0.0, 150),
        Arguments.of(13, 6733.0, 100.0, new Coordinates(0, 0), emptyMap(), emptyMap(), 0.0, 150));
  }

  static Stream<Arguments> cityEdgeArguments() {
    return Stream.of(
        Arguments.of(6000.0, 200.0, 14),
        Arguments.of(14500.00, 4501.00, 2),
        Arguments.of(73000.00, 3001.00, 23),
        Arguments.of(46500.00, 1.00, 43469),
        Arguments.of(90500.00, 2501.00, 34),
        Arguments.of(82000.00, 4501.00, 17),
        Arguments.of(78500.00, 3501.00, 21),
        Arguments.of(49500.00, 5001.00, 9),
        Arguments.of(33000.00, 4001.00, 7),
        Arguments.of(10000.00, 501.00, 13),
        Arguments.of(68500.00, 501.00, 130),
        Arguments.of(46000.00, 1001.00, 42),
        Arguments.of(43000.00, 501.00, 79),
        Arguments.of(71500.00, 3501.00, 19),
        Arguments.of(72000.00, 2001.00, 34),
        Arguments.of(21000.00, 4001.00, 4),
        Arguments.of(43000.00, 4001.00, 9),
        Arguments.of(63000.00, 2001.00, 29),
        Arguments.of(91000.00, 3501.00, 25),
        Arguments.of(95000.00, 3501.00, 26),
        Arguments.of(62000.00, 5001.00, 11),
        Arguments.of(16500.00, 1.00, 13469),
        Arguments.of(88000.00, 3001.00, 28),
        Arguments.of(46000.00, 3001.00, 14),
        Arguments.of(28000.00, 5001.00, 4),
        Arguments.of(65500.00, 4001.00, 15),
        Arguments.of(35500.00, 1.00, 32469),
        Arguments.of(46000.00, 1.00, 42969),
        Arguments.of(58000.00, 501.00, 109),
        Arguments.of(17000.00, 501.00, 27));
  }

  static Stream<Arguments> densityGradientArguments() {
    return Stream.of(
        Arguments.of(0, 6000.0, 200.0, 0.012900),
        Arguments.of(1, 6000.0, 200.0, 0.009556),
        Arguments.of(2, 6000.0, 200.0, 0.007080),
        Arguments.of(3, 6000.0, 200.0, 0.005245),
        Arguments.of(4, 6000.0, 200.0, 0.003886),
        Arguments.of(5, 6000.0, 200.0, 0.002879),
        Arguments.of(6, 6000.0, 200.0, 0.002133),
        Arguments.of(7, 6000.0, 200.0, 0.001580),
        Arguments.of(8, 6000.0, 200.0, 0.001171),
        Arguments.of(9, 6000.0, 200.0, 0.000867),
        Arguments.of(10, 6000.0, 200.0, 0.000643),
        Arguments.of(11, 6000.0, 200.0, 0.000476),
        Arguments.of(12, 6000.0, 200.0, 0.000353),
        Arguments.of(13, 6000.0, 200.0, 0.000262),
        Arguments.of(14, 6000.0, 200.0, 0.000194),
        Arguments.of(3, 10000.0, 2080.0, 0.000448),
        Arguments.of(18, 5149.0, 1.0, 0.003503),
        Arguments.of(0, 5347.0, 2476.0, 0.004844),
        Arguments.of(15, 9109.0, 100.0, 0.144123),
        Arguments.of(0, 4258.0, 4456.0, 0.000946),
        Arguments.of(19, 4852.0, 100.0, 0.000133),
        Arguments.of(2, 8020.0, 1981.0, 0.000701),
        Arguments.of(0, 7228.0, 2377.0, 0.081383),
        Arguments.of(0, 9505.0, 2080.0, 2.476658),
        Arguments.of(0, 9901.0, 496.0, 4.485770),
        Arguments.of(19, 9109.0, 199.0, 0.004708),
        Arguments.of(2, 9703.0, 3070.0, 0.000333),
        Arguments.of(8, 7426.0, 1.0, 0.108220),
        Arguments.of(1, 9406.0, 1288.0, 0.309255),
        Arguments.of(0, 5743.0, 4258.0, 0.008773),
        Arguments.of(8, 7822.0, 595.0, 0.000157),
        Arguments.of(5, 8317.0, 397.0, 0.021225),
        Arguments.of(0, 5545.0, 2674.0, 0.006519),
        Arguments.of(5, 5941.0, 100.0, 0.005577),
        Arguments.of(0, 6535.0, 2872.0, 0.028780),
        Arguments.of(0, 4456.0, 4060.0, 0.001273));
  }

  static Stream<Arguments> landPriceArgumentsWithNoRestriction() {
    return Stream.of(
        Arguments.of(0, 6000.0, 200.0, new Coordinates(0, 0), emptyMap(), 8.599468),
        Arguments.of(1, 6000.0, 200.0, new Coordinates(0, 0), emptyMap(), 6.370642),
        Arguments.of(2, 6000.0, 200.0, new Coordinates(0, 0), emptyMap(), 4.719488),
        Arguments.of(3, 6000.0, 200.0, new Coordinates(0, 0), emptyMap(), 3.496283),
        Arguments.of(4, 6000.0, 200.0, new Coordinates(0, 0), emptyMap(), 2.590110),
        Arguments.of(5, 6000.0, 200.0, new Coordinates(0, 0), emptyMap(), 1.918801),
        Arguments.of(6, 6000.0, 200.0, new Coordinates(0, 0), emptyMap(), 1.421482),
        Arguments.of(7, 6000.0, 200.0, new Coordinates(0, 0), emptyMap(), 1.053060),
        Arguments.of(8, 6000.0, 200.0, new Coordinates(0, 0), emptyMap(), 0.780126),
        Arguments.of(11, 4456.0, 298.0, new Coordinates(0, 0), emptyMap(), 0.006212),
        Arguments.of(14, 6139.0, 298.0, new Coordinates(0, 0), emptyMap(), 0.020286),
        Arguments.of(14, 4060.0, 100.0, new Coordinates(0, 0), emptyMap(), 0.057366),
        Arguments.of(3, 9208.0, 298.0, new Coordinates(0, 0), emptyMap(), 276.635381),
        Arguments.of(9, 6337.0, 100.0, new Coordinates(0, 0), emptyMap(), 3.695812),
        Arguments.of(8, 5941.0, 496.0, new Coordinates(0, 0), emptyMap(), 0.020470));
  }

  static Stream<Arguments> createResidentialBlockArguments() {
    return Stream.of(
        Arguments.of(
            new Coordinates(-8, 8),
            10,
            10.0,
            new ResidentialBlock(
                new Building(10, BuildingType.CONDO, (long) 10 * 20),
                new Money(10.0, Currency.CHF),
                new Money(5.0, Currency.CHF),
                nullMoney,
                nullMoney,
                nullMoney,
                nullMoney,
                nullMoney)),
        Arguments.of(
            new Coordinates(8, -8),
            11,
            11.0,
            new ResidentialBlock(
                new Building(11, BuildingType.CONDO, (long) 11 * 20),
                new Money(11.0, Currency.CHF),
                new Money(6.0, Currency.CHF),
                nullMoney,
                nullMoney,
                nullMoney,
                nullMoney,
                nullMoney)),
        Arguments.of(
            new Coordinates(-8, -8),
            12,
            2.0,
            new ResidentialBlock(
                new Building(12, BuildingType.CONDO, (long) 12 * 20),
                new Money(2.0, Currency.CHF),
                new Money(1.0, Currency.CHF),
                nullMoney,
                nullMoney,
                nullMoney,
                nullMoney,
                nullMoney)),
        Arguments.of(
            new Coordinates(8, 8),
            13,
            3.0,
            new ResidentialBlock(
                new Building(13, BuildingType.CONDO, (long) 13 * 20),
                new Money(3.0, Currency.CHF),
                new Money(2.0, Currency.CHF),
                nullMoney,
                nullMoney,
                nullMoney,
                nullMoney,
                nullMoney)),
        Arguments.of(
            new Coordinates(0, 0),
            14,
            0.0,
            new ResidentialBlock(
                new Building(14, BuildingType.SKYSCRAPER, (long) 14 * 20),
                new Money(0.0, Currency.CHF),
                new Money(0.0, Currency.CHF),
                nullMoney,
                nullMoney,
                nullMoney,
                nullMoney,
                nullMoney)));
  }

  static Stream<Arguments> generateNonResidentialBlocksArguments() {
    return Stream.of(
        Arguments.of(new Coordinates(14, 14), BlockType.WATERBLOCK),
        Arguments.of(new Coordinates(7, 6), BlockType.PARKBLOCK),
        Arguments.of(new Coordinates(14, -14), BlockType.WATERBLOCK),
        Arguments.of(new Coordinates(7, -6), BlockType.PARKBLOCK),
        Arguments.of(new Coordinates(6, 7), BlockType.PARKBLOCK),
        Arguments.of(new Coordinates(14, 14), BlockType.WATERBLOCK),
        Arguments.of(new Coordinates(6, -7), BlockType.PARKBLOCK),
        Arguments.of(new Coordinates(-14, 14), BlockType.WATERBLOCK),
        Arguments.of(new Coordinates(-6, 6), BlockType.PARKBLOCK),
        Arguments.of(new Coordinates(-7, 6), BlockType.PARKBLOCK),
        Arguments.of(new Coordinates(-6, -6), BlockType.PARKBLOCK),
        Arguments.of(new Coordinates(-7, -6), BlockType.PARKBLOCK),
        Arguments.of(new Coordinates(-6, 7), BlockType.PARKBLOCK),
        Arguments.of(new Coordinates(-6, 6), BlockType.PARKBLOCK),
        Arguments.of(new Coordinates(-6, -7), BlockType.PARKBLOCK),
        Arguments.of(new Coordinates(-14, -14), BlockType.WATERBLOCK));
  }

  @ParameterizedTest(
      name = "given distance: {0}, wage: {1} and commuter cost: {2}, the housing cost is: {3}")
  @MethodSource("housingRentArguments")
  void testHousingRent(int distance, double wage, double commuterCost, double expectedPrice) {
    double calculatedPrice = housingRent(distance, wage, commuterCost);
    assertEquals(
        expectedPrice,
        calculatedPrice,
        1e-6,
        "The calculated housing cost should match the expected value");
  }

  @ParameterizedTest(
      name =
          "given distance: {0}, wage: {1}, commuter cost: {2}, and no restrictions, the efficient height for the "
              + "building is: {5}")
  @MethodSource("efficiencyHeightArgumentsWithNoRestrictions")
  public void testEfficiencyHeightWithNoRestrictions(
      int distance,
      Double wage,
      Double commuterCost,
      Coordinates coords,
      Map<Coordinates, Double> localTransportationCostRestrictionsParam,
      Map<Coordinates, Double> localConstructionCostRestrictionParam,
      Double globalConstructionCostRestriction,
      int expectedOutput) {
    int result =
        efficiencyHeight(
            distance,
            wage,
            commuterCost,
            coords,
            localTransportationCostRestrictionsParam,
            localConstructionCostRestrictionParam,
            globalConstructionCostRestriction);
    assertEquals(expectedOutput, result, "The calculated height did not match the expected value.");
  }

  @Test
  @DisplayName(
      "Test that the effciencyHeight method overrules the commuterCost and applies the local "
          + "commuter cost restriction")
  public void testEfficiencyHeightWithLocalTransportationRestrictions() {
    Double globalConstructionCostRestriction = 0.0;
    localTransportationCostRestrictions.put(defaultCoords, defaultCommuterCost);
    int expectedOutput = 150;
    int result =
        efficiencyHeight(
            defaultDistance,
            defaultWage,
            nullCommuterCost,
            defaultCoords,
            localTransportationCostRestrictions,
            localConstructionCostRestriction,
            globalConstructionCostRestriction);
    assertEquals(
        expectedOutput,
        result,
        "The calculated height does not reflect the local transportation restrictions.");
  }

  @Test
  @DisplayName(
      "Test that the effciencyHeight method overrules the calculated height and applies the local "
          + "construction restriction")
  public void testEfficiencyHeightWithLocalConstructionRestrictionsV1() {
    Double globalConstructionCostRestriction = 0.0;
    localConstructionCostRestriction.put(defaultCoords, 2000.0);
    int expectedOutput = 7;
    int result =
        efficiencyHeight(
            defaultDistance,
            defaultWage,
            nullCommuterCost,
            defaultCoords,
            localTransportationCostRestrictions,
            localConstructionCostRestriction,
            globalConstructionCostRestriction);
    assertEquals(
        expectedOutput,
        result,
        "The calculated height does not reflect the local construction restrictions.");
  }

  @Test
  @DisplayName(
      "Test that the effciencyHeight method does not overrule the calculated height because the imposed local "
          + "construction restriction is higher than the one calculated")
  public void testEfficiencyHeightWithLocalConstructionRestrictionsV2() {
    Double globalConstructionCostRestriction = 0.0;
    localConstructionCostRestriction.put(defaultCoords, 200000.0);
    int expectedOutput = 150;
    int result =
        efficiencyHeight(
            defaultDistance,
            defaultWage,
            nullCommuterCost,
            defaultCoords,
            localTransportationCostRestrictions,
            localConstructionCostRestriction,
            globalConstructionCostRestriction);
    assertEquals(
        expectedOutput,
        result,
        "The calculated height should not be determined by the local construction restrictions");
  }

  /**
   * This test has a limit that needs to be applied as it applies a height that is lower than the
   * one calculated
   */
  @Test
  @DisplayName(
      "Test that the effciencyHeight method overrules the calculated height and applies the global "
          + "construction restriction")
  public void testEfficiencyHeightWithGlobalConstructionRestrictionsV1() {
    Double globalConstructionCostRestriction = 2000.0;
    int expectedOutput = 7;
    int result =
        efficiencyHeight(
            defaultDistance,
            defaultWage,
            nullCommuterCost,
            defaultCoords,
            localTransportationCostRestrictions,
            localConstructionCostRestriction,
            globalConstructionCostRestriction);
    assertEquals(
        expectedOutput,
        result,
        "The calculated height does not reflect the global construction restrictions.");
  }

  @Test
  @DisplayName(
      "Test that the effciencyHeight method does not overrule the calculated height because the imposed global "
          + "construction restriction is higher than the one calculated")
  public void testEfficiencyHeightWithGlobalConstructionRestrictionsV2() {
    Double globalConstructionCostRestriction = 200000.0;
    int expectedOutput = 150;
    int result =
        efficiencyHeight(
            defaultDistance,
            defaultWage,
            nullCommuterCost,
            defaultCoords,
            localTransportationCostRestrictions,
            localConstructionCostRestriction,
            globalConstructionCostRestriction);
    assertEquals(
        expectedOutput,
        result,
        "The calculated height should not be determined by the global restrictions");
  }

  @Test
  @DisplayName(
      "Test that the effciencyHeight method overrules the global restriction and applies the local "
          + "construction restriction")
  public void testEfficiencyHeightWithGlobalAndLocalConstructionRestrictions() {
    final double globalConstructionCostRestriction = 1500.0;
    localConstructionCostRestriction.put(defaultCoords, 2000.0);
    int expectedOutput = 7;
    int result =
        efficiencyHeight(
            defaultDistance,
            defaultWage,
            nullCommuterCost,
            defaultCoords,
            localTransportationCostRestrictions,
            localConstructionCostRestriction,
            globalConstructionCostRestriction);
    assertEquals(
        expectedOutput,
        result,
        "The calculated height does not reflect the global construction restrictions.");
  }

  @ParameterizedTest(
      name = "given distance: {0}, wage: {1} and commuter cost: {2}, the rent gradient is: {3}")
  @MethodSource("landPriceArgumentsWithNoRestriction")
  public void testLandPriceWithNoRestriction(
      int distance,
      Double wage,
      Double commuterCost,
      Coordinates coords,
      Map<Coordinates, Double> localTransportationCostRestrictionsParam,
      double expectedOutput) {
    double result =
        landPrice(distance, wage, commuterCost, coords, localTransportationCostRestrictionsParam);
    assertEquals(
        expectedOutput,
        result,
        1e-6,
        "The calculated rent gradient did not match the expected value.");
  }

  @Test
  @DisplayName(
      "Test that the rentGradient method overrules the commuterCost and applies the local "
          + "commuter cost restriction")
  public void testRentGradientWithLocalTransportationRestrictions() {
    localTransportationCostRestrictions.put(defaultCoords, 200.0);
    double expectedOutput = 8.599468;
    double result =
        landPrice(
            defaultDistance,
            defaultWage,
            nullCommuterCost,
            defaultCoords,
            localTransportationCostRestrictions);
    assertEquals(
        expectedOutput,
        result,
        1e-6,
        "The calculated rent gradient did not respect the local transportation restrictions.");
  }

  @ParameterizedTest(
      name = "given distance: {0}, wage: {1} and commuter cost: {2}, the rent cost is: {3}")
  @MethodSource("housingRentArguments")
  public void testRentCostCalculatorWithNoRestriction(
      int distance, Double wage, Double commuterCost, double expectedOutput) {
    final Coordinates coords = new Coordinates(0, 0);
    final Map<Coordinates, Double> localTransportationCostRestrictionsParam = new HashMap<>();
    final Map<Coordinates, Double> localRentGradientRestrictionsParam = new HashMap<>();
    double result =
        rentCostCalculator(
            distance,
            wage,
            commuterCost,
            coords,
            localTransportationCostRestrictionsParam,
            localRentGradientRestrictionsParam,
            0.0);
    assertEquals(
        expectedOutput,
        result,
        1e-6,
        "The calculated rent gradient did not match the expected value.");
  }

  @Test
  @DisplayName(
      "Test that the rentCostCalculator method overrules the commuterCost and applies the local "
          + "commuter cost restriction")
  public void testRentCostCalculatorWithLocalTransportationRestrictions() {
    localTransportationCostRestrictions.put(defaultCoords, 200.0);
    double expectedOutput = 28.528467;
    double result =
        rentCostCalculator(
            defaultDistance,
            defaultWage,
            nullCommuterCost,
            defaultCoords,
            localTransportationCostRestrictions,
            localRentGradientRestrictions,
            0.0);
    assertEquals(
        expectedOutput,
        result,
        1e-6,
        "The calculated rent cost did not respect the local transportation restrictions.");
  }

  /**
   * This test has a limit that needs to be applied as it has a rent gradient that is lower than the
   * one calculated
   */
  @Test
  @DisplayName(
      "Test that the rentCostCalculator method overrules the calculated rent and applies the local "
          + "rent restriction")
  public void testRentCostCalculatorWithLocalRentGradientRestrictionsV1() {
    localRentGradientRestrictions.put(defaultCoords, 8.0);
    double expectedOutput = 8.0;
    double result =
        rentCostCalculator(
            defaultDistance,
            defaultWage,
            nullCommuterCost,
            defaultCoords,
            localTransportationCostRestrictions,
            localRentGradientRestrictions,
            0.0);
    assertEquals(
        expectedOutput,
        result,
        "The calculated rent cost did not respect the local rent gradient restrictions.");
  }

  @Test
  @DisplayName(
      "Test that the rentCostCalculator method does not overrule the calculated rent because the imposed local limit is "
          + "greater")
  public void testRentCostCalculatorWithLocalRentGradientRestrictionsV2() {
    localRentGradientRestrictions.put(defaultCoords, 200.0);
    double expectedOutput = 28.528467;
    double result =
        rentCostCalculator(
            defaultDistance,
            defaultWage,
            nullCommuterCost,
            defaultCoords,
            localTransportationCostRestrictions,
            localRentGradientRestrictions,
            0.0);
    assertEquals(
        expectedOutput,
        result,
        1e-6,
        "The calculated rent cost should not be limited by the restrictions imposed.");
  }

  @Test
  @DisplayName(
      "Test that the rentCostCalculator method does overrule the calculated rent because the imposed global limit is "
          + "less")
  public void testRentCostCalculatorWithGlobalRentCostRestrictionsV1() {
    final double globalRentCostRestriction = 8.0;
    final double expectedOutput = 8.0;
    final double result =
        rentCostCalculator(
            defaultDistance,
            defaultWage,
            nullCommuterCost,
            defaultCoords,
            localTransportationCostRestrictions,
            localRentGradientRestrictions,
            globalRentCostRestriction);
    assertEquals(expectedOutput, result, "The rent cost should match the global restriction.");
  }

  @Test
  @DisplayName(
      "Test that the rentCostCalculator method does not overrule the calculated rent because the imposed global limit is "
          + "greater")
  public void testRentCostCalculatorWithGlobalRentCostRestrictionsV2() {
    final double globalRentCostRestriction = 29;
    final double expectedOutput = 28.528467;
    final double result =
        rentCostCalculator(
            defaultDistance,
            defaultWage,
            nullCommuterCost,
            defaultCoords,
            localTransportationCostRestrictions,
            localRentGradientRestrictions,
            globalRentCostRestriction);
    assertEquals(expectedOutput, result, 1e-6, "The rent cost should match the calculated value.");
  }

  @Test
  @DisplayName(
      "Test that the rentCostCalculator method does overrule the imposed global restriction with the local one")
  public void testRentCostCalculatorWithGlobalAndLocalRentGradientRestrictions() {
    localRentGradientRestrictions.put(defaultCoords, 8.0);
    final double globalRentCostRestriction = 7.9;
    final double expectedOutput = 8.0;
    final double result =
        rentCostCalculator(
            defaultDistance,
            defaultWage,
            nullCommuterCost,
            defaultCoords,
            localTransportationCostRestrictions,
            localRentGradientRestrictions,
            globalRentCostRestriction);
    assertEquals(expectedOutput, result, "The rent cost should match the local restriction.");
  }

  @ParameterizedTest(
      name = "given wage: {0} and commuter cost: {1}, the maximum distance from the CBD is: {2}")
  @MethodSource("cityEdgeArguments")
  public void testCityEdge(Double wage, Double commuterCost, int expectedOutput) {
    int result = cityEdge(wage, commuterCost);
    assertEquals(expectedOutput, result, "The calculated height did not match the expected value.");
  }

  @ParameterizedTest(
      name = "given distance: {0}, wage: {1} and commuter cost: {2}, the density gradient is: {3}")
  @MethodSource("densityGradientArguments")
  public void testDensityGradient(
      int distance, Double wage, Double commuterCost, double expectedOutput) {
    double result = densityGradient(distance, wage, commuterCost);
    assertEquals(
        expectedOutput, result, 1e-6, "The calculated height did not match the expected value.");
  }

  @ParameterizedTest(
      name =
          "given coordinates: {0}, height: {1} and rent: {2}, the ResidentialBlock created is: {3}")
  @MethodSource("createResidentialBlockArguments")
  public void testCreateResidentialBlock(
      Coordinates coordinates, int height, double rent, ResidentialBlock expectedOutput) {
    // Setup

    heightOnDistance.put(coordinates, height);
    rentOnDistance.put(coordinates, rent);
    densityPerBlock.put(coordinates, expectedOutput.getBuilding().getPeople());
    landPricePerBlock.put(coordinates, expectedOutput.getLandPrice().getValue());

    // Act
    ResidentialBlock block =
        createResidentialBlock(
            coordinates, heightOnDistance, rentOnDistance, densityPerBlock, landPricePerBlock);

    // Assert
    assertNotNull(block);
    assertEquals(expectedOutput.getBlockType(), block.getBlockType());
    assertEquals(expectedOutput.getRentCost(), block.getRentCost());
    assertEquals(expectedOutput.getLandPrice(), block.getLandPrice());
    Building resultBuilding = block.getBuilding();
    Building expectedBuilding = expectedOutput.getBuilding();
    assertNotNull(resultBuilding);
    assertEquals(expectedBuilding.getHeight(), resultBuilding.getHeight());
    assertEquals(expectedBuilding.getBuildingType(), resultBuilding.getBuildingType());
    assertEquals(expectedBuilding.getPeople(), resultBuilding.getPeople());
  }

  @ParameterizedTest
  @DisplayName(
      "generateNonResBLock should create a NonResidentialBlock of type {1} in coordinates {0}")
  @MethodSource("generateNonResidentialBlocksArguments")
  public void testGenerateNonResBLock(Coordinates coordinates, BlockType expectedBlockType) {

    NonResidentialBlock result = generateNonResBLock(defaultCityEdge, coordinates);
    assertNotNull(result);
    assertEquals(expectedBlockType, result.getBlockType());
  }

  @Test
  @DisplayName(
      "tests that the function refactorEfficiencyHeight returns the correct value given the parameters")
  public void testRefactorEfficiencyHeight() {
    double constructionCost = 100000.0;
    int expectedResult = 97;

    int result = refactorEfficiencyHeight(constructionCost);
    assertEquals(expectedResult, result, "The calculated height did not match the expected value.");
  }

  @Nested
  class testNewCityGrid {
    static final int popSize = 542672;
    static int totalBlocks;

    @BeforeEach
    void setUp() {
      totalBlocks = (int) Math.pow(2 * defaultCityEdge + 1, 2);
      for (int i = -defaultCityEdge; i < defaultCityEdge + 1; i++) {
        for (int j = -defaultCityEdge; j < defaultCityEdge + 1; j++) {
          int distance = Math.abs(i) + Math.abs(j);
          Coordinates coordinates = new Coordinates(i, j);
          Integer height =
              efficiencyHeight(
                  distance,
                  defaultWage,
                  defaultCommuterCost,
                  coordinates,
                  emptyMap(),
                  emptyMap(),
                  0.0);
          Double rent =
              rentCostCalculator(
                  distance,
                  defaultWage,
                  defaultCommuterCost,
                  coordinates,
                  emptyMap(),
                  emptyMap(),
                  0.0);

          Double landPrice =
              landPrice(distance, defaultWage, defaultCommuterCost, coordinates, emptyMap());

          heightOnDistance.put(coordinates, height);
          rentOnDistance.put(coordinates, rent);
          landPricePerBlock.put(coordinates, landPrice);

          Long popPerBlock =
              (long)
                  Math.ceil(
                      (densityGradient(distance, defaultWage, defaultCommuterCost)
                          * CONVERSION_COEFFICIENT));
          densityPerBlock.put(coordinates, popPerBlock);
        }
      }
    }

    @Test
    @DisplayName("Test the implementation for newCityGrid without restrictions")
    public void testNewCityGridWithNoRestrictions() {
      // act
      Map<Coordinates, CityBlock> result =
          newCityGrid(
              defaultCityEdge,
              heightOnDistance,
              rentOnDistance,
              emptyMap(),
              densityPerBlock,
              landPricePerBlock);

      // Assert
      assertNotNull(result);
      assertEquals(totalBlocks, result.size());
      for (int i = -defaultCityEdge; i < defaultCityEdge + 1; i++) {
        for (int j = -defaultCityEdge; j < defaultCityEdge + 1; j++) {
          int distance = Math.abs(i) + Math.abs(j);
          Coordinates coordinates = new Coordinates(i, j);
          CityBlock cityBlock = result.get(coordinates);
          if (distance <= defaultCityEdge) {
            assertEquals(
                cityBlock.getBlockType(),
                BlockType.RESIDENTIALBLOCK,
                "A NonResidentialBlock has been created inside the residential radius even though there are no "
                    + "restrictions");
          } else {
            assertTrue(
                cityBlock.getBlockType() == BlockType.PARKBLOCK
                    || cityBlock.getBlockType() == BlockType.WATERBLOCK,
                "A ResidentialBlock has been created  inside the residential radius even though there are no "
                    + "restrictions");
          }
        }
      }
    }

    @Test
    @DisplayName("Test the implementation for newCityGrid with restrictions")
    public void testNewCityGridWithRestrictions() {
      // setup
      Map<Coordinates, BlockType> restrictions = new HashMap<>();
      restrictions.put(new Coordinates(0, 0), BlockType.WATERBLOCK);
      restrictions.put(new Coordinates(1, 1), BlockType.PARKBLOCK);
      restrictions.put(new Coordinates(4, 4), BlockType.WATERBLOCK);
      restrictions.put(new Coordinates(-3, 5), BlockType.PARKBLOCK);
      restrictions.put(new Coordinates(-2, 2), BlockType.WATERBLOCK);
      restrictions.put(new Coordinates(8, 8), BlockType.WATERBLOCK);
      restrictions.put(new Coordinates(5, 5), BlockType.PARKBLOCK);
      restrictions.put(new Coordinates(-7, 4), BlockType.WATERBLOCK);
      restrictions.put(new Coordinates(-3, 7), BlockType.PARKBLOCK);
      restrictions.put(new Coordinates(-6, 3), BlockType.WATERBLOCK);
      // act
      Map<Coordinates, CityBlock> result =
          newCityGrid(
              defaultCityEdge,
              heightOnDistance,
              rentOnDistance,
              restrictions,
              densityPerBlock,
              landPricePerBlock);

      // Assert
      assertNotNull(result);
      assertEquals(totalBlocks, result.size());
      for (int i = -defaultCityEdge; i < defaultCityEdge + 1; i++) {
        for (int j = -defaultCityEdge; j < defaultCityEdge + 1; j++) {
          int distance = Math.abs(i) + Math.abs(j);
          Coordinates coordinates = new Coordinates(i, j);
          CityBlock cityBlock = result.get(coordinates);
          if (restrictions.containsKey(coordinates)) {
            assertEquals(
                restrictions.get(coordinates),
                cityBlock.getBlockType(),
                "The creation of the grid does not " + "respect the blockType restrictions");
          } else {
            if (distance <= defaultCityEdge) {
              assertEquals(
                  cityBlock.getBlockType(),
                  BlockType.RESIDENTIALBLOCK,
                  "A NonResidentialBlock has been created inside the residential radius even though there are no "
                      + "restrictions");
            } else if (Math.abs(i) == defaultCityEdge && Math.abs(j) == defaultCityEdge) {
              assertEquals(
                  BlockType.WATERBLOCK,
                  cityBlock.getBlockType(),
                  "A block that isn't a WATERBLOCK has been created in the corners even though there are no "
                      + "restrictions");
            } else {
              assertEquals(
                  BlockType.PARKBLOCK,
                  cityBlock.getBlockType(),
                  "A block that isn't a PARKBLOCK has been created outside the residential radius even though there are no "
                      + "restrictions");
            }
          }
        }
      }
    }

    @Test
    @DisplayName("Tests the createNewCity method with no restrictions")
    public void testCreateNewCityWithNoRestrictions() {
      final int expectedRadius = 14;
      final int expectedTotBlocks = 841;
      CityCreationParameters params =
          new CityCreationParameters(
              defaultWage,
              defaultCommuterCost,
              nullMoney.getValue(),
              nullMoney.getValue(),
              emptyMap(),
              emptyMap(),
              emptyMap(),
              emptyMap(),
              Currency.CHF);
      Map<Coordinates, CityBlock> expectedCityGrid =
          storeRestrictions(
              defaultCityEdge,
              newCityGrid(
                  defaultCityEdge,
                  heightOnDistance,
                  rentOnDistance,
                  emptyMap(),
                  densityPerBlock,
                  landPricePerBlock),
              params);

      City result = createNewCity(params);

      Map<Coordinates, CityBlock> outputCityGrid = result.getBlocks();
      assertEquals(popSize, result.getPopulation());
      assertEquals(expectedRadius, result.getRadius());
      assertEquals(expectedTotBlocks, result.getBlockCounter());
      for (int i = -defaultCityEdge; i < defaultCityEdge + 1; i++) {
        for (int j = -defaultCityEdge; j < defaultCityEdge + 1; j++) {
          Coordinates coordinates = new Coordinates(i, j);
          CityBlock output = outputCityGrid.get(coordinates);
          CityBlock expected = expectedCityGrid.get(coordinates);
          assertEquals(expected, output, "these blocks in the same coordinates are not equal");
        }
      }
    }
  }

  @Nested
  class populationTests {
    private Map<Coordinates, Long> expectedDensity;
    private Map<Coordinates, Long> expectedDensityWithRestrictions;

    @BeforeEach
    public void setUp() {
      expectedDensity = new HashMap<>();
      expectedDensityWithRestrictions = new HashMap<>();
      List<Double> densityList =
          densityGradientArguments().limit(15).map(arg -> (Double) arg.get()[3]).toList();
      List<Double> densityListHalf = new ArrayList<>();
      densityListHalf.add(0.012900);
      densityListHalf.add(0.011103);
      densityListHalf.add(0.009556);
      densityListHalf.add(0.008225);
      densityListHalf.add(0.007080);
      densityListHalf.add(0.006094);
      densityListHalf.add(0.005245);
      densityListHalf.add(0.004514);
      densityListHalf.add(0.003886);
      densityListHalf.add(0.003344);
      densityListHalf.add(0.002879);
      densityListHalf.add(0.002478);
      densityListHalf.add(0.002133);
      densityListHalf.add(0.001836);
      densityListHalf.add(0.001580);
      for (int i = -defaultCityEdge; i <= defaultCityEdge; i++) {
        for (int j = -defaultCityEdge; j <= defaultCityEdge; j++) {
          Coordinates coordinates = new Coordinates(i, j);
          int distance = Math.abs(i) + Math.abs(j);
          if (distance <= defaultCityEdge) {
            expectedDensity.put(
                coordinates, (long) Math.abs(densityList.get(distance) * CONVERSION_COEFFICIENT));
            expectedDensityWithRestrictions.put(
                coordinates,
                (long) Math.abs(densityListHalf.get(distance) * CONVERSION_COEFFICIENT));
          }
        }
      }
    }

    @Test
    @DisplayName(
        "Test the calculation of the total number of residents in the city without restrictions")
    public void testPopulationSizeWithNoRestrictions() {
      // setup
      final long expectedResult = 542672;
      Map<Coordinates, Long> resultDensity = new HashMap<>();
      // act
      long result =
          populationSize(
              defaultCityEdge,
              defaultWage,
              defaultCommuterCost,
              emptyMap(),
              emptyMap(),
              resultDensity);

      // assert
      assertEquals(expectedResult, result);
      for (int i = -defaultCityEdge; i <= defaultCityEdge; i++) {
        for (int j = -defaultCityEdge; j <= defaultCityEdge; j++) {
          Coordinates coordinates = new Coordinates(i, j);
          Long localResultDensity = resultDensity.get(coordinates);
          Long localExpectedDensity = expectedDensity.get(coordinates);
          assertEquals(localExpectedDensity, localResultDensity, "local populations do not match");
        }
      }
    }

    @Test
    @DisplayName(
        "Test the calculation of the total number of residents in a maximal city without restrictions")
    public void testMaxPopulationSizeWithNoRestrictions() {
      // setup
      final long expectedResult = Long.MAX_VALUE;
      final int cityEdge = 23;
      Map<Coordinates, Long> resultDensity = new HashMap<>();
      // act
      long result =
          populationSize(cityEdge, 100000.0, 3000.0, emptyMap(), emptyMap(), resultDensity);

      // assert
      assertEquals(expectedResult, result);
      for (int i = -cityEdge; i <= cityEdge; i++) {
        for (int j = -cityEdge; j <= cityEdge; j++) {
          Coordinates coordinates = new Coordinates(i, j);
          int distance = Math.abs(i) + Math.abs(j);
          if (distance <= cityEdge) {
            Long localResultDensity = resultDensity.get(coordinates);
            Long localExpectedDensity = Long.MAX_VALUE;
            assertEquals(
                localExpectedDensity, localResultDensity, "local populations do not match");
          }
        }
      }
    }

    @Test
    @DisplayName(
        "Test the calculation of the total number of residents in the city with local transportation cost "
            + "restrictions")
    public void testPopulationSizeWithRestrictionsV1() {
      // setup
      final long expectedResult = 1475156;
      Map<Coordinates, Long> resultDensity = new HashMap<>();

      for (int i = -defaultCityEdge; i <= defaultCityEdge; i++) {
        for (int j = -defaultCityEdge; j <= defaultCityEdge; j++) {
          Coordinates coordinates = new Coordinates(i, j);
          localTransportationCostRestrictions.put(coordinates, defaultCommuterCost / 2);
        }
      }
      // act
      long result =
          populationSize(
              defaultCityEdge,
              defaultWage,
              nullCommuterCost,
              localTransportationCostRestrictions,
              emptyMap(),
              resultDensity);

      // assert
      assertEquals(expectedResult, result);
      for (int i = -defaultCityEdge; i <= defaultCityEdge; i++) {
        for (int j = -defaultCityEdge; j <= defaultCityEdge; j++) {
          Coordinates coordinates = new Coordinates(i, j);
          Long localResultDensity = resultDensity.get(coordinates);
          Long localExpectedDensity = expectedDensityWithRestrictions.get(coordinates);
          assertEquals(localExpectedDensity, localResultDensity, "local populations do not match");
        }
      }
    }

    @Test
    @DisplayName(
        "Test the calculation of the total number of residents in the city with type restrictions")
    public void testPopulationSizeWithRestrictionsV2() {
      // setup
      final long expectedResult = 0;
      Map<Coordinates, Long> resultDensity = new HashMap<>();
      Map<Coordinates, BlockType> typeRestrictions = new HashMap<>();
      for (int i = -defaultCityEdge; i <= defaultCityEdge; i++) {
        for (int j = -defaultCityEdge; j <= defaultCityEdge; j++) {
          typeRestrictions.put(new Coordinates(i, j), BlockType.PARKBLOCK);
          // should be overridden by type restrictions
          localTransportationCostRestrictions.put(new Coordinates(i, j), defaultCommuterCost);
        }
      }
      // act
      long result =
          populationSize(
              defaultCityEdge,
              defaultWage,
              nullCommuterCost,
              localTransportationCostRestrictions,
              typeRestrictions,
              resultDensity);

      // assert
      assertEquals(expectedResult, result);
      assertTrue(resultDensity.isEmpty(), "Map should have no values");
    }

    @Test
    @DisplayName("Tests the createNewCity method with local restrictions")
    public void testCreateNewCityWithRestrictionsV1() {
      final int expectedRadius = 14;
      final int expectedTotBlocks = 841;
      final int expectedPopSize = 1426268;
      for (int i = -defaultCityEdge; i <= defaultCityEdge; i++) {
        for (int j = -defaultCityEdge; j <= defaultCityEdge; j++) {
          localTransportationCostRestrictions.put(new Coordinates(i, j), defaultCommuterCost / 2);
          localConstructionCostRestriction.put(new Coordinates(i, j), 2000.0);
          localRentGradientRestrictions.put(new Coordinates(i, j), 8.0);
          Coordinates coordinates = new Coordinates(i, j);
          int distance = Math.abs(i) + Math.abs(j);
          Integer height =
              efficiencyHeight(
                  distance,
                  defaultWage,
                  defaultCommuterCost,
                  coordinates,
                  localTransportationCostRestrictions,
                  localConstructionCostRestriction,
                  0.0);
          Double rent =
              rentCostCalculator(
                  distance,
                  defaultWage,
                  defaultCommuterCost,
                  coordinates,
                  localTransportationCostRestrictions,
                  localRentGradientRestrictions,
                  0.0);
          Double landPrice =
              landPrice(
                  distance,
                  defaultWage,
                  defaultCommuterCost,
                  coordinates,
                  localTransportationCostRestrictions);
          heightOnDistance.put(coordinates, height);
          rentOnDistance.put(coordinates, rent);
          landPricePerBlock.put(coordinates, landPrice);
        }
      }
      Map<Coordinates, BlockType> restrictions = new HashMap<>();
      restrictions.put(new Coordinates(0, 0), BlockType.WATERBLOCK);
      restrictions.put(new Coordinates(1, 1), BlockType.PARKBLOCK);
      restrictions.put(new Coordinates(4, 4), BlockType.WATERBLOCK);
      restrictions.put(new Coordinates(-3, 5), BlockType.PARKBLOCK);
      restrictions.put(new Coordinates(-2, 2), BlockType.WATERBLOCK);
      restrictions.put(new Coordinates(8, 8), BlockType.WATERBLOCK);
      restrictions.put(new Coordinates(5, 5), BlockType.PARKBLOCK);
      restrictions.put(new Coordinates(-7, 4), BlockType.WATERBLOCK);
      restrictions.put(new Coordinates(-3, 7), BlockType.PARKBLOCK);
      restrictions.put(new Coordinates(-6, 3), BlockType.WATERBLOCK);

      CityCreationParameters params =
          new CityCreationParameters(
              defaultWage,
              defaultCommuterCost,
              nullMoney.getValue(),
              nullMoney.getValue(),
              localTransportationCostRestrictions,
              localConstructionCostRestriction,
              localRentGradientRestrictions,
              restrictions,
              Currency.CHF);

      Map<Coordinates, CityBlock> expectedCityGrid =
          storeRestrictions(
              defaultCityEdge,
              newCityGrid(
                  defaultCityEdge,
                  heightOnDistance,
                  rentOnDistance,
                  restrictions,
                  expectedDensityWithRestrictions,
                  landPricePerBlock),
              params);

      City result = createNewCity(params);
      Map<Coordinates, CityBlock> outputCityGrid = result.getBlocks();
      assertEquals(expectedPopSize, result.getPopulation());
      assertEquals(expectedRadius, result.getRadius());
      assertEquals(expectedTotBlocks, result.getBlockCounter());
      for (int i = -defaultCityEdge; i < defaultCityEdge + 1; i++) {
        for (int j = -defaultCityEdge; j < defaultCityEdge + 1; j++) {
          Coordinates coordinates = new Coordinates(i, j);
          CityBlock output = outputCityGrid.get(coordinates);
          CityBlock expected = expectedCityGrid.get(coordinates);
          if (!expected.equals(output)) {
            int x = 9;
          }
          assertEquals(expected, output, "these blocks in the same coordinates are not equal");
        }
      }
    }

    @Test
    @DisplayName("Tests the createNewCity method with global restrictions")
    public void testCreateNewCityWithRestrictionsV2() {
      final int expectedRadius = 14;
      final int expectedTotBlocks = 841;
      final int expectedPopSize = 542672;
      final double globalRentCostRestriction = 8.0;
      final double globalConstructionCostRestriction = 2000.0;
      for (int i = -defaultCityEdge; i <= defaultCityEdge; i++) {
        for (int j = -defaultCityEdge; j <= defaultCityEdge; j++) {
          Coordinates coordinates = new Coordinates(i, j);
          int distance = Math.abs(i) + Math.abs(j);
          Integer height =
              efficiencyHeight(
                  distance,
                  defaultWage,
                  defaultCommuterCost,
                  coordinates,
                  emptyMap(),
                  emptyMap(),
                  globalConstructionCostRestriction);

          Double rent =
              rentCostCalculator(
                  distance,
                  defaultWage,
                  defaultCommuterCost,
                  coordinates,
                  emptyMap(),
                  emptyMap(),
                  globalRentCostRestriction);

          Double landPrice =
              landPrice(
                  distance,
                  defaultWage,
                  defaultCommuterCost,
                  coordinates,
                  localTransportationCostRestrictions);

          heightOnDistance.put(coordinates, height);
          rentOnDistance.put(coordinates, rent);
          landPricePerBlock.put(coordinates, landPrice);
        }
      }
      CityCreationParameters params =
          new CityCreationParameters(
              defaultWage,
              defaultCommuterCost,
              globalConstructionCostRestriction,
              globalRentCostRestriction,
              emptyMap(),
              emptyMap(),
              emptyMap(),
              emptyMap(),
              Currency.CHF);

      Map<Coordinates, CityBlock> expectedCityGrid =
          storeRestrictions(
              defaultCityEdge,
              newCityGrid(
                  defaultCityEdge,
                  heightOnDistance,
                  rentOnDistance,
                  emptyMap(),
                  expectedDensity,
                  landPricePerBlock),
              params);

      City result = createNewCity(params);
      Map<Coordinates, CityBlock> outputCityGrid = result.getBlocks();
      assertEquals(expectedPopSize, result.getPopulation());
      assertEquals(expectedRadius, result.getRadius());
      assertEquals(expectedTotBlocks, result.getBlockCounter());
      for (int i = -defaultCityEdge; i < defaultCityEdge + 1; i++) {
        for (int j = -defaultCityEdge; j < defaultCityEdge + 1; j++) {
          Coordinates coordinates = new Coordinates(i, j);
          CityBlock output = outputCityGrid.get(coordinates);
          CityBlock expected = expectedCityGrid.get(coordinates);
          if (!expected.equals(output)) {
            int x = 9;
          }
          assertEquals(expected, output, "these blocks in the same coordinates are not equal");
        }
      }
    }
  }
}
