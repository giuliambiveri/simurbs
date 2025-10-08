package ch.usi.inf.bsc.sa4.lab02spring.repository;

import ch.usi.inf.bsc.sa4.lab02spring.model.Coordinates;
import org.springframework.core.convert.converter.Converter;

/** Used to convert the Record Coordinates into strings */
public class CoordinatesConverter implements Converter<Coordinates, String> {

  /**
   * Converts the instance of Coordinates into a string
   *
   * @param source Coordinates instance
   * @return string
   */
  @Override
  public String convert(Coordinates source) {
    return source.toString();
  }
}
