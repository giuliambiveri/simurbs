package ch.usi.inf.bsc.sa4.lab02spring.model;

import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.Document;

/** Class that represents the users that have an account on the platform */
@Document(collection = "users")
public class User {
  /** id of the user */
  private final String id;

  /** first name of the user */
  private final String firstName;

  /** last name of the user */
  private final String lastName;

  /** email of the user */
  private final String email;

  /**
   * The constructor for the user
   *
   * @param id a String consisting of the user's switch id
   * @param firstName a String consisting of the user's first name
   * @param lastName a String consisting of the user's last name
   * @param email a String consisting of the user's email
   * @throws IllegalArgumentException if the firstname is null
   * @throws IllegalArgumentException if the lastname is null
   * @throws IllegalArgumentException if the email format is incorrect or null
   */
  public User(String id, String firstName, String lastName, String email) {
    if (firstName == null) {
      throw new IllegalArgumentException(
          "the first name cannot be null, it should be a valid name");
    }

    if (lastName == null) {
      throw new IllegalArgumentException(
          "the lastname should not be null, it should be a valid lastname");
    }

    if (email == null || email.indexOf('@') == -1) {
      throw new IllegalArgumentException("email format is invalid or null");
    }
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

  /**
   * Returns the id of the user
   *
   * @return the value of the id
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the first name of the user
   *
   * @return the value of the first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Returns the last name of the user
   *
   * @return the value of the last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Returns the email of the user
   *
   * @return the value of the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Performs a deep comparison between the fields of two Users
   *
   * @param o object to compare to
   * @return true if the two Users are equal or false if not
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final User user = (User) o;
    return Objects.equals(id, user.id)
        && Objects.equals(firstName, user.firstName)
        && Objects.equals(lastName, user.lastName)
        && Objects.equals(email, user.email);
  }

  /**
   * Calculates hash code of the User object
   *
   * @return hash
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, email);
  }
}
