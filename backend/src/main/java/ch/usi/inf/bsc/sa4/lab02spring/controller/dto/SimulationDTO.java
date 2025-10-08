package ch.usi.inf.bsc.sa4.lab02spring.controller.dto;

import ch.usi.inf.bsc.sa4.lab02spring.model.City;
import ch.usi.inf.bsc.sa4.lab02spring.model.Money;
import ch.usi.inf.bsc.sa4.lab02spring.model.Simulation;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * an output city simulation DTO representing a city simulation's public information.
 *
 * @param averageIncome the city simulation's average income of families (per month)
 * @param commuterCosts the city simulation's commuter costs (per km and per month)
 * @param name the city simulation's name
 * @param city the city simulation's city object
 */
public record SimulationDTO(
    String id,
    Money averageIncome,
    Money commuterCosts,
    String name,
    City city,
    boolean isPublic,
    ZonedDateTime creationDate,
    Integer views,
    Simulation oldSimulation) {
  public SimulationDTO(Simulation simulatedCity) {
    this(
        simulatedCity.getId(),
        simulatedCity.getAverageIncome(),
        simulatedCity.getCommuterCosts(),
        simulatedCity.getName(),
        simulatedCity.getSimulatedCity(),
        simulatedCity.isPublic(),
        simulatedCity.getCreationDate(),
        simulatedCity.getViews(),
        createOldSimulation(simulatedCity.getOldSimulation()));
  }

  /**
   * Setter for old simulation
   *
   * @param oldSimulation instance
   * @return deep copy of old simulation or null
   */
  private static Simulation createOldSimulation(Simulation oldSimulation) {
    if (oldSimulation != null) {
      return new Simulation(oldSimulation);
    } else {
      return null;
    }
  }

  /**
   * does a deep copy of the city stored in the instance, for security purposes
   *
   * @return deep copy of the city
   */
  public City city() {
    return new City(this.city);
  }

  /**
   * Returns a deep copy of the old simulation stored in the instance, for security purposes.
   *
   * @return Deep copy of the old simulation.
   */
  public Simulation oldSimulation() {
    if (this.oldSimulation != null) {
      return new Simulation(this.oldSimulation);
    } else {
      return null;
    }
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
    final SimulationDTO that = (SimulationDTO) o;
    return isPublic == that.isPublic
        && Objects.equals(id, that.id)
        && Objects.equals(city, that.city)
        && Objects.equals(name, that.name)
        && Objects.equals(views, that.views)
        && Objects.equals(averageIncome, that.averageIncome)
        && Objects.equals(commuterCosts, that.commuterCosts)
        && Objects.equals(oldSimulation, that.oldSimulation)
        && Objects.equals(creationDate, that.creationDate);
  }

  /**
   * Hashcode method
   *
   * @return hash
   */
  @Override
  public int hashCode() {
    return Objects.hash(
        averageIncome, commuterCosts, name, city, isPublic, creationDate, views, oldSimulation);
  }
}
