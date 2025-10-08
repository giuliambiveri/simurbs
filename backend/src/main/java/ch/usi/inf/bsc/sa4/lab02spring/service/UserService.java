package ch.usi.inf.bsc.sa4.lab02spring.service;

import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.UserDTO;
import ch.usi.inf.bsc.sa4.lab02spring.model.User;
import ch.usi.inf.bsc.sa4.lab02spring.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/** A service class to support user management. */
@Service
public class UserService {
  /** variable definition to access the userRepository from this class */
  private final UserRepository userRepository;

  private final SimulationService simulationService;

  /**
   * instantiates the userRepository variable
   *
   * @param userRepository the instance
   */
  public UserService(UserRepository userRepository, SimulationService simulationService) {
    this.simulationService = simulationService;
    this.userRepository = userRepository;
  }

  /**
   * Returns all existing users.
   *
   * @return a list of all existing users.
   */
  public List<UserDTO> getAll() {
    final List<UserDTO> usersDTO = new ArrayList<>();
    final List<User> listOfUsers = this.userRepository.findAll();
    for (final User user : listOfUsers) {
      final double averageCommuter = simulationService.averageCommuter(user.getId());
      final double averageIncome = simulationService.averageIncome(user.getId());
      final double averagePopulation = simulationService.averagePopulationSize(user.getId());
      final Long count = simulationService.countSimulationsByUser(user.getId());
      final UserDTO userDTO =
          new UserDTO(user, averageCommuter, averageIncome, averagePopulation, count);
      usersDTO.add(userDTO);
    }
    return usersDTO;
  }

  /**
   * Persists any change to a user identified by its own ID.
   *
   * @param user a user.
   * @return the updated user.
   * @spec.modifies the persisted user.
   */
  public User storeUser(User user) {
    return userRepository.save(user);
  }

  /**
   * Looks for a user by its id.
   *
   * @param userId the id of the user
   * @return an optional which contains the user with the given id if it exists, otherwise an empty
   *     optional.
   * @spec.requires <code>userId != null</code>
   */
  public Optional<User> getById(String userId) {
    return userRepository.findById(userId);
  }
}
