package ch.usi.inf.bsc.sa4.lab02spring.controller;

import static ch.usi.inf.bsc.sa4.lab02spring.utils.CreateSimulationDTOUtils.createsValidSimulation;

import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.CreateSimulationDTO;
import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.ListSimulationDTO;
import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.SimulationDTO;
import ch.usi.inf.bsc.sa4.lab02spring.model.Simulation;
import ch.usi.inf.bsc.sa4.lab02spring.service.SimulationService;
import ch.usi.inf.bsc.sa4.lab02spring.utils.CreateSimulationDTOUtils;
import ch.usi.inf.bsc.sa4.lab02spring.utils.OAuth2util;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** the controller for the city simulation */
@RestController
@RequestMapping("/simulations")
public class SimulationController {

  private final SimulationService simulationService;

  public SimulationController(SimulationService simulationService) {
    this.simulationService = simulationService;
  }

  /**
   * adds a simulation.
   *
   * @param createSimulationDTO the DTO to create a city simulation.
   * @param authentication the authentication token of the user.
   * @return a 201 Created if simulation has been correctly added, a 401 Unauthorized if client has
   *     no token / invalid token.
   * @spec.modifies the list of city simulation in the system.
   */
  @PostMapping("/new")
  public ResponseEntity<SimulationDTO> addSimulation(
      OAuth2AuthenticationToken authentication,
      @RequestBody CreateSimulationDTO createSimulationDTO) {
    int creationStatus = createsValidSimulation(createSimulationDTO);
    if (!(CreateSimulationDTOUtils.isValidCreateSimulationDTO(createSimulationDTO))
        || creationStatus == 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    SimulationDTO simDTO =
        new SimulationDTO(simulationService.createSimulation(createSimulationDTO, authentication));
    if (creationStatus == 1) {
      return ResponseEntity.status(HttpStatus.CREATED).body(simDTO);
    } else {
      return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(simDTO);
    }
  }

  /**
   * Fetches simulations for the authenticated user.
   *
   * @param authentication OAuth2 token to identify the user.
   * @return ResponseEntity with a list of the user's simulations. Returns 200 OK if successful, 401
   *     Unauthorized or 403 Forbidden if authentication fails.
   */
  @GetMapping("/mine")
  public ResponseEntity<List<ListSimulationDTO>> getMySimulations(
      OAuth2AuthenticationToken authentication) {
    String currentUserId = OAuth2util.getUserID(authentication);
    List<ListSimulationDTO> listMySimulationsDTO =
        simulationService.findByUserIdAndIsOldIsFalse(currentUserId).stream()
            .map(ListSimulationDTO::new)
            .toList();
    return ResponseEntity.ok(listMySimulationsDTO);
  }

  /**
   * Gets from the DB the requested simulation, identified by its ID
   *
   * @param authentication Authentication token passed in the header
   * @param id The ID of the simulation
   * @return the Simulation encapsulated in the DTO if it exists, or a not found error
   */
  @GetMapping("/{id}")
  public ResponseEntity<SimulationDTO> getSimulation(
      OAuth2AuthenticationToken authentication, @PathVariable String id) {
    String userId = OAuth2util.getUserID(authentication);
    Optional<Simulation> citySim = simulationService.getSimulationById(id);
    if (citySim.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      Simulation sim = citySim.get();
      if ((sim.getUser().getId()).equals(userId)) {
        return ResponseEntity.ok(new SimulationDTO(sim));
      } else {
        return ResponseEntity.notFound().build();
      }
    }
  }

  /**
   * Saves into the database the edited city simulation and returns it to the user
   *
   * @param id the id of the city simulation to update
   * @param createSimulationDTO the DTO with the updated values
   * @param authentication the authentication token of the user
   * @param isCompared boolean value that tells if the edited simulation has a comparison mode
   * @param editOld boolean value that tells if we are editing the old version of the simulation
   * @return a 200 OK with the updated simulationDTO if the simulation exist in the database, a 404
   *     NOT FOUND otherwise and a 400 BAD_REQUEST if the given simulation has empty fields
   * @spec.modifies the city simulation retrieved from the database
   */
  @PutMapping("/{id}/edit")
  public ResponseEntity<SimulationDTO> updateSimulation(
      OAuth2AuthenticationToken authentication,
      @PathVariable String id,
      @RequestParam(value = "compare", defaultValue = "false") boolean isCompared,
      @RequestParam(value = "editOld", defaultValue = "false") boolean editOld,
      @RequestBody CreateSimulationDTO createSimulationDTO) {

    Optional<Simulation> simulationOptional = simulationService.getSimulationById(id);
    ResponseEntity<SimulationDTO> responseEntity;

    if (simulationOptional.isPresent()) {
      Simulation simulation = simulationOptional.get();
      String userId = OAuth2util.getUserID(authentication);
      int creationStatus = createsValidSimulation(createSimulationDTO);
      HttpStatus status = null;

      if (!simulation.getUser().getId().equals(userId)) {
        status = HttpStatus.NOT_FOUND;
      } else if (!CreateSimulationDTOUtils.isValidCreateSimulationDTO(createSimulationDTO)
          || creationStatus == 0) {
        status = HttpStatus.BAD_REQUEST;
      }
      if (status != null) {
        responseEntity = ResponseEntity.status(status).build();
      } else {
        Simulation storedSimulation =
            simulationService.editSimulation(
                createSimulationDTO, simulation, authentication, isCompared, editOld);
        if (creationStatus == 1) {
          responseEntity = ResponseEntity.ok(new SimulationDTO(storedSimulation));
        } else {
          responseEntity =
              ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                  .body(new SimulationDTO(storedSimulation));
        }
      }
    } else {
      responseEntity = ResponseEntity.notFound().build();
    }
    return responseEntity;
  }

  /**
   * Toggles the privacy of a simulation
   *
   * @param authentication OAuth2AuthenticationToken to verify user's identity
   * @param id the id of the simulation to toggle
   * @return 200 ok containing a SimulationDTO with updated privacy settings if succeed, a 404 if
   *     the simulation doest exist
   * @spec.modifies the privacy of the city simulation toggled
   */
  @PatchMapping("/{id}/togglePrivacy")
  public ResponseEntity<SimulationDTO> toggleSimulation(
      OAuth2AuthenticationToken authentication, @PathVariable String id) {
    Optional<Simulation> simulationOptional = simulationService.getSimulationById(id);
    if (simulationOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    if (!simulationOptional.get().getUser().getId().equals(OAuth2util.getUserID(authentication))) {
      return ResponseEntity.notFound().build();
    }
    Simulation simulation = simulationOptional.get();
    Simulation storedSimulation =
        simulationService.storeSimulation(simulation.setPublic(!simulation.isPublic()));
    return ResponseEntity.ok(new SimulationDTO(storedSimulation));
  }

  /**
   * Deletes a simulation
   *
   * @param authentication OAuth2AuthenticationToken to verify user's identity
   * @param id id of the simulation
   * @return HttpStatus.OK if deletion is successful otherwise not found
   * @spec.modifies deletes the simulation from the database
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteSimulation(
      OAuth2AuthenticationToken authentication, @PathVariable String id) {
    String userId = OAuth2util.getUserID(authentication);
    Optional<Simulation> citySim = simulationService.getSimulationById(id);
    if (citySim.isPresent() && citySim.get().getUser().getId().equals(userId)) {
      boolean cascadeDeletion = true;
      simulationService.deleteSimulationById(id, cascadeDeletion);
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.notFound().build();
  }

  /**
   * Gets a public simulation by its id.
   *
   * @param id id of the simulation
   * @return The public simulation in the DTO
   * @throws NoSuchElementException If the sim ID does not exist or is not public.
   * @spec.requires The id must be a non-null and valid of a public simulation.
   */
  @GetMapping("/public/{id}")
  public ResponseEntity<SimulationDTO> getPublicSimulation(@PathVariable String id) {

    Optional<Simulation> optSimulation = simulationService.getSimulationById(id);
    if (optSimulation.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    if (!optSimulation.get().isPublic()) {
      return ResponseEntity.notFound().build();
    }

    simulationService.incViews(optSimulation.get());
    SimulationDTO simulationDTO = new SimulationDTO(optSimulation.get());

    return ResponseEntity.ok(simulationDTO);
  }

  /**
   * Gets all the public simulations of the given user.
   *
   * @param userId id of the user
   * @return a 200 OK with a list of SimulationDTO objects otherwise a 404 Not Found.
   */
  @GetMapping("/public/all")
  public ResponseEntity<List<SimulationDTO>> getAllPublicSimulations(@RequestParam String userId) {
    List<Simulation> simulations = simulationService.getPublicSimulationsByUserId(userId);
    if (simulations.isEmpty()) {
      return ResponseEntity.ok(Collections.emptyList());
    }

    List<SimulationDTO> simulationDTOs =
        simulations.stream().map(SimulationDTO::new).collect(Collectors.toList());
    return ResponseEntity.ok(simulationDTOs);
  }

  /**
   * Generates public link.
   *
   * @param id id of the simulation
   * @return A map with the public link.
   * @throws NoSuchElementException If the sim ID does not exist or is not public.
   * @spec.requires The id must be a non-null and valid of a public simulation.
   */
  @PatchMapping("/{id}/makeLink")
  public ResponseEntity<?> makePublic(@PathVariable String id) {
    try {
      // actually used to check if it exists, if not it throws a NoSuchElementException
      simulationService.getPublicSimulation(id);
      String publicLink = simulationService.generatePublicLink(id);
      return ResponseEntity.ok(Map.of("publicLink", publicLink));
    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
