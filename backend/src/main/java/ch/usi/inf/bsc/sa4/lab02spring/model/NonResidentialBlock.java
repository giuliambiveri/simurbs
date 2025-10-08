package ch.usi.inf.bsc.sa4.lab02spring.model;

import java.util.Objects;
import org.springframework.data.annotation.PersistenceCreator;

/** Represent a non-residential block within a city. */
public class NonResidentialBlock implements CityBlock {

  /** type of the block */
  private BlockType type;

  /** restriction on the type of the block */
  private BlockType blockTypeRestriction;

  /**
   * Constructs a non-residential block.
   *
   * @param type the type of the non-residential block.
   * @spec.requires the type parameter should not be a RESIDENTIALBLOCK.
   */
  @PersistenceCreator
  public NonResidentialBlock(BlockType type) {
    this.type = type;
    this.blockTypeRestriction = BlockType.NONE;
  }

  /**
   * Constructor that sets also the blockTypeRestriction
   *
   * @param type block type
   * @param blockTypeRestriction block restriction
   */
  public NonResidentialBlock(BlockType type, BlockType blockTypeRestriction) {
    this.type = type;
    this.blockTypeRestriction = blockTypeRestriction;
  }

  /**
   * constructor for defensive copy
   *
   * @param other original instance
   */
  public NonResidentialBlock(NonResidentialBlock other) {
    this.type = other.type;
    this.blockTypeRestriction = other.blockTypeRestriction;
  }

  /**
   * Returns the type of this block.
   *
   * @return block type.
   */
  @Override
  public BlockType getBlockType() {
    return this.type;
  }

  /**
   * sets the new nonresidential type
   *
   * @param type the new nonresidential type
   */
  public void setType(BlockType type) {
    this.type = type;
  }

  /**
   * Gets the restriction for the block
   *
   * @return restriction
   */
  @Override
  public BlockType getBlockTypeRestriction() {
    return this.blockTypeRestriction;
  }

  /**
   * Sets the restriction on the block
   *
   * @param blockType restriction
   */
  @Override
  public void setBlockTypeRestriction(BlockType blockType) {
    this.blockTypeRestriction = blockType;
  }

  /**
   * Custom equals for NonResidential class
   *
   * @param o NonResidential instance to compare to
   * @return Boolean
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final NonResidentialBlock that = (NonResidentialBlock) o;
    return type == that.type;
  }

  /**
   * hashing method
   *
   * @return hash
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(type);
  }
}
