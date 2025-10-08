package ch.usi.inf.bsc.sa4.lab02spring.modelTests;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.bsc.sa4.lab02spring.model.Coordinates;
import ch.usi.inf.bsc.sa4.lab02spring.model.Currency;
import ch.usi.inf.bsc.sa4.lab02spring.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Money Class Tests")
public class MoneyTests {

  @Test
  @DisplayName("Money creation with valid amount")
  public void testMoneyCreationValidAmount() {

    double expectedValue = 1000.0;
    Currency expectedCurrency = Currency.CHF;

    Money rentCost = new Money(expectedValue, expectedCurrency);

    assertEquals(expectedValue, rentCost.getValue());
  }

  @Test
  @DisplayName("Money creation with valid currency")
  public void testMoneyCreationValidCurrency() {

    double expectedValue = 1000.0;
    Currency expectedCurrency = Currency.CHF;

    Money rentCost = new Money(expectedValue, expectedCurrency);

    assertEquals(expectedCurrency, rentCost.getCurrency());
  }

  @Test
  @DisplayName("Money equals method works correctly for identical values and currencies")
  public void testMoneyEqualityForSameValues() {

    Money money1 = new Money(500.0, Currency.CHF);
    Money money2 = new Money(500.0, Currency.CHF);

    assertEquals(money1, money2);
  }

  @Test
  @DisplayName("Money toString returns the correct string representation")
  public void testMoneyToString() {

    Money money = new Money(750.0, Currency.CHF);
    String expectedString = "Money{value=750.0, currency=CHF}";

    String actualString = money.toString();

    assertEquals(expectedString, actualString);
  }

  @Test
  @DisplayName("Method setMoney will correctly set the new money value")
  public void testSetMoneyValue() {

    Money money = new Money(750.0, Currency.CHF);
    money.setValue(760.0);
    assertEquals(760.0, money.getValue());
  }

  @Test
  @DisplayName("Method setCurrency will correctly set the new currency")
  public void testSetCurrencyValue() {

    Currency currency = Currency.CHF;
    Currency differentCurrency = Currency.CHF;

    Money money = new Money(750.0, currency);
    money.setCurrency(differentCurrency);
    assertEquals(differentCurrency, money.getCurrency());
  }

  @Test
  @DisplayName("Method equals returns false when the comparison object is null ")
  public void testEqualsWithNullObject() {

    Currency currency = Currency.CHF;
    Money money1 = null;
    Money money = new Money(750.0, currency);
    assertFalse(money.equals(money1));
  }

  @Test
  @DisplayName("Method equals returns false when the comparison object is not a Money type")
  public void testEqualsWith() {

    Currency currency = Currency.CHF;
    Coordinates money1 = new Coordinates(2, 2);
    Money money = new Money(750.0, currency);
    assertFalse(money.equals(money1));
  }

  @Test
  @DisplayName("Method equals returns true because comparison was successfull")
  public void testEqualsIsSuccessfull() {

    Currency currency = Currency.CHF;
    Money money = new Money(750.0, currency);
    Money money2 = new Money(750.0, currency);
    assertTrue(money.equals(money2));
  }

  @Test
  @DisplayName("Method equals returns true because comparison was not successfull")
  public void testEqualsWasNotSuccessfull() {

    Money money = new Money(750.0, Currency.CHF);
    Money money2 = new Money(750.0, null);
    assertFalse(money.equals(money2));
  }
}
