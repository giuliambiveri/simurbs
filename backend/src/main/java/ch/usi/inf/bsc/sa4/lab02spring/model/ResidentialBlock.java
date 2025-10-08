package ch.usi.inf.bsc.sa4.lab02spring.model;

import java.util.Objects;
import org.springframework.data.annotation.PersistenceCreator;

/** Represents a residential block within a city. */
public class ResidentialBlock implements CityBlock {

  /** representation of the building present in the block */
  private Building building;

  /**
   * this is the cost per square meters in chf it is determined by the distance from CBD but when
   * passed to constructor this will have been already taken into account
   */
  private Money rentCost;

  /** Cost of land */
  private Money landPrice;

  /** Type of block */
  private BlockType type;

  /** The block type restriction */
  private BlockType blockTypeRestriction;

  /** Transportation cost */
  private Money transportationCost;

  /** The effective construction cost of the block */
  private Money constructionCost;

  /** the maximum rent cost of the single residential block */
  private Money localLimitRentCost;

  /** the maximum construction cost of the single residential block */
  private Money localLimitConstructionCost;

  /** the maximum transportation cost of the single residential block */
  private Money localLimitTransportationCost;

  /**
   * Constructs a ResidentialBlock.
   *
   * @param building the specific building located on this block.
   * @param rentCost the cost per square meter in CHF.
   * @param localLimitRentCost the maximum rent cost specific for this block.
   * @param localLimitConstructionCost the maximum construction cost for this block.
   * @spec.requires cost >= 0
   */
  @PersistenceCreator
  public ResidentialBlock(
      Building building,
      Money rentCost,
      Money landPrice,
      Money transportationCost,
      Money constructionCost,
      Money localLimitConstructionCost,
      Money localLimitTransportationCost,
      Money localLimitRentCost) {
    this.building = new Building(building);
    this.rentCost = new Money(rentCost);
    this.landPrice = new Money(landPrice);
    this.type = BlockType.RESIDENTIALBLOCK;
    this.transportationCost = new Money(transportationCost);
    this.constructionCost = new Money(constructionCost);
    this.localLimitRentCost = new Money(localLimitRentCost);
    this.localLimitConstructionCost = new Money(localLimitConstructionCost);
    this.localLimitTransportationCost = new Money(localLimitTransportationCost);
    this.blockTypeRestriction = BlockType.NONE;
  }

  /**
   * Constructor used to store also the blockTypeRestriction
   *
   * @param building instance
   * @param rentCost instance
   * @param transportationCost instance
   * @param constructionCost instance
   * @param localLimitConstructionCost instance
   * @param localLimitTransportationCost instance
   * @param localLimitRentCost instance
   * @param blockTypeRestriction instance
   */
  public ResidentialBlock(
      Building building,
      Money rentCost,
      Money landPrice,
      Money transportationCost,
      Money constructionCost,
      Money localLimitConstructionCost,
      Money localLimitTransportationCost,
      Money localLimitRentCost,
      BlockType blockTypeRestriction) {
    this.building = new Building(building);
    this.rentCost = new Money(rentCost);
    this.landPrice = new Money(landPrice);
    this.type = BlockType.RESIDENTIALBLOCK;
    this.transportationCost = new Money(transportationCost);
    this.constructionCost = new Money(constructionCost);
    this.localLimitRentCost = new Money(localLimitRentCost);
    this.localLimitConstructionCost = new Money(localLimitConstructionCost);
    this.localLimitTransportationCost = new Money(localLimitTransportationCost);
    this.blockTypeRestriction = blockTypeRestriction;
  }

  /**
   * constructor for defensive copy
   *
   * @param other original instance
   */
  public ResidentialBlock(ResidentialBlock other) {
    this.building = new Building(other.building);
    this.rentCost = other.rentCost;
    this.landPrice = other.landPrice;
    this.type = other.type;
    this.transportationCost = other.transportationCost;
    this.constructionCost = other.constructionCost;
    this.localLimitRentCost = other.localLimitRentCost;
    this.localLimitConstructionCost = other.localLimitConstructionCost;
    this.localLimitTransportationCost = other.localLimitTransportationCost;
    this.blockTypeRestriction = other.blockTypeRestriction;
  }

  /**
   * Returns the type of this block.
   *
   * @return the BlockType of the block.
   */
  @Override
  public BlockType getBlockType() {
    return this.type;
  }

  /**
   * Gets the restriction for the block
   *
   * @return restriction
   */
  @Override
  public BlockType getBlockTypeRestriction() {
    return blockTypeRestriction;
  }

  /**
   * Sets the restriction on the block
   *
   * @param blockType restriction
   */
  @Override
  public void setBlockTypeRestriction(BlockType blockType) {
    this.blockTypeRestriction = blockType;
  }

