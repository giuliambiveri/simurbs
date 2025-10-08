package ch.usi.inf.bsc.sa4.lab02spring.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

/** Represents a simulated city. */
@Document(collection = "cities")
public class City {

  private static final int SCALE_RATIO = 2;

  @Id private String id;
  private Long population;
  private Integer radius;
  private Integer blockCounter;
  private Map<Coordinates, CityBlock> cityBlockMap;
  private Money globalMaxRentCost;
  private Money globalMaxConstructionCost;

  /**
   * Constructor of the City class.
   *
   * @param radius city grid radius
   * @param id id of the city in the Db
   * @param population city population number
   * @param blockCounter total number of city blocks
   * @param cityBlockMap map of city blocks to their coordinates
   * @param globalMaxRentCost the limit on rent cost for all the blocks
   * @param globalMaxConstructionCost the limit on construction costs for all the blocks
   */
  @PersistenceCreator
  public City(
      Integer radius,
      String id,
      Long population,
      Integer blockCounter,
      Map<Coordinates, CityBlock> cityBlockMap,
      Money globalMaxRentCost,
      Money globalMaxConstructionCost) {
    this.id = id;
    this.population = population;
    this.radius = radius;
    this.blockCounter = blockCounter;
    this.cityBlockMap = new HashMap<>(cityBlockMap);
    this.globalMaxRentCost = new Money(globalMaxRentCost);
    this.globalMaxConstructionCost = new Money(globalMaxConstructionCost);
  }

  /**
   * Constructor of the City class.
   *
   * @param size city grid size
   * @param population city population number
   * @param blocks total number of city blocks
   * @param cityBlockMap map of city blocks to their coordinates
   * @param globalMaxRentCost the global limit on rent cost for this city
   * @param globalMaxConstructionCost the global limit on construction costs for this city
   */
  public City(
      Integer size,
      Long population,
      Integer blocks,
      Map<Coordinates, CityBlock> cityBlockMap,
      Money globalMaxRentCost,
      Money globalMaxConstructionCost) {
    this(
        size, null, population, blocks, cityBlockMap, globalMaxRentCost, globalMaxConstructionCost);
  }

  /**
   * Constructor of the City class for when limits on rent, and construction costs are not set.
   *
   * @param size city grid size
   * @param id id of the city
   * @param population city population number
   * @param blocks total number of city blocks
   * @param cityBlockMap map of city blocks to their coordinates
   */
  public City(
      Integer size,
      String id,
      Long population,
      Integer blocks,
      Map<Coordinates, CityBlock> cityBlockMap) {
    this(size, id, population, blocks, cityBlockMap, new Money(0.0), new Money(0.0));
  }

  /**
   * Constructor of the City class for when limits on rent, limits on construction costs and ID are
   * not set.
   *
   * @param size city grid size
   * @param population city population number
   * @param blocks total number of city blocks
   * @param cityBlockMap map of city blocks to their coordinates
   */
  public City(
      Integer size, Long population, Integer blocks, Map<Coordinates, CityBlock> cityBlockMap) {
    this(size, null, population, blocks, cityBlockMap, new Money(0.0), new Money(0.0));
  }

  /**
   * Deep copy function to secure information transferred to the frontend
   *
   * @param other the city instance to be copied
   */
  public City(City other) {
    if (other == null) {
      throw new IllegalArgumentException("Cannot copy a null object.");
    }
    this.id = other.id;
    this.population = other.population;
    this.radius = other.radius;
    this.blockCounter = other.blockCounter;
    this.cityBlockMap = new HashMap<>();
    for (Map.Entry<Coordinates, CityBlock> entry : other.cityBlockMap.entrySet()) {
      CityBlock block = entry.getValue();
      if (block instanceof NonResidentialBlock) {
        this.cityBlockMap.put(entry.getKey(), new NonResidentialBlock((NonResidentialBlock) block));
      } else {
        this.cityBlockMap.put(entry.getKey(), new ResidentialBlock((ResidentialBlock) block));
      }
    }
    this.globalMaxRentCost = other.globalMaxRentCost;
    this.globalMaxConstructionCost = other.globalMaxConstructionCost;
  }

