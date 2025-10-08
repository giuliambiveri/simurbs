package ch.usi.inf.bsc.sa4.lab02spring.model;

/**
 * Represents an object with coordinates x and y
 *
 * @param x the x-coordinate
 * @param y the y-coordinate
 */
public record Coordinates(Integer x, Integer y) implements Comparable<Coordinates> {
  @Override
  public int compareTo(Coordinates o) {
    int compareX = this.x.compareTo(o.x);
    if (compareX == 0) {
      return this.y.compareTo(o.y);
    } else {
      return compareX;
    }
  }
}
