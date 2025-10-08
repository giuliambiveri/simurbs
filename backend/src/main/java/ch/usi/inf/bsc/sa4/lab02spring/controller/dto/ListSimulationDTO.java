package ch.usi.inf.bsc.sa4.lab02spring.controller.dto;

import ch.usi.inf.bsc.sa4.lab02spring.model.Simulation;
import java.time.ZonedDateTime;

public record ListSimulationDTO(
    String id,
    String name,
    boolean isPublic,
    ZonedDateTime creationDate,
    Integer views,
    boolean isOld,
    Long population,
    Simulation oldSimulation) {
  public ListSimulationDTO(Simulation simulation) {
    this(
        simulation.getId(),
        simulation.getName(),
        simulation.isPublic(),
        simulation.getCreationDate(),
        simulation.getViews(),
        simulation.isOld(),
        simulation.getSimulatedCity().getPopulation(),
        simulation.getOldSimulation());
  }
}