  /**
   * Get the ID of the object.
   *
   * @return the ID of the object.
   */
  public String getId() {
    return this.id;
  }

  /**
   * Get the City population.
   *
   * @return the city population.
   */
  public Long getPopulation() {
    return this.population;
  }

  /**
   * Method to get the city grid length.
   *
   * @return city grid length.
   */
  public Integer getSize() {

    return this.radius * SCALE_RATIO + 1;
  }

  /**
   * Method to get the radius.
   *
   * @return the radius value.
   */
  public Integer getRadius() {
    return this.radius;
  }

  /**
   * A method to get the blocks count.
   *
   * @return the block counter.
   */
  public Integer getBlockCounter() {
    return this.blockCounter;
  }

  /**
   * Retrieves mapped city blocks.
   *
   * @return map of city blocks.
   */
  public Map<Coordinates, CityBlock> getBlocks() {
    return new HashMap<>(this.cityBlockMap);
  }

  /**
   * Retrieves the current limit on rent cost of the city.
   *
   * @return a double representing the limit on rent cost for the city.
   */
  public Money getGlobalMaxRentCost() {
    return new Money(this.globalMaxRentCost);
  }

  /**
   * Retrieves the current limit set for construction cost.
   *
   * @return a double representing the limit on construction cost for the city.
   */
  public Money getGlobalMaxConstructionCost() {
    return new Money(this.globalMaxConstructionCost);
  }

  /**
   * sets the new population number of the City
   *
   * @param population new population
   */
  public void setPopulation(Long population) {
    this.population = population;
  }

  /**
   * sets the new radius of the City
   *
   * @param radius new radius
   */
  public void setRadius(Integer radius) {
    this.radius = radius;
  }

  /**
   * sets the new number of blocks
   *
   * @param blockCounter new number of blocks
   */
  public void setBlockCounter(Integer blockCounter) {
    this.blockCounter = blockCounter;
  }

  /**
   * Sets the CityBlockMap
   *
   * @param cityBlockMap map of blocks
   */
  public void setCityBlockMap(Map<Coordinates, CityBlock> cityBlockMap) {
    this.cityBlockMap = new HashMap<>(cityBlockMap);
  }

  /**
   * Sets the city's global limit on rent cost.
   *
   * @param maxRentCost the new rent cost to be set to the city.
   */
  public void setGlobalMaxRentCost(Money maxRentCost) {
    this.globalMaxRentCost = new Money(maxRentCost);
  }

  /**
   * Sets the city's global limit on construction cost.
   *
   * @param maxConstructionCost the new construction cost limit of the city.
   */
  public void setGlobalMaxConstructionCost(Money maxConstructionCost) {
    this.globalMaxConstructionCost = new Money(maxConstructionCost);
  }

  /**
   * Performs a deep comparison between the fields
   *
   * @param o object to compare to
   * @return true or false
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    City other = (City) o;
    return Objects.equals(id, other.id)
        && Objects.equals(population, other.population)
        && Objects.equals(radius, other.radius)
        && Objects.equals(blockCounter, other.blockCounter)
        && cityBlockMap.equals(other.cityBlockMap)
        && Objects.equals(globalMaxRentCost, other.globalMaxRentCost)
        && Objects.equals(globalMaxConstructionCost, other.globalMaxConstructionCost);
  }

  /**
   * Calculates hash code of the Simulation object
   *
   * @return hash
   */
  @Override
  public int hashCode() {
    return Objects.hash(
        id,
        population,
        radius,
        blockCounter,
        cityBlockMap,
        globalMaxRentCost,
        globalMaxConstructionCost);
  }
}
