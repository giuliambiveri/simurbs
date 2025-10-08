package ch.usi.inf.bsc.sa4.lab02spring.utils;

import static ch.usi.inf.bsc.sa4.lab02spring.model.BlockType.RESIDENTIALBLOCK;
import static ch.usi.inf.bsc.sa4.lab02spring.utils.CityGenerator.cityEdge;

import ch.usi.inf.bsc.sa4.lab02spring.configuration.RestrictionsDTODeserializer;
import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.CreateSimulationDTO;
import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.RestrictionsDTO;
import ch.usi.inf.bsc.sa4.lab02spring.model.BlockType;
import ch.usi.inf.bsc.sa4.lab02spring.model.Coordinates;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** Utils used during the creation of the simulation */
public final class CreateSimulationDTOUtils {

  /** EPSILON is used for comparison between doubles */
  private static final Double EPSILON = 1e-10;

  /** regex pattern to validate simulation names */
  private static final Pattern VALID_NAME_PATTERN =
      Pattern.compile("^[\\p{Alnum}\\s]+$", Pattern.UNICODE_CHARACTER_CLASS);

  /**
   * Util method to check whether the passed parameters for the new simulation are valid
   *
   * @param createSimulationDTO the DTO containing the parameters
   * @return true or false
   */
  public static boolean isValidCreateSimulationDTO(CreateSimulationDTO createSimulationDTO) {
    boolean params =
        createSimulationDTO.avgIncome().getValue() > 0.0
            && createSimulationDTO.commuterCost().getValue() > 0.0;
    boolean globalRestrictions =
        createSimulationDTO.globalConstructionCostRestriction().getValue() >= 0.0
            && createSimulationDTO.globalRentCostRestriction().getValue() >= 0.0;
    boolean name =
        createSimulationDTO.name() != null
            && VALID_NAME_PATTERN.matcher(createSimulationDTO.name()).matches();
    return params && globalRestrictions && name;
  }

  public static Integer createsValidSimulation(CreateSimulationDTO createSimulationDTO) {
    final int ERROR = 0;
    final int OK = 1;
    final int LIMITED = 2;
    final int MAX_RADIUS = 20;
    Double wageParam = createSimulationDTO.avgIncome().getValue();
    Double commuterCostParam = createSimulationDTO.commuterCost().getValue();
    final int radius = cityEdge(wageParam, commuterCostParam);
    if (radius < 0) {
      return ERROR;
    } else if (radius > MAX_RADIUS) {
      return LIMITED;
    } else {
      return OK;
    }
  }

  public static Map<Coordinates, Double> getTransportationCostLimits(
      CreateSimulationDTO createSimulationDTO) {
    Map<Coordinates, Double> localTransportCostRestriction = new HashMap<>();

    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(RestrictionsDTO.class, new RestrictionsDTODeserializer())
            .create();

    if (createSimulationDTO.restrictionsJSON() != null) {

      List<RestrictionsDTO> restrictions =
          Arrays.asList(
              gson.fromJson(createSimulationDTO.restrictionsJSON(), RestrictionsDTO[].class));

      for (RestrictionsDTO restriction : restrictions) {
        if (restriction.localLimitTransportationCost() != null
            && Math.abs(restriction.localLimitTransportationCost().getValue()) > EPSILON) {
          localTransportCostRestriction.put(
              restriction.coordinates(), restriction.localLimitTransportationCost().getValue());
        }
      }
    }

    return localTransportCostRestriction;
  }

  public static Map<Coordinates, Double> getConstructionCostLimits(
      CreateSimulationDTO createSimulationDTO) {
    Map<Coordinates, Double> localConstructionCostRestriction = new HashMap<>();

    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(RestrictionsDTO.class, new RestrictionsDTODeserializer())
            .create();

    if (createSimulationDTO.restrictionsJSON() != null) {

      List<RestrictionsDTO> restrictions =
          Arrays.asList(
              gson.fromJson(createSimulationDTO.restrictionsJSON(), RestrictionsDTO[].class));

      for (RestrictionsDTO restriction : restrictions) {
        if (restriction.localLimitConstructionCost() != null
            && Math.abs(restriction.localLimitConstructionCost().getValue()) > EPSILON) {
          localConstructionCostRestriction.put(
              restriction.coordinates(), restriction.localLimitConstructionCost().getValue());
        }
      }
    }

    return localConstructionCostRestriction;
  }

  public static Map<Coordinates, Double> getRentCostLimits(
      CreateSimulationDTO createSimulationDTO) {
    Map<Coordinates, Double> localRentGradientRestriction = new HashMap<>();

    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(RestrictionsDTO.class, new RestrictionsDTODeserializer())
            .create();

    if (createSimulationDTO.restrictionsJSON() != null) {

      List<RestrictionsDTO> restrictions =
          Arrays.asList(
              gson.fromJson(createSimulationDTO.restrictionsJSON(), RestrictionsDTO[].class));

      for (RestrictionsDTO restriction : restrictions) {
        if (restriction.localLimitRentCost() != null
            && Math.abs(restriction.localLimitRentCost().getValue()) > EPSILON) {
          localRentGradientRestriction.put(
              restriction.coordinates(), restriction.localLimitRentCost().getValue());
        }
      }
    }

    return localRentGradientRestriction;
  }

  public static Map<Coordinates, BlockType> getBlockTypeLimits(
      CreateSimulationDTO createSimulationDTO) {
    Map<Coordinates, BlockType> blockTypeRestrictions = new HashMap<>();

    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(RestrictionsDTO.class, new RestrictionsDTODeserializer())
            .create();

    if (createSimulationDTO.restrictionsJSON() != null) {

      List<RestrictionsDTO> restrictions =
          Arrays.asList(
              gson.fromJson(createSimulationDTO.restrictionsJSON(), RestrictionsDTO[].class));

      for (RestrictionsDTO restriction : restrictions) {
        if (restriction.blockTypeRestriction() != null
            && restriction.blockTypeRestriction() != BlockType.NONE) {
          blockTypeRestrictions.put(restriction.coordinates(), restriction.blockTypeRestriction());
        }
      }
    }

    return blockTypeRestrictions;
  }

  /**
   * Filters any restrictions that have coordinates that are outside the bounds
   *
   * @param cityEdge the bound value of the coordinates
   * @param restrictions the map that contains the restrictions
   * @param <T>
   * @return the filtered list
   * @spec.modifies restrictions
   */
  public static <T> Map<Coordinates, T> filterInvalidRestrictions(
      int cityEdge, Map<Coordinates, T> restrictions) {
    return restrictions.entrySet().stream()
        .filter(
            entry ->
                entry.getKey().x() >= -cityEdge
                    && entry.getKey().x() <= cityEdge
                    && entry.getKey().y() >= -cityEdge
                    && entry.getKey().y() <= cityEdge)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  /**
   * Removes the restriction entries that have as blocktype RESIDENTIALBLOCK as the algorithm
   * already takes into account their existence and removes the restrictions on CBD
   *
   * @param blockTypeRestrictions map of restrictions
   * @return filtered map
   * @spec.modifies blockTypeRestrictions
   */
  public static Map<Coordinates, BlockType> eliminateResidentialBlockRestrictions(
      Map<Coordinates, BlockType> blockTypeRestrictions) {
    return blockTypeRestrictions.entrySet().stream()
        .filter(
            entry ->
                entry.getValue() != RESIDENTIALBLOCK
                    && !(entry.getKey().x() == 0 && entry.getKey().y() == 0))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
