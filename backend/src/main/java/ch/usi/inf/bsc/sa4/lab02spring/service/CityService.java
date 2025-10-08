package ch.usi.inf.bsc.sa4.lab02spring.service;

import static ch.usi.inf.bsc.sa4.lab02spring.utils.CityGenerator.createNewCity;

import ch.usi.inf.bsc.sa4.lab02spring.model.City;
import ch.usi.inf.bsc.sa4.lab02spring.repository.CityRepository;
import ch.usi.inf.bsc.sa4.lab02spring.utils.CityCreationParameters;
import org.springframework.stereotype.Service;

/** Service class for the city */
@Service
public class CityService {

  /** cityRepository instance to enable access to it */
  private final CityRepository cityRepository;

  /**
   * Constructor of the class
   *
   * @param cityRepository a cityRepository instance
   */
  public CityService(CityRepository cityRepository) {
    this.cityRepository = cityRepository;
  }

  /**
   * persists any change to a city (updating or adding)
   *
   * @param city a city
   * @return the updated/added city
   * @spec.modifies the persisted city
   */
  public City storeCity(City city) {
    return cityRepository.save(city);
  }

  /**
   * Creates a new City and persists it in the DB.
   *
   * @param parameters the instance of CityCreationParameters containing all parameters to create
   *     the city
   * @return a new City
   */
  public City createCity(CityCreationParameters parameters) {
    final City city = createNewCity(parameters);
    return this.storeCity(city);
  }

  /**
   * Deletes the requested city, identified by the Id field in the Database
   *
   * @param id the ID of the simulation
   */
  public boolean deleteCityById(String id) {
    try {
      cityRepository.deleteById(id);
      return true;
    } catch (IllegalStateException e) {
      return false;
    }
  }
}
