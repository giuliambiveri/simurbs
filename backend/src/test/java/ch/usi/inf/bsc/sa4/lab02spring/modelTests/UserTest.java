package ch.usi.inf.bsc.sa4.lab02spring.modelTests;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.bsc.sa4.lab02spring.model.Coordinates;
import ch.usi.inf.bsc.sa4.lab02spring.model.User;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("In the User model")
public class UserTest {
  @Test
  @DisplayName(
      "Method getId should return 123abc when id = 123abc, firstName = Edoardo, lastName = Ababei, email = ababee@usi.ch ")
  public void testGetId() {
    // arrange/setup
    String id = "123abc";
    String firstName = "Edoardo";
    String lastName = "Ababei";
    String email = "ababee@usi.ch";
    // act/exercise
    String result = new User(id, firstName, lastName, email).getId();
    // assert/verify
    assertEquals("123abc", result);
  }

  @Test
  @DisplayName(
      "Method getFistName should return Federico when id = 45778a77t, firstName = Federico, lastName = Sala, email = salaa@usi.ch")
  public void testGetFirstName() {
    // arrange/setup
    String id = "45778a77t";
    String firstName = "Federico";
    String lastName = "Sala";
    String email = "salaa@usi.ch";
    // act/exercise
    String result = new User(id, firstName, lastName, email).getFirstName();
    // assert/verify
    assertEquals("Federico", result);
  }

  @Test
  @DisplayName("Method getFirstName should throw if firstName is null")
  public void testFirstNameIsNotNull() {
    String id = "67994ddg4";
    String firstName = null;
    String lastName = "Favarin";
    String email = "favar@usi.ch";
    assertThrows(
        IllegalArgumentException.class,
        () -> new User(id, firstName, lastName, email),
        "firstName must not be null");
  }

  @Test
  @DisplayName(
      "Method getLastName should return Sala when id = 45778a77t, firstName = Federico, lastName = Sala, email = salaa@usi.ch")
  public void testGetLastName() {
    // arrange/setup
    String id = "45778a77t";
    String firstName = "Federico";
    String lastName = "Sala";
    String email = "salaa@usi.ch";
    // act/exercise
    String result = new User(id, firstName, lastName, email).getLastName();
    // assert/verify
    assertEquals("Sala", result);
  }

  @Test
  @DisplayName("Method getFirstName should throw is lastName is null")
  public void testLastNameIsNotNull() {
    String id = "67994ddg4";
    String firstName = "Federico";
    String lastName = null;
    String email = "favar@usi.ch";
    assertThrows(
        IllegalArgumentException.class,
        () -> new User(id, firstName, lastName, email),
        "lastName must not be null");
  }

  @Test
  @DisplayName(
      "Method getEmail should return lillog@usi.ch when id = 345ac22rt, firstName = Giorgia, lastName = Lillo, email = lillog@usi.ch")
  public void testGetEmail() {
    // arrange/setup
    String id = "345ac22rt";
    String firstName = "Giorgia";
    String lastName = "Lillo";
    String email = "lillog@usi.ch";
    // act/exercise
    String result = new User(id, firstName, lastName, email).getEmail();
    // assert/verify
    assertEquals("lillog@usi.ch", result);
  }

  @Test
  @DisplayName("When email is null should throw illegalArgumentException")
  public void testEmailIsNotNull() {
    String id = "93729dm08";
    String firstName = "Sergio";
    String lastName = "Boffi";
    String email = null;
    assertThrows(
        IllegalArgumentException.class,
        () -> new User(id, firstName, lastName, email),
        "email must not be null");
  }

  @Test
  @DisplayName("When '@' is not present in the email should throw illegalArgumentException")
  public void testEmailIsNotValid() {
    String id = "93729dm08";
    String firstName = "Sergio";
    String lastName = "Boffi";
    String email = "Lambevela.com";
    assertThrows(
        IllegalArgumentException.class,
        () -> new User(id, firstName, lastName, email),
        "'@' not present in the email");
  }

