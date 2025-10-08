package ch.usi.inf.bsc.sa4.lab02spring.utils;

import static ch.usi.inf.bsc.sa4.lab02spring.model.BlockType.*;

import ch.usi.inf.bsc.sa4.lab02spring.model.BlockType;
import ch.usi.inf.bsc.sa4.lab02spring.model.Building;
import ch.usi.inf.bsc.sa4.lab02spring.model.BuildingType;
import ch.usi.inf.bsc.sa4.lab02spring.model.City;
import ch.usi.inf.bsc.sa4.lab02spring.model.CityBlock;
import ch.usi.inf.bsc.sa4.lab02spring.model.Coordinates;
import ch.usi.inf.bsc.sa4.lab02spring.model.Currency;
import ch.usi.inf.bsc.sa4.lab02spring.model.Money;
import ch.usi.inf.bsc.sa4.lab02spring.model.NonResidentialBlock;
import ch.usi.inf.bsc.sa4.lab02spring.model.ResidentialBlock;
import java.util.HashMap;
import java.util.Map;

/** Class used to create a new City object based on constants and user defined parameters */
public final class CityGenerator {

  /** housing UTILITY weight is related to housing consumption */
  private static final Double HOUSING_UTILITY_WEIGHT = 2000.0;

  /**
   * UTILITY stands for the value that an agent derives from housing and the consumption of another
   * good
   */
  private static final Double UTILITY = 12500.0;

  /** CONSTRUCTION_COST_EXPONENT is used to derive the cost based on the building height */
  private static final Double CONSTRUCTION_COST_EXPONENT = 1.5;

  /** BASE_CONSTRUCTION_COST represents the coefficient in the cost function for building height */
  private static final Double BASE_CONSTRUCTION_COST = 20.0;

  /** LAND_PRICE_GRADIENT represents the cost of housing at the end of the city */
  private static final Double LAND_PRICE_GRADIENT = 0.1;

  /** EPSILON is used for comparison between doubles */
  private static final Double EPSILON = 1e-10;

  /** Default currency used to represent monetary values */
  private static Currency currency = Currency.CHF;

  /** Private constructor to hide the implicit public one */
  private CityGenerator() {}

  /**
   * Creates a new City Object
   *
   * @param parameters the instance of the CityCreationParameters containing all necessary
   *     parameters
   * @return the City instance
   * @spec.requires the wageParam and the commuterCostParam cannot be 0.0
   */
  public static City createNewCity(CityCreationParameters parameters) {
    final int POWER_COEFFICIENT = 2;
    final int MAX_RADIUS = 20;

    Double wageParam = parameters.wageParam();
    Double commuterCostParam = parameters.commuterCostParam();
    Map<Coordinates, Double> localTransportationCostRestrictions =
        parameters.localTransportationCostRestrictions();
    Double globalConstructionCostRestriction = parameters.globalConstructionCostRestriction();
    Map<Coordinates, Double> localConstructionCostRestriction =
        parameters.localConstructionCostRestriction();
    Double globalRentCostRestriction = parameters.globalRentCostRestriction();
    Map<Coordinates, Double> localRentCostRestrictions = parameters.localRentCostRestrictions();
    Map<Coordinates, BlockType> blockTypeRestrictions = parameters.blockTypeRestrictions();
    currency = parameters.currency();
    int cityDistance = cityEdge(wageParam, commuterCostParam);
    if (cityDistance > MAX_RADIUS) {
      cityDistance = MAX_RADIUS;
    }

    Map<Coordinates, Integer> heightOnDistance = new HashMap<>();
    Map<Coordinates, Double> rentOnDistance = new HashMap<>();
    Map<Coordinates, Double> landPricePerBlock = new HashMap<>();

    final int lowerBound = -cityDistance;
    final int upperBound = cityDistance;
    for (int yCoordinate = upperBound; yCoordinate >= lowerBound; yCoordinate--) {
      for (int xCoordinate = lowerBound; xCoordinate <= upperBound; xCoordinate++) {

        Coordinates coordinates = new Coordinates(xCoordinate, yCoordinate);
        int distance = Math.abs(yCoordinate) + Math.abs(xCoordinate);

        Integer height =
            efficiencyHeight(
                distance,
                wageParam,
                commuterCostParam,
                coordinates,
                localTransportationCostRestrictions,
                localConstructionCostRestriction,
                globalConstructionCostRestriction);

        heightOnDistance.put(coordinates, height);

        Double rentCost =
            rentCostCalculator(
                distance,
                wageParam,
                commuterCostParam,
                coordinates,
                localTransportationCostRestrictions,
                localRentCostRestrictions,
                globalRentCostRestriction);

        rentOnDistance.put(coordinates, rentCost);

        Double landCost =
            landPrice(
                distance,
                wageParam,
                commuterCostParam,
                coordinates,
                localTransportationCostRestrictions);

        landPricePerBlock.put(coordinates, landCost);
      }
    }

    Map<Coordinates, Long> densityPerBlock = new HashMap<>();
    long popSize =
        populationSize(
            cityDistance,
            wageParam,
            commuterCostParam,
            localTransportationCostRestrictions,
            blockTypeRestrictions,
            densityPerBlock);

    Map<Coordinates, CityBlock> cityGrid =
        newCityGrid(
            cityDistance,
            heightOnDistance,
            rentOnDistance,
            blockTypeRestrictions,
            densityPerBlock,
            landPricePerBlock);
    Map<Coordinates, CityBlock> fullCityGrid =
        storeRestrictions(cityDistance, cityGrid, parameters);
    final int citySize = 2 * cityDistance + 1;
    int blockCounter = (int) (Math.pow(citySize, POWER_COEFFICIENT));
    return new City(
        cityDistance,
        null,
        popSize,
        blockCounter,
        fullCityGrid,
        new Money(globalRentCostRestriction, currency),
        new Money(globalConstructionCostRestriction, currency));
  }

