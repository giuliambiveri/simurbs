package ch.usi.inf.bsc.sa4.lab02spring.model;

import java.time.ZonedDateTime;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/** Represents the simulation of a city. */
@Document(collection = "simulations")
public class Simulation {
  /** id of the simulation */
  @Id private String id;

  /** Owner of the simulation, represented by its mongoDB id */
  @DocumentReference private final User user;

  /**
   * indicates whether the simulation is accessible by everyone, the variable is set to false by
   * default
   */
  private boolean isPublic;

  /** reference to the city of the simulation */
  @DocumentReference(collection = "cities")
  private City simulatedCity;

  /** name of the simulation */
  private String name;

  /** the average income of the population in the simulation */
  private Money averageIncome;

  /** cost for commuting in the city */
  private Money commuterCosts;

  /** date of creation of the simulation */
  private ZonedDateTime creationDate;

  /** number of views that the simulation has */
  private Integer views;

  /** the old simulation for the comparison mode */
  @DocumentReference(collection = "simulations")
  private Simulation oldSimulation;

  /** states if this simulation is the current in use or and old version */
  private boolean isOld;

  /**
   * Constructs a Simulation.
   *
   * @param averageIncome the average income of the city's population.
   * @param commuterCosts the average commuting cost.
   * @param name the name of the city.
   * @param user user of the simulation
   * @param id id of the simulation in the db
   * @param views counter of the simulation visualizations
   * @param oldSimulation the older version of this simulation
   * @throws IllegalArgumentException if name is null or empty.
   */
  @PersistenceCreator
  public Simulation(
      Money averageIncome,
      Money commuterCosts,
      String name,
      User user,
      String id,
      ZonedDateTime creationDate,
      Integer views,
      Simulation oldSimulation,
      boolean isOld) {

    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("City name must be non-null and not empty.");
    }

