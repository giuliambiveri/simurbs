package ch.usi.inf.bsc.sa4.lab02spring.controller.dto;

import static ch.usi.inf.bsc.sa4.lab02spring.model.Currency.CHF;

import ch.usi.inf.bsc.sa4.lab02spring.model.Money;

/**
 * an input city simulation DTO to create a new city simulation.
 *
 * @param avgIncome average income for families (per month)
 * @param commuterCost the commuter costs (per KM and per month)
 * @param name the name of the city
 * @param restrictionsJSON restrictions in JSON format
 */
public record CreateSimulationDTO(
    Money avgIncome,
    Money commuterCost,
    String name,
    Money globalConstructionCostRestriction,
    Money globalRentCostRestriction,
    String restrictionsJSON) {

  /**
   * Cosntructor for no global restrictions
   *
   * @param avgIncome average income for families (per month)
   * @param commuterCost the commuter costs (per KM and per month)
   * @param name the name of the city
   * @param restrictionsJSON restrictions in JSON format
   */
  public CreateSimulationDTO(
      Money avgIncome, Money commuterCost, String name, String restrictionsJSON) {
    this(avgIncome, commuterCost, name, new Money(0.0, CHF), new Money(0.0, CHF), restrictionsJSON);
  }

  @Override
  public Money avgIncome() {
    return new Money(avgIncome);
  }

  @Override
  public Money commuterCost() {
    return new Money(commuterCost);
  }

  @Override
  public Money globalConstructionCostRestriction() {
    return new Money(globalConstructionCostRestriction);
  }

  @Override
  public Money globalRentCostRestriction() {
    return new Money(globalRentCostRestriction);
  }
}
