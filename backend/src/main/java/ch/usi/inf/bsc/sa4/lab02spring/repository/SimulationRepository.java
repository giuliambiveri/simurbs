package ch.usi.inf.bsc.sa4.lab02spring.repository;

import ch.usi.inf.bsc.sa4.lab02spring.model.Simulation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/** Repository class for the simulation, used to communicate with mongoDB */
@Repository
public interface SimulationRepository extends MongoRepository<Simulation, String> {

  /**
   * Searches list of simulations based on the userId
   *
   * @param string userId
   * @return list of simulations of the user
   */
  List<Simulation> findByUser(String string);

  /**
   * Finds a public simulation by its id and public field
   *
   * @param id id of the Simulation
   * @param isPublic true if Simulation is public
   * @return Simulation
   */
  Optional<Simulation> findByIdAndIsPublic(String id, boolean isPublic);

  /**
   * Searches a list of simulations based on a userId and filters based on the isPublic parameter
   *
   * @param userId id of the user stored in mongoDB
   * @param isPublic to search based if the simulations are public or not
   * @return list of simulations
   */
  List<Simulation> findByUserIdAndIsPublic(String userId, boolean isPublic);

  /**
   * Used to count the number of simulations under a user
   *
   * @param userId the user's id
   * @return the number of simulations
   */
  Long countByUserAndIsOldIsFalse(String userId);

  /**
   * returns a list of simulations that are not old
   *
   * @param userId the user's id
   * @return list of simulations of the user that are not old
   */
  List<Simulation> findByUserIdAndIsOldIsFalse(String userId);
}
