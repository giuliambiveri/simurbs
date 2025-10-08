package ch.usi.inf.bsc.sa4.lab02spring.modelTests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.usi.inf.bsc.sa4.lab02spring.model.Currency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Currency Enum Tests")
public class CurrencyTests {

  @Test
  @DisplayName("Currency enum should contain CHF")
  public void currencyEnumShouldContainCHF() {
    var containsCHF = false;
    for (Currency currency : Currency.values()) {
      if (currency == Currency.CHF) {
        containsCHF = true;
        break;
      }
    }
    assertTrue(containsCHF);
  }
}
