package ch.usi.inf.bsc.sa4.lab02spring.utils;

import static ch.usi.inf.bsc.sa4.lab02spring.model.Currency.CHF;
import static java.util.Collections.emptyMap;

import ch.usi.inf.bsc.sa4.lab02spring.model.BlockType;
import ch.usi.inf.bsc.sa4.lab02spring.model.Coordinates;
import ch.usi.inf.bsc.sa4.lab02spring.model.Currency;
import java.util.Map;

public record CityCreationParameters(
    Double wageParam,
    Double commuterCostParam,
    Double globalConstructionCostRestriction,
    Double globalRentCostRestriction,
    Map<Coordinates, Double> localTransportationCostRestrictions,
    Map<Coordinates, Double> localConstructionCostRestriction,
    Map<Coordinates, Double> localRentCostRestrictions,
    Map<Coordinates, BlockType> blockTypeRestrictions,
    Currency currency) {

  /**
   * Constructor with only wage adn commuter cost, all the other values are set to default
   *
   * @param wageParam value
   * @param commuterCostParam value
   */
  public CityCreationParameters(Double wageParam, Double commuterCostParam) {
    this(
        wageParam,
        commuterCostParam,
        0.0,
        0.0,
        emptyMap(),
        emptyMap(),
        emptyMap(),
        emptyMap(),
        CHF);
  }
}