  /**
   * Calculates the cost of housing for a determined building
   *
   * @param distance from the CBD
   * @param wage the salary of the residents
   * @param commuterCost the cost to commute based on Currency/km/month
   * @return the cost of housing
   * @spec.requires the wage and the commuterCost cannot be 0.0
   */
  public static Double housingRent(Integer distance, Double wage, Double commuterCost) {
    final double exponent =
        (wage - UTILITY - HOUSING_UTILITY_WEIGHT - commuterCost * distance)
            / HOUSING_UTILITY_WEIGHT;
    return HOUSING_UTILITY_WEIGHT * Math.exp(exponent);
  }

  /**
   * Calculates the rent cost of a block based on its distance form the CDB
   *
   * @param distance the distance from the CBD
   * @param wage the salary of the residents
   * @param commuterCost the cost to commute based on Currency/km/month
   * @param coordinates the coordinates of the block
   * @param localTransportationCostRestrictions the restrictions on the transportation cost
   * @param localRentCostRestrictions local limits for rent
   * @param globalRentCostRestriction global limit for rent
   * @return the price of the land
   * @spec.requires the wage and the commuterCost cannot be 0.0
   */
  public static Double rentCostCalculator(
      int distance,
      Double wage,
      Double commuterCost,
      Coordinates coordinates,
      Map<Coordinates, Double> localTransportationCostRestrictions,
      Map<Coordinates, Double> localRentCostRestrictions,
      Double globalRentCostRestriction) {
    double commCost = commuterCost;
    if (localTransportationCostRestrictions.containsKey(coordinates)) {
      commCost = localTransportationCostRestrictions.get(coordinates);
    }
    double rentCost = housingRent(distance, wage, commCost);

    if (localRentCostRestrictions.containsKey(coordinates)
        && (rentCost - localRentCostRestrictions.get(coordinates)) > EPSILON) {
      rentCost = localRentCostRestrictions.get(coordinates);
    } else {
      if (globalRentCostRestriction > EPSILON && rentCost > globalRentCostRestriction) {
        rentCost = globalRentCostRestriction;
      }
    }
    return rentCost;
  }

