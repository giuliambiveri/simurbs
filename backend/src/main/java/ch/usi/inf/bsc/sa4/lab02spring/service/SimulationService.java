package ch.usi.inf.bsc.sa4.lab02spring.service;

import static ch.usi.inf.bsc.sa4.lab02spring.utils.CityGenerator.cityEdge;
import static ch.usi.inf.bsc.sa4.lab02spring.utils.CreateSimulationDTOUtils.*;

import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.CreateSimulationDTO;
import ch.usi.inf.bsc.sa4.lab02spring.model.BlockType;
import ch.usi.inf.bsc.sa4.lab02spring.model.City;
import ch.usi.inf.bsc.sa4.lab02spring.model.Coordinates;
import ch.usi.inf.bsc.sa4.lab02spring.model.Simulation;
import ch.usi.inf.bsc.sa4.lab02spring.model.User;
import ch.usi.inf.bsc.sa4.lab02spring.repository.SimulationRepository;
import ch.usi.inf.bsc.sa4.lab02spring.repository.UserRepository;
import ch.usi.inf.bsc.sa4.lab02spring.utils.CityCreationParameters;
import ch.usi.inf.bsc.sa4.lab02spring.utils.OAuth2util;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class SimulationService {

  private final SimulationRepository simulationRepository;
  private final UserRepository userRepository;
  private final CityService cityService;

  @Value("${simurbs.frontend.url}")
  private String frontendUrl;

  public SimulationService(
      SimulationRepository simulationRepository,
      UserRepository userRepository,
      CityService cityService) {
    this.simulationRepository = simulationRepository;
    this.userRepository = userRepository;
    this.cityService = cityService;
  }

  /**
   * persists any change to a city simulation (updating or adding)
   *
   * @param simulation a city simulation
   * @return the updated/added city simulation
   * @spec.modifies the persisted city simulation
   */
  public Simulation storeSimulation(Simulation simulation) {
    return simulationRepository.save(simulation);
  }

  /**
   * Finds the requested simulation, identified by the Id field, in the Database
   *
   * @param id the ID of the simulation
   * @return An Optional, to handle possible missing simulations
   */
  public Optional<Simulation> getSimulationById(String id) {
    return simulationRepository.findById(id);
  }

  /**
   * Creates a new Simulation and persists it in the DB. The new simulation created is by default
   * not an old simulation
   *
   * @param createSimulationDTO the data to create a new Simulation.
   * @param authentication the authentication token of the user adding the new simulation
   * @return the newly created Simulation.
   * @throws NoSuchElementException if the User is not found
   * @spec.requires CreateSimulationDTO != null
   */
  public Simulation createSimulation(
      CreateSimulationDTO createSimulationDTO, OAuth2AuthenticationToken authentication) {
    Optional<User> user = userRepository.findById(OAuth2util.getUserID(authentication));

    final double wageParam = createSimulationDTO.avgIncome().getValue();
    final double commuterCostParam = createSimulationDTO.commuterCost().getValue();
    final double globalConstructionCostRestriction =
        createSimulationDTO.globalConstructionCostRestriction().getValue();
    final double globalRentCostRestriction =
        createSimulationDTO.globalRentCostRestriction().getValue();

    final int cityDistance = cityEdge(wageParam, commuterCostParam);

    Map<Coordinates, Double> parsedTransportationCostLimits =
        getTransportationCostLimits(createSimulationDTO);
    Map<Coordinates, Double> parsedConstructionCostLimits =
        getConstructionCostLimits(createSimulationDTO);
    Map<Coordinates, Double> parsedRentCostLimits = getRentCostLimits(createSimulationDTO);
    Map<Coordinates, BlockType> parsedBlockTypeLimits = getBlockTypeLimits(createSimulationDTO);

    Map<Coordinates, Double> validLocalTransportationCostRestrictions =
        filterInvalidRestrictions(cityDistance, parsedTransportationCostLimits);
    Map<Coordinates, Double> validLocalConstructionCostRestriction =
        filterInvalidRestrictions(cityDistance, parsedConstructionCostLimits);
    Map<Coordinates, Double> validLocalRentGradientRestriction =
        filterInvalidRestrictions(cityDistance, parsedRentCostLimits);
    Map<Coordinates, BlockType> validBlockTypeRestrictions =
        filterInvalidRestrictions(cityDistance, parsedBlockTypeLimits);
    Map<Coordinates, BlockType> filteredBlockTypeRestrictions =
        eliminateResidentialBlockRestrictions(validBlockTypeRestrictions);

    CityCreationParameters params =
        new CityCreationParameters(
            wageParam,
            commuterCostParam,
            globalConstructionCostRestriction,
            globalRentCostRestriction,
            validLocalTransportationCostRestrictions,
            validLocalConstructionCostRestriction,
            validLocalRentGradientRestriction,
            filteredBlockTypeRestrictions,
            createSimulationDTO.avgIncome().getCurrency());

    if (user.isPresent()) {
      Simulation citySim =
          new Simulation(
              createSimulationDTO.avgIncome(),
              createSimulationDTO.commuterCost(),
              createSimulationDTO.name(),
              user.get(),
              ZonedDateTime.now(),
              0,
              null,
              false);
      citySim.setSimulatedCity(cityService.createCity(params));
      return this.storeSimulation(citySim);
    } else {
      throw new NoSuchElementException("User not found");
    }
  }

  /**
   * Finds the list of simulations based on user id.
   *
   * @param userId the id of the user we want to retrieve the simulations of.
   * @return the list of the user's simulations.
   * @spec.requires userId != null
   */
  public List<Simulation> findByUser(String userId) {
    return simulationRepository.findByUser(userId);
  }

  public List<Simulation> findByUserIdAndIsOldIsFalse(String userId) {
    return simulationRepository.findByUserIdAndIsOldIsFalse(userId);
  }

  /**
   * Toggles the privacy of a simulation if the requesting user is the owner.
   *
   * @param simId The MongoDB ID associated to the simulation.
   * @throws IllegalArgumentException if the simulation is not found
   * @spec.requires CreateSimulationDTO != null
   * @spec.modifies the privacy of the simulation
   */
  public void toggleSimulationPrivacy(String simId) {
    Simulation simulation =
        simulationRepository
            .findById(simId)
            .orElseThrow(() -> new IllegalArgumentException("Simulation not found."));
    simulation.setPublic(!simulation.isPublic());
    simulationRepository.save(simulation);
  }

  /**
   * Deletes the requested simulation, identified by the Id field in the Database
   *
   * @param simId the ID of the simulation
   * @return 200 ok if succeed, 404 if the simulation doest exist, 500 if something else fails
   */
  public HttpStatus deleteSimulationById(String simId, boolean oldDeletion) {

    try {
      Simulation simulation =
          simulationRepository.findById(simId).orElseThrow(IllegalArgumentException::new);
      if (oldDeletion && simulation.getOldSimulation() != null) {
        deleteSimulationById(simulation.getOldSimulation().getId(), false);
      }
      if (!cityService.deleteCityById(simulation.getSimulatedCity().getId())) {
        throw new IllegalStateException("City for the simulation was not deleted");
      }
      simulationRepository.deleteById(simId);
      return HttpStatus.OK;
    } catch (IllegalArgumentException e) {
      return HttpStatus.NOT_FOUND;
    } catch (Exception e) {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
  }

  /**
   * Returns public Simulation.
   *
   * @param id id of the Simulation
   * @return the public Simelation
   * @throws NoSuchElementException if the simulation is not found or is not public.
   * @spec.requires id must be valid and not null
   */
  public Simulation getPublicSimulation(String id) {
    return simulationRepository
        .findByIdAndIsPublic(id, true)
        .orElseThrow(() -> new NoSuchElementException("No public simulation found with ID " + id));
  }

  /**
   * Returns list with all public Simulations of the given user id.
   *
   * @param userId the user id
   * @return list with all public Simulations with userId.
   */
  public List<Simulation> getPublicSimulationsByUserId(String userId) {
    return simulationRepository.findByUserIdAndIsPublic(userId, true);
  }

  /**
   * Generate link for the public simulation.
   *
   * @param id id of the simulation
   * @return String containing the link.
   */
  public String generatePublicLink(String id) {
    String baseUrl = frontendUrl + "/simulation/public/";
    return baseUrl + id;
  }

  /**
   * Counts the number of simulations based on user id.
   *
   * @param userId the id of the user we want to retrieve the simulation's number of.
   * @return the number of the user's simulations.
   * @spec.requires userId != null
   */
  public Long countSimulationsByUser(String userId) {
    return simulationRepository.countByUserAndIsOldIsFalse(userId);
  }

  /**
   * Increments the simulation's views counter by 1.
   *
   * @param sim the simulation we need to increment the views of.
   */
  public void incViews(Simulation sim) {
    sim.setViews(sim.getViews() + 1);
    simulationRepository.save(sim);
  }

  /**
   * Calculates the average population size of simulations associated with a user.
   *
   * @param userId The ID of the user for whom to calculate the average population size.
   * @return The average population size of simulations associated with the user.
   * @throws NoSuchElementException if no simulations are found for the user.
   */
  public double averagePopulationSize(String userId) {
    List<Simulation> simulations = simulationRepository.findByUserIdAndIsOldIsFalse(userId);
    if (simulations.isEmpty()) {
      return 0;
    }
    int totalPopulation = 0;
    for (Simulation simulation : simulations) {
      City city = simulation.getSimulatedCity();
      totalPopulation += city.getPopulation();
    }
    return (double) totalPopulation / simulations.size();
  }

  /**
   * Calculates the average income associated with a user.
   *
   * @param userId The ID of the user for whom to calculate the average income.
   * @return The average income of simulations associated with the user.
   * @throws NoSuchElementException if no simulations are found for the user.
   */
  public double averageIncome(String userId) {
    List<Simulation> simulations = simulationRepository.findByUserIdAndIsOldIsFalse(userId);
    if (simulations.isEmpty()) {
      return 0;
    }
    int totalIncome = 0;
    for (Simulation simulation : simulations) {
      totalIncome += simulation.getAverageIncome().getValue();
    }
    return (double) totalIncome / simulations.size();
  }

  /**
   * Calculates the average commuter cost associated with a user.
   *
   * @param userId The ID of the user for whom to calculate the average commuter cost.
   * @return The average commuter cost of simulations associated with the user.
   * @throws NoSuchElementException if no simulations are found for the user.
   */
  public double averageCommuter(String userId) {
    List<Simulation> simulations = simulationRepository.findByUserIdAndIsOldIsFalse(userId);
    if (simulations.isEmpty()) {
      return 0;
    }
    int totalCommuter = 0;
    for (Simulation simulation : simulations) {
      totalCommuter += simulation.getCommuterCosts().getValue();
    }
    return (double) totalCommuter / simulations.size();
  }

  /**
   * Adds the old Simulation reference to the new edited one and creates the new one with the edited
   * parameters
   *
   * @param simulationToEdit The DTO with the parameters for the new simulation
   * @param simulation The actual simulation still not edited
   * @param authentication the authentication token of the user editing the simulation
   * @param isCompared the boolean parameters that says if for the edited simulation the compare
   *     mode exists
   * @param editOld the boolean value that tells us if we are editing the old version of the
   *     simulation
   * @return the edited simulation
   * @spec.modifies the old, now edited simulation
   * @spec.requires simulationToEdit != null, simulation != nulls
   */
  public Simulation editSimulation(
      CreateSimulationDTO simulationToEdit,
      Simulation simulation,
      OAuth2AuthenticationToken authentication,
      boolean isCompared,
      boolean editOld) {
    Simulation editedSimulation = createSimulation(simulationToEdit, authentication);
    if (isCompared) {
      if (!editOld) {
        if (simulation.getOldSimulation() != null) {
          deleteSimulationById(simulation.getOldSimulation().getId(), false);
          simulation.setOldSimulation(null);
        }
        simulation.setOld(true);
        storeSimulation(simulation);
        editedSimulation.setOldSimulation(simulation);
      } else {
        Simulation oldSimulation = simulation.getOldSimulation();
        deleteSimulationById(simulation.getId(), false);
        editedSimulation.setOldSimulation(oldSimulation);
      }
    } else {
      if (simulation.getOldSimulation() != null) {
        deleteSimulationById(simulation.getOldSimulation().getId(), false);
      }
      if (!editOld) {
        deleteSimulationById(simulation.getId(), false);
      } else {
        // delete the current simulation and the old one, we do have just the new one
        deleteSimulationById(simulation.getId(), true);
      }
    }
    return storeSimulation(editedSimulation);
  }
}