  @Test
  @DisplayName("Method testUserIsNull should return true when the Object is null")
  public void testUserIsNull() {
    // arrange/setup
    User user1 = null;
    String id = "345ac22rt";
    String firstName = "Giorgia";
    String lastName = "Lillo";
    String email = "lillog@usi.ch";
    // act/exercise
    User user2 = new User(id, firstName, lastName, email);
    boolean result = user2.equals(user1);
    // assert/verify
    assertFalse(result);
  }

  @Test
  @DisplayName("Method testUserIsNotUser should return true when the Object is not a User")
  public void testUserIsNotUser() {
    // arrange/setup
    Integer x = 3;
    Integer y = 4;
    String id = "345ac22rt";
    String firstName = "Giorgia";
    String lastName = "Lillo";
    String email = "lillog@usi.ch";
    // act/exercise
    Coordinates coordinates = new Coordinates(x, y);
    User user2 = new User(id, firstName, lastName, email);
    boolean result = user2.equals(coordinates);
    // assert/verify
    assertFalse(result);
  }

  @Test
  @DisplayName("Method testEqualUser should return true when two users are the same")
  public void testEqualUsers() {
    // arrange/setup
    String id = "345ac22rt";
    String firstName = "Giorgia";
    String lastName = "Lillo";
    String email = "lillog@usi.ch";

    // act/exercise
    User user1 = new User(id, firstName, lastName, email);
    User user2 = new User(id, firstName, lastName, email);
    boolean result = user1.equals(user2);
    // assert/verify
    assertTrue(result);
  }

  @Test
  @DisplayName("Method testDifferentId should return true when two users has different id")
  public void testDifferentId() {
    // arrange/setup
    String id1 = "345ac22rt";
    String id2 = "345ac21rt";
    String firstName1 = "Giorgia";
    String lastName1 = "Lillo";
    String email1 = "lillog@usi.ch";

    // act/exercise
    User user1 = new User(id1, firstName1, lastName1, email1);
    User user2 = new User(id2, firstName1, lastName1, email1);
    boolean result = user1.equals(user2);
    // assert/verify
    assertFalse(result);
  }

  @Test
  @DisplayName(
      "Method testDifferentFirsName should return true when two users has different first names")
  public void testDifferentFirstName() {
    // arrange/setup
    String id = "345ac22rt";
    String firstName1 = "Giorgia";
    String firstName2 = "Giorgie";
    String lastName1 = "Lillo";
    String email1 = "lillog@usi.ch";

    // act/exercise
    User user1 = new User(id, firstName1, lastName1, email1);
    User user2 = new User(id, firstName2, lastName1, email1);
    boolean result = user1.equals(user2);
    // assert/verify
    assertFalse(result);
  }

  @Test
  @DisplayName(
      "Method testDifferentLastName should return true when two users has different last names")
  public void testDifferentLastName() {
    // arrange/setup
    String id = "345ac22rt";
    String firstName = "Giorgia";
    String lastName1 = "Lillo";
    String lastName2 = "Della";
    String email1 = "lillog@usi.ch";

    // act/exercise
    User user1 = new User(id, firstName, lastName1, email1);
    User user2 = new User(id, firstName, lastName2, email1);
    boolean result = user1.equals(user2);
    // assert/verify
    assertFalse(result);
  }

  @Test
  @DisplayName("Method testDifferentEmail should return true when two users has different email")
  public void testDifferentEmail() {
    // arrange/setup
    String id = "345ac22rt";
    String firstName = "Giorgia";
    String lastName = "Lillo";
    String email1 = "lillog@usi.ch";
    String email2 = "aboaba@usi.ch";

    // act/exercise
    User user1 = new User(id, firstName, lastName, email1);
    User user2 = new User(id, firstName, lastName, email2);
    boolean result = user1.equals(user2);
    // assert/verify
    assertFalse(result);
  }

  @Test
  @DisplayName("Method testHashCode should return given a User")
  public void testHashCode() {
    // arrange/setup
    String id = "345ac22rt";
    String firstName = "Giorgia";
    String lastName = "Lillo";
    String email = "lillog@usi.ch";

    // act/exercise
    User user = new User(id, firstName, lastName, email);
    // assert/verify
    assertEquals(user.hashCode(), Objects.hash(id, firstName, lastName, email));
  }
}