  /**
   * Calculates the height for a specific building
   *
   * @param distance the distance from the CBD
   * @param wage the salary of the residents
   * @param commuterCost the cost to commute based on Currency/km/month
   * @param coordinates the coordinates of the block
   * @param localTransportationCostRestrictions the restrictions on the transportation cost
   * @param localConstructionCostRestriction local limits for construction const
   * @param globalConstructionCostRestriction global upper limit for construction costs
   * @return the number of floors
   * @spec.requires the wage and the commuterCost cannot be 0.0
   */
  public static Integer efficiencyHeight(
      int distance,
      Double wage,
      Double commuterCost,
      Coordinates coordinates,
      Map<Coordinates, Double> localTransportationCostRestrictions,
      Map<Coordinates, Double> localConstructionCostRestriction,
      Double globalConstructionCostRestriction) {
    final int SCALE_COEFFICIENT = 1_000;
    final int FLOOR_HEIGHT = 3;
    final int MAX_HEIGHT = 150;
    Double commCost = commuterCost;
    if (localTransportationCostRestrictions.containsKey(coordinates)) {
      commCost = localTransportationCostRestrictions.get(coordinates);
    }
    final double exponent = 1 / (CONSTRUCTION_COST_EXPONENT - 1);
    Integer currentHeight =
        (int)
            Math.ceil(
                Math.pow(
                        (housingRent(distance, wage, commCost)
                            / (CONSTRUCTION_COST_EXPONENT * BASE_CONSTRUCTION_COST)),
                        exponent)
                    * SCALE_COEFFICIENT
                    / FLOOR_HEIGHT);
    Double currentConstructionCost = constructionCost(currentHeight);
    if (localConstructionCostRestriction.containsKey(coordinates)) {
      if (currentConstructionCost > localConstructionCostRestriction.get(coordinates)) {
        currentHeight =
            refactorEfficiencyHeight(localConstructionCostRestriction.get(coordinates));
      }
    } else {
      if (globalConstructionCostRestriction > EPSILON
          && currentConstructionCost > globalConstructionCostRestriction) {
        currentHeight = refactorEfficiencyHeight(globalConstructionCostRestriction);
      }
    }
    if (currentHeight < MAX_HEIGHT) {
      return currentHeight;
    } else {
      return MAX_HEIGHT;
    }
  }

  /**
   * Refactors the height of the building based on a construction cost limitation
   *
   * @param constructionCostRestriction the construction const restriction
   * @return the refactored height
   */
  public static Integer refactorEfficiencyHeight(Double constructionCostRestriction) {
    final int FLOOR_HEIGHT = 3;
    return (int)
        Math.floor(
            Math.pow(
                constructionCostRestriction / BASE_CONSTRUCTION_COST,
                1 / CONSTRUCTION_COST_EXPONENT)) / FLOOR_HEIGHT;
  }

  /**
   * Computes the construction cost of a building based on its height
   *
   * @param height the height of the building
   * @return the building construction const
   */
  public static Double constructionCost(Integer height) {
    final double FLOOR_TO_M = 3;
    return BASE_CONSTRUCTION_COST * Math.pow(height * FLOOR_TO_M, CONSTRUCTION_COST_EXPONENT);
  }

  /**
   * Calculates the price of the land of a block based on its distance form the CDB
   *
   * @param distance the distance from the CBD
   * @param wage the salary of the residents
   * @param commuterCost the cost to commute based on Currency/km/month
   * @param coordinates the coordinates of the block
   * @param localTransportationCostRestrictions the restrictions on the transportation cost
   * @return the price of the land
   * @spec.requires the wage and the commuterCost cannot be 0.0
   */
  public static Double landPrice(
      int distance,
      Double wage,
      Double commuterCost,
      Coordinates coordinates,
      Map<Coordinates, Double> localTransportationCostRestrictions) {
    Double commCost = commuterCost;
    if (localTransportationCostRestrictions.containsKey(coordinates)) {
      commCost = localTransportationCostRestrictions.get(coordinates);
    }
    final double price = housingRent(distance, wage, commCost);
    final double exponent = 1 / (1 - CONSTRUCTION_COST_EXPONENT);
    return (CONSTRUCTION_COST_EXPONENT - 1)
        * Math.pow(CONSTRUCTION_COST_EXPONENT, CONSTRUCTION_COST_EXPONENT * exponent)
        * Math.pow(BASE_CONSTRUCTION_COST, exponent)
        * Math.pow(price, CONSTRUCTION_COST_EXPONENT / (CONSTRUCTION_COST_EXPONENT - 1));
  }

