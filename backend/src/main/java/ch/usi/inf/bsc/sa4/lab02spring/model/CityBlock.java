package ch.usi.inf.bsc.sa4.lab02spring.model;

/** The CityBlock interface . */
public interface CityBlock {

  /**
   * A method to get the blocks type.
   *
   * @return the block type.
   */
  BlockType getBlockType();

  /**
   * gets the restriction applied on the CityBlock
   *
   * @return Blocktype restriction
   */
  BlockType getBlockTypeRestriction();

  /** sets the restriction applied on the CityBlock */
  void setBlockTypeRestriction(BlockType blockType);
}
