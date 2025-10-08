package ch.usi.inf.bsc.sa4.lab02spring.controller.dto;

import ch.usi.inf.bsc.sa4.lab02spring.model.User;

/**
 * An output DTO representing a user's public information.
 *
 * @param id the user's id.
 * @param firstName the user's firstName.
 * @param lastName the user's lastName.
 * @param email the user's email.
 */
public record UserDTO(
    String id,
    String firstName,
    String lastName,
    String email,
    double averageCommuter,
    double averageIncome,
    double averagePopulation,
    Long count) {

  /**
   * Create a new UserDTO object.
   *
   * @param user User, the entity from which create the DTO.
   */
  public UserDTO(
      User user,
      double averageCommuter,
      double averageIncome,
      double averagePopulation,
      Long count) {
    this(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getEmail(),
        averageCommuter,
        averageIncome,
        averagePopulation,
        count);
  }
}