  /**
   * Calculates the maximum distance from the CDB that is inhabited (in coordinates, CBD is distance
   * 0)
   *
   * @param wage the salary of the residents
   * @param commuterCost the cost to commute based on Currency/km/month
   * @return the city edge
   * @spec.requires the wage and the commuterCost cannot be 0.0
   */
  public static Integer cityEdge(Double wage, Double commuterCost) {
    final int CBD = 1;
    final double coefficient = 1 / (1 - CONSTRUCTION_COST_EXPONENT);
    final double cCostCoefficient = (CONSTRUCTION_COST_EXPONENT - 1);
    final double logDenominator =
        cCostCoefficient
            * Math.pow(CONSTRUCTION_COST_EXPONENT, CONSTRUCTION_COST_EXPONENT * coefficient)
            * Math.pow(BASE_CONSTRUCTION_COST, coefficient)
            * Math.pow(HOUSING_UTILITY_WEIGHT, CONSTRUCTION_COST_EXPONENT / cCostCoefficient);
    final double numerator = wage - UTILITY - HOUSING_UTILITY_WEIGHT;
    final double logTerm = Math.log(LAND_PRICE_GRADIENT / logDenominator);
    double dBar =
        (numerator
                - ((HOUSING_UTILITY_WEIGHT * cCostCoefficient)
                    / CONSTRUCTION_COST_EXPONENT
                    * logTerm))
            / commuterCost;
    return ((int) Math.ceil(dBar)) - CBD;
  }

  /**
   * Calculates the density gradient of a block based on its distance from the CDB
   *
   * @param distance the distance from the CBD
   * @param wage the salary of the residents
   * @param commuterCost the cost to commute based on Currency/km/month
   * @return the density gradient
   * @spec.requires the wage and the commuterCost cannot be 0.0
   */
  public static Double densityGradient(int distance, Double wage, Double commuterCost) {
    final double exponent = 1 / (1 - CONSTRUCTION_COST_EXPONENT);
    final double coefficient =
        Math.pow(CONSTRUCTION_COST_EXPONENT, exponent)
            * Math.pow(BASE_CONSTRUCTION_COST, exponent)
            * Math.pow(HOUSING_UTILITY_WEIGHT, 1 / (CONSTRUCTION_COST_EXPONENT - 1));
    return Math.abs(
        coefficient
            * Math.exp(
                (CONSTRUCTION_COST_EXPONENT
                        * (wage - UTILITY - HOUSING_UTILITY_WEIGHT - commuterCost * distance))
                    / (HOUSING_UTILITY_WEIGHT * (CONSTRUCTION_COST_EXPONENT - 1))));
  }

  /**
   * Calculates the total number of people in the city
   *
   * @param cityEdge the maximum distance from the CDB that is inhabited
   * @param wage the salary of the residents
   * @param commuterCost the cost to commute based on Currency/km/month
   * @param localTransportationCostRestrictions the cost to commute in certain blocks
   * @param blockTypeRestrictions zoning restrictions for the city
   * @param densityPerBlock density in each valid block
   * @return the total number of people in the city
   * @spec.modifies densityPerBlock, populates the map
   * @spec.requires the wage and the commuterCost cannot be 0.0
   */
  public static Long populationSize(
      int cityEdge,
      Double wage,
      Double commuterCost,
      Map<Coordinates, Double> localTransportationCostRestrictions,
      Map<Coordinates, BlockType> blockTypeRestrictions,
      Map<Coordinates, Long> densityPerBlock) {
    final int CONVERSION_COEFFICIENT = 1_000_000;
    double population = 0;
    final int lowerBound = -cityEdge;
    final int upperBound = cityEdge;
    for (int y = upperBound; y >= lowerBound; y--) {
      for (int x = lowerBound; x <= upperBound; x++) {
        Coordinates coordinates = new Coordinates(x, y);
        if (isValidBlock(coordinates, cityEdge, blockTypeRestrictions)) {
          double effectiveCommCost =
              localTransportationCostRestrictions.getOrDefault(coordinates, commuterCost);
          int distance = Math.abs(y) + Math.abs(x);
          double localPopulation =
              Math.ceil(
                  CONVERSION_COEFFICIENT * densityGradient(distance, wage, effectiveCommCost));
          if (localPopulation > Long.MAX_VALUE || localPopulation < 0) {
            localPopulation = Long.MAX_VALUE;
          }
          densityPerBlock.put(coordinates, (long) localPopulation);
          population += localPopulation;
        }
      }
    }
    if (population > Long.MAX_VALUE || population < 0) {
      population = Long.MAX_VALUE;
    }
    return (long) population;
  }

