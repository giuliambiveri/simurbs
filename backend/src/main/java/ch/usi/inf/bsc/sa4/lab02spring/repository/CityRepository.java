package ch.usi.inf.bsc.sa4.lab02spring.repository;

import ch.usi.inf.bsc.sa4.lab02spring.model.City;
import org.springframework.data.mongodb.repository.MongoRepository;

/** Repository class to communicate with the DB for the city collection */
public interface CityRepository extends MongoRepository<City, String> {}