  /**
   * Returns the type of building in this block.
   *
   * @return the BuildingType of the building.
   */
  public BuildingType getBuildingType() {
    return this.building.getBuildingType();
  }

  /**
   * Returns the rent cost of this block.
   *
   * @return the rentCost of the block.
   */
  public Money getRentCost() {
    return new Money(this.rentCost);
  }

  /**
   * Returns the land price of this block.
   *
   * @return the land price of the block.
   */
  public Money getLandPrice() {
    return new Money(landPrice);
  }

  /**
   * Returns the building object.
   *
   * @return the building object.
   */
  public Building getBuilding() {
    return new Building(building);
  }

  /**
   * @return construction cost of the block
   */
  public Money getConstructionCost() {
    return new Money(this.constructionCost);
  }

  /**
   * Sets the construction cost for the block
   *
   * @param constructionCost the value
   */
  public void setConstructionCost(Money constructionCost) {
    this.constructionCost = new Money(constructionCost);
  }

  /**
   * Returns the transportation cost.
   *
   * @return the transportation cost.
   */
  public Money getTransportationCost() {
    return new Money(this.transportationCost);
  }

  /**
   * Retrieves the current residential block's limit on rent cost.
   *
   * @return a double representing the limit on rent cost for the current residential block.
   */
  public Money getLocalLimitRentCost() {
    return new Money(this.localLimitRentCost);
  }

  /**
   * Retrieves the current residential block's limit on construction cost.
   *
   * @return a double representing the limit on construction cost for the current residential block.
   */
  public Money getLocalLimitConstructionCost() {
    return new Money(this.localLimitConstructionCost);
  }

  /**
   * Retrieves the current residential block's limit on transportation cost.
   *
   * @return a double representing the limit on transportation cost for the current residential
   *     block.
   */
  public Money getLocalLimitTransportationCost() {
    return new Money(this.localLimitTransportationCost);
  }

  /**
   * sets the new building type to the residential block
   *
   * @param building a new building
   */
  public void setBuilding(Building building) {
    this.building = new Building(building);
  }

  /**
   * sets the new rent cost of the residential block
   *
   * @param rentCost the new rent cost
   */
  public void setRentCost(Money rentCost) {
    this.rentCost = new Money(rentCost);
  }

  /**
   * sets the new land price of the residential block
   *
   * @param landPrice the new land price
   */
  public void setLandPrice(Money landPrice) {
    this.landPrice = new Money(landPrice);
  }

  /**
   * sets the type to the class
   *
   * @param type the new type of the block
   */
  public void setType(BlockType type) {
    this.type = type;
  }

  /**
   * sets the new transportation cost of the residential block.
   *
   * @param transportationCost the new transportation cost.
   */
  public void setTransportationCost(Money transportationCost) {
    this.transportationCost = new Money(transportationCost);
  }

  /**
   * Sets the block's limit on rent cost to a new value.
   *
   * @param newLocalMaxRentCost the new value to set for the block's limit on rent cost.
   */
  public void setLocalLimitRentCost(Money newLocalMaxRentCost) {
    this.localLimitRentCost = new Money(newLocalMaxRentCost);
  }

  /**
   * Set the block's limit on construction cost to a new value
   *
   * @param localMaxConstructionCost the new value to set for the block's limit on construction
   *     cost.
   */
  public void setLocalLimitConstructionCost(Money localMaxConstructionCost) {
    this.localLimitConstructionCost = new Money(localMaxConstructionCost);
  }

  /**
   * Set the block's limit on transportation cost to a new value
   *
   * @param localLimitTransportationCost the new value to set for the block's limit on
   *     transportation cost.
   */
  public void setLocalLimitTransportationCost(Money localLimitTransportationCost) {
    this.localLimitTransportationCost = new Money(localLimitTransportationCost);
  }

  /**
   * Custom equals for NonResidential class
   *
   * @param o Residential instance to compare to
   * @return Boolean
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ResidentialBlock that = (ResidentialBlock) o;
    return Objects.equals(building, that.building)
        && Objects.equals(rentCost, that.rentCost)
        && Objects.equals(landPrice, that.landPrice)
        && type == that.type
        && blockTypeRestriction == that.blockTypeRestriction
        && Objects.equals(transportationCost, that.transportationCost)
        && Objects.equals(constructionCost, that.constructionCost)
        && Objects.equals(localLimitRentCost, that.localLimitRentCost)
        && Objects.equals(localLimitConstructionCost, that.localLimitConstructionCost);
  }

  /**
   * hashing method
   *
   * @return hash
   */
  @Override
  public int hashCode() {
    return Objects.hash(
        building,
        rentCost,
        landPrice,
        type,
        blockTypeRestriction,
        transportationCost,
        constructionCost,
        localLimitRentCost,
        localLimitConstructionCost,
        localLimitTransportationCost);
  }
}