  /**
   * The method checks if a given block is within the residential radius and doesn't have
   * restrictions
   *
   * @param coordinates the coordinate of the block
   * @param cityEdge the residential radius
   * @param blockTypeRestrictions zoning restrictions for the city
   * @return flag
   */
  private static boolean isValidBlock(
      Coordinates coordinates, int cityEdge, Map<Coordinates, BlockType> blockTypeRestrictions) {
    int distance = Math.abs(coordinates.y()) + Math.abs(coordinates.x());
    return distance <= cityEdge && !blockTypeRestrictions.containsKey(coordinates);
  }

  /**
   * Creates a new ResidentialBlock Object
   *
   * @param coordinates the distance from the CBD
   * @param heightOnDistance height of the buildings based on their distance from the CBD
   * @param rentOnDistance rent of the buildings based on their distance from the CBD
   * @param densityPerBlock density in each valid block
   * @param landPricePerBlock land price in each valid block
   * @return the ResidentialBlock instance
   */
  public static ResidentialBlock createResidentialBlock(
      Coordinates coordinates,
      Map<Coordinates, Integer> heightOnDistance,
      Map<Coordinates, Double> rentOnDistance,
      Map<Coordinates, Long> densityPerBlock,
      Map<Coordinates, Double> landPricePerBlock) {
    final Money defaultValue = new Money(0.0, currency);
    final Double rent = rentOnDistance.get(coordinates);
    final Double landPrice = landPricePerBlock.get(coordinates);
    final int height = heightOnDistance.get(coordinates);
    final long people = densityPerBlock.getOrDefault(coordinates, 0L);
    BuildingType bType = BuildingType.CONDO;
    if (coordinates.x() == 0 && coordinates.y() == 0) {
      bType = BuildingType.SKYSCRAPER;
    }
    Building currBuilding = new Building(height, bType, people);
    Money currRentCost = new Money(rent, currency);
    Money currLandPrice = new Money(landPrice, currency);
    return new ResidentialBlock(
        currBuilding,
        currRentCost,
        currLandPrice,
        defaultValue,
        defaultValue,
        defaultValue,
        defaultValue,
        defaultValue);
  }

  /**
   * Creates a new NonResidentialBlock Object
   *
   * @param cityEdge the residential radius
   * @param coordinates the coordinate of the block
   * @return the NonResidentialBlock instance
   */
  public static NonResidentialBlock generateNonResBLock(Integer cityEdge, Coordinates coordinates) {
    BlockType type;
    if (Math.abs(coordinates.y()) == cityEdge && Math.abs(coordinates.x()) == cityEdge) {
      type = WATERBLOCK;
    } else {
      type = PARKBLOCK;
    }
    return new NonResidentialBlock(type);
  }

