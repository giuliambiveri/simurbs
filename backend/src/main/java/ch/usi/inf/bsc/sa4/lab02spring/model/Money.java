package ch.usi.inf.bsc.sa4.lab02spring.model;

import java.util.Objects;
import org.springframework.data.annotation.PersistenceCreator;

/**
 * Represents the rental cost with a specified value and currency.
 *
 * @spec.requires value >= 0
 * @spec.modifies nothing.
 */
public class Money {
  /** Value of the money representation */
  private Double value;

  /** Currency of the money representation */
  private Currency currency;

  /**
   * Persistence constructor for the Money class
   *
   * @param value monetary value
   * @param currency in which the money is represented in
   */
  @PersistenceCreator
  public Money(Double value, Currency currency) {
    this.value = value;
    this.currency = currency;
  }

  /**
   * Default constructor which puts CHF as default currency
   *
   * @param value monetary value
   */
  public Money(Double value) {
    this.value = value;
    this.currency = Currency.CHF;
  }

  /**
   * constructor for deep copy
   *
   * @param other the instance to be copied
   */
  public Money(Money other) {
    this.value = other.value;
    this.currency = other.currency;
  }

  // Getter and setter methods for value and currency
  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  @Override
  public String toString() {
    return "Money{" + "value=" + value + ", currency=" + currency + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Money money = (Money) o;
    return Objects.equals(value, money.value) && currency == money.currency;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, currency);
  }
}
