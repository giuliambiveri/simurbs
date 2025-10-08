package ch.usi.inf.bsc.sa4.lab02spring.model;

import java.util.Objects;
import org.springframework.data.annotation.PersistenceCreator;

/**
 * Represents a building of our city The height of the building is modeled in number of floors
 * Height is not final and hypothetically changeable
 */
public class Building {

  /** height of building expressed in number of floors */
  private Integer height;

  /** number of people living in the building */
  private Long people;

  /** type of the building */
  private final BuildingType type;

  /**
   * Constructs a Building instance
   *
   * @param height the number of floors in the building, it also represents height
   * @param type the type of the building
   * @param people number of people that live in the block
   * @throws IllegalArgumentException if floorNumber is negative or null and if bType is not valid
   * @spec.requires floorNumber must be a non-negative number
   * @spec.requires bType must be a valid instance of the BuildingType enum.
   */
  @PersistenceCreator
  public Building(Integer height, BuildingType type, Long people) {
    this.height = height;
    this.type = type;
    this.people = people;
  }

  /**
   * constructor for defensive copy
   *
   * @param other original instance
   */
  public Building(Building other) {
    if (other == null) {
      throw new IllegalArgumentException("Cannot copy a null object.");
    }
    this.height = other.height;
    this.type = other.type;
    this.people = other.people;
  }

  /**
   * Returns the height of the building in terms of number of floors
   *
   * @return the height of the building in number of floors
   */
  public Integer getHeight() {
    return this.height;
  }

  /**
   * Returns number of people in this block
   *
   * @return the number of people in this block
   */
  public Long getPeople() {
    return people;
  }

  /**
   * Returns the type of the Building
   *
   * @return BuildingType
   */
  public BuildingType getBuildingType() {
    return this.type;
  }

  /**
   * sets the new height to the building
   *
   * @param height new height
   */
  public void setHeight(Integer height) {
    this.height = height;
  }

  /**
   * sets the new number of people in the building
   *
   * @param people new number of people
   */
  public void setPeople(Long people) {
    this.people = people;
  }

  /**
   * Custom equals for NonResidential class
   *
   * @param o Building instance to compare to
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
    final Building building = (Building) o;
    return Objects.equals(height, building.height)
        && Objects.equals(people, building.people)
        && type == building.type;
  }

  /**
   * hashing method
   *
   * @return hash
   */
  @Override
  public int hashCode() {
    return Objects.hash(height, people, type);
  }
}