  /**
   * Creates a map of the city
   *
   * @param cityEdge the maximum distance from the CDB that is inhabited
   * @param heightOnDistance height of the buildings based on their distance from the CBD
   * @param rentOnDistance rent of the buildings based on their distance from the CBD
   * @param blockTypeRestrictions the zoning restrictions for the city
   * @return the map of the city
   */
  public static Map<Coordinates, CityBlock> newCityGrid(
      int cityEdge,
      Map<Coordinates, Integer> heightOnDistance,
      Map<Coordinates, Double> rentOnDistance,
      Map<Coordinates, BlockType> blockTypeRestrictions,
      Map<Coordinates, Long> densityPerBlock,
      Map<Coordinates, Double> landPricePerBlock) {

    Map<Coordinates, CityBlock> cityGrid = new HashMap<>();
    final int lowerBound = -cityEdge;
    final int upperBound = cityEdge;

    for (int y = upperBound; y >= lowerBound; y--) {
      for (int x = lowerBound; x <= upperBound; x++) {
        Coordinates coordinates = new Coordinates(x, y);
        CityBlock newBlock =
            determineBlockType(
                coordinates,
                cityEdge,
                blockTypeRestrictions,
                heightOnDistance,
                rentOnDistance,
                densityPerBlock,
                landPricePerBlock);
        cityGrid.put(coordinates, newBlock);
      }
    }
    return cityGrid;
  }

  /**
   * Creates a ResidentialBlock or a NonResidentialBlock based on parameters
   *
   * @param coordinates the coordinates of the block
   * @param cityEdge the residential radius
   * @param blockTypeRestrictions the zoning restrictions for the city
   * @param heightOnDistance height of the buildings based on their distance from the CBD
   * @param rentOnDistance rent of the buildings based on their distance from the CBD
   * @param densityPerBlock density in each valid block
   * @return the instance of the CityBlock
   */
  private static CityBlock determineBlockType(
      Coordinates coordinates,
      int cityEdge,
      Map<Coordinates, BlockType> blockTypeRestrictions,
      Map<Coordinates, Integer> heightOnDistance,
      Map<Coordinates, Double> rentOnDistance,
      Map<Coordinates, Long> densityPerBlock,
      Map<Coordinates, Double> landPricePerBlock) {
    BlockType type = blockTypeRestrictions.get(coordinates);

    if (type != null && type != NONE) {
      return new NonResidentialBlock(type);
    }
    final int x = coordinates.x();
    final int y = coordinates.y();
    int value = Math.abs(y) + Math.abs(x);
    if (value <= cityEdge) {
      return createResidentialBlock(
          coordinates, heightOnDistance, rentOnDistance, densityPerBlock, landPricePerBlock);
    } else {
      return generateNonResBLock(cityEdge, coordinates);
    }
  }

  public static Map<Coordinates, CityBlock> storeRestrictions(
      Integer cityDistance,
      Map<Coordinates, CityBlock> cityGrid,
      CityCreationParameters parameters) {

    for (int i = -cityDistance; i <= cityDistance; i++) {
      for (int j = -cityDistance; j <= cityDistance; j++) {
        Coordinates coordinates = new Coordinates(i, j);
        CityBlock cityBlock = cityGrid.get(coordinates);
        cityBlock.setBlockTypeRestriction(
            parameters.blockTypeRestrictions().getOrDefault(coordinates, NONE));
        if (cityBlock.getBlockType() == RESIDENTIALBLOCK) {
          ResidentialBlock block = (ResidentialBlock) cityBlock;
          ResidentialBlock updatedBlock = processResidentialBlock(block, coordinates, parameters);
          cityGrid.put(coordinates, updatedBlock);
        }
      }
    }
    return cityGrid;
  }

  private static ResidentialBlock processResidentialBlock(
      ResidentialBlock block, Coordinates coordinates, CityCreationParameters parameters) {
    ResidentialBlock setBlock = new ResidentialBlock(block);
    Money localLimitConstruction =
        new Money(
            parameters.localConstructionCostRestriction().getOrDefault(coordinates, 0.0), currency);
    double transportationCostLimit =
        parameters.localTransportationCostRestrictions().getOrDefault(coordinates, 0.0);

    Money localLimitRentCost =
        new Money(parameters.localRentCostRestrictions().getOrDefault(coordinates, 0.0));
    double constructionCost = constructionCost(setBlock.getBuilding().getHeight());

    setBlock.setLocalLimitConstructionCost(localLimitConstruction);
    setBlock.setLocalLimitRentCost(localLimitRentCost);
    setBlock.setTransportationCost(new Money(parameters.commuterCostParam(), currency));
    setBlock.setLocalLimitTransportationCost(new Money(transportationCostLimit));
    setBlock.setConstructionCost(new Money(constructionCost, currency));
    return setBlock;
  }
}
