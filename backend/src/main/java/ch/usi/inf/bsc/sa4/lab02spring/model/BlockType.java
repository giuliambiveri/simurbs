package ch.usi.inf.bsc.sa4.lab02spring.model;

/**
 * Represents the types of blocks within SimUrbs.
 *
 * <p>Types:
 *
 * <p>PARKBLOCK - non-residential, represent a park block
 *
 * <p>WATERBLOCK - non-residential, represent a water block, e.g. a part of a Lake
 *
 * <p>RESIDENTIALBLOCK - a residential block, where buildings resides
 */
public enum BlockType {
  PARKBLOCK,
  WATERBLOCK,
  RESIDENTIALBLOCK,
  NONE
}
