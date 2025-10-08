package ch.usi.inf.bsc.sa4.lab02spring.controller.dto;

import ch.usi.inf.bsc.sa4.lab02spring.model.BlockType;
import ch.usi.inf.bsc.sa4.lab02spring.model.Coordinates;
import ch.usi.inf.bsc.sa4.lab02spring.model.Money;

public record RestrictionsDTO(
    Coordinates coordinates,
    Money localLimitTransportationCost,
    Money localLimitConstructionCost,
    Money localLimitRentCost,
    BlockType blockTypeRestriction) {}