    this.id = id;
    this.user = user;
    this.name = name;
    this.averageIncome = averageIncome;
    this.commuterCosts = commuterCosts;
    this.creationDate = creationDate;
    this.views = views;
    if (oldSimulation != null) {
      this.oldSimulation = new Simulation(oldSimulation);
    } else {
      this.oldSimulation = null;
    }
    this.isOld = isOld;
  }

  /**
   * Constructs a Simulation.
   *
   * @param averageIncome the average income of the city's population.
   * @param commuterCosts the average commuting cost.
   * @param name the name of the city.
   */
  public Simulation(
      Money averageIncome,
      Money commuterCosts,
      String name,
      User user,
      ZonedDateTime creationDate,
      Integer views,
      Simulation oldSimulation,
      boolean isOld) {
    this(averageIncome, commuterCosts, name, user, null, creationDate, views, oldSimulation, isOld);
  }

  /**
   * Deep copy function to secure information transferred to the frontend
   *
   * @param other the simulation instance to be copied
   */
  public Simulation(Simulation other) {
    if (other == null) {
      throw new IllegalArgumentException("Cannot copy a null object.");
    }
    this.id = other.id;
    this.user = other.user;
    this.isPublic = other.isPublic;
    this.simulatedCity = new City(other.simulatedCity);
    this.name = other.name;
    this.averageIncome = other.averageIncome;
    this.commuterCosts = other.commuterCosts;
    this.creationDate = other.creationDate;
    this.views = other.views;
    this.oldSimulation = other.oldSimulation != null ? new Simulation(other.oldSimulation) : null;
    this.isOld = other.isOld;
  }

  /**
   * Returns the id of the simulation.
   *
   * @return the id of the simulation.
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the id of the city simulation's user.
   *
   * @return the id of the city simulation's User.
   */
  public User getUser() {
    return user;
  }

  /**
   * Returns if simulation is private or not.
   *
   * @return true if the city simulation is public false otherwise.
   */
  public boolean isPublic() {
    return isPublic;
  }

  /**
   * Returns the City object.
   *
   * @return the simulated City.
   */
  public City getSimulatedCity() {
    return new City(simulatedCity);
  }

  /**
   * Returns the name of the city.
   *
   * @return the city name.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the average income.
   *
   * @return the average income.
   */
  public Money getAverageIncome() {
    return this.averageIncome;
  }

  /**
   * Returns the commuter costs.
   *
   * @return he commuter costs.
   */
  public Money getCommuterCosts() {
    return this.commuterCosts;
  }

  /**
   * Returns the creation date of the simulation.
   *
   * @return the creation date of the simulation.
   */
  public ZonedDateTime getCreationDate() {
    return creationDate;
  }

  /**
   * Returns the views count of the simulation.
   *
   * @return the views count of the simulation.
   */
  public Integer getViews() {
    return views;
  }

  /**
   * Returns old Simulation of this simulation
   *
   * @return the old simulation of this simulation.
   */
  public Simulation getOldSimulation() {
    if (oldSimulation != null) {
      return new Simulation(oldSimulation);
    } else {
      return null;
    }
  }

  /**
   * returns if a simulation is old or not
   *
   * @return true if the simulation is old or false otherwise
   */
  public boolean isOld() {
    return isOld;
  }

  /**
   * Sets a new visibility to the simulation.
   *
   * @param aPublic boolean value representing the new visibility of the simulation.
   */
  public Simulation setPublic(boolean aPublic) {
    isPublic = aPublic;
    return this;
  }

  /**
   * Sets a new simulatedCity to the simulation.
   *
   * @param simulatedCity a City representing the new simulatedCity.
   */
  public void setSimulatedCity(City simulatedCity) {
    this.simulatedCity = new City(simulatedCity);
  }

  /**
   * Sets a new name to the simulation.
   *
   * @param name a string representing the new name of the simulation.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets a new average income to the simulation.
   *
   * @param averageIncome a Double representing the new average income of the simulation.
   */
  public void setAverageIncome(Money averageIncome) {
    this.averageIncome = averageIncome;
  }

  /**
   * Sets a new commuter costs to the simulation.
   *
   * @param commuterCosts Double representing the new commuter costs of the simulation.
   */
  public void setCommuterCosts(Money commuterCosts) {
    this.commuterCosts = commuterCosts;
  }

  /**
   * Sets a new creation date to the simulation.
   *
   * @param newCreationDate a ZonedDateTime representing the new creation date of the simulation.
   */
  public void setCreationDate(ZonedDateTime newCreationDate) {
    this.creationDate = newCreationDate;
  }

  /**
   * Sets a new views count to the simulation.
   *
   * @param newViews an Integer representing the new views count of the simulation.
   */
  public void setViews(Integer newViews) {
    this.views = newViews;
  }

  /**
   * Sets an oldSimulation in the oldSimulation field
   *
   * @param oldSimulation the older simulation of this simulation
   */
  public void setOldSimulation(Simulation oldSimulation) {
    this.oldSimulation = oldSimulation != null ? new Simulation(oldSimulation) : null;
  }

  /**
   * sets the boolean parameter isOld when True the simulation is old when false the simulation is
   * the current in use
   *
   * @param old boolean value true or false
   */
  public void setOld(boolean old) {
    this.isOld = old;
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
    final Simulation that = (Simulation) o;

    return isPublic == that.isPublic
        && isOld == that.isOld
        && Objects.equals(id, that.id)
        && Objects.equals(user, that.user)
        && simulatedCity.equals(that.simulatedCity)
        && Objects.equals(name, that.name)
        && averageIncome.equals(that.averageIncome)
        && commuterCosts.equals(that.commuterCosts)
        && Objects.equals(views, that.views)
        && Objects.equals(this.oldSimulation, that.oldSimulation);
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
        user,
        isPublic,
        simulatedCity,
        name,
        averageIncome,
        commuterCosts,
        creationDate,
        views,
        oldSimulation,
        isOld);
  }
}
