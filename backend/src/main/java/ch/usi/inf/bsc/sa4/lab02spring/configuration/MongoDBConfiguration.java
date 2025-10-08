package ch.usi.inf.bsc.sa4.lab02spring.configuration;

import ch.usi.inf.bsc.sa4.lab02spring.model.Coordinates;
import java.time.ZonedDateTime;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

/** This is used as a converter for the hashMap used in the City.class for the city blocks */
@Configuration
class MongoDBConfiguration {

  /**
   * Defines custom MongoDB conversions for handling coordinates. Converts coordinates between
   * application objects and database fields.
   *
   * @return MongoCustomConversions object containing custom converters.
   */
  @Bean
  public MongoCustomConversions mongoCustomConversions() {
    return new MongoCustomConversions(
        Arrays.asList(
            new CoordinatesReadingConverter(),
            new CoordinatesWritingConverter(),
            new CreationDateReadingConverter(),
            new CreationDateWritingConverter()));
  }

  /**
   * used to convert the Coordinates records to strings in order to be able to store them in the DB
   */
  @WritingConverter
  public static class CoordinatesWritingConverter implements Converter<Coordinates, String> {

    /**
     * converts the Coordinates record fields into a string to persist it in the DB
     *
     * @param source the source object to convert, which must be an instance of {@code
     *     Coordinates.class} (never {@code null}.
     * @return the string Version of the Cordinates.class fields
     * @spec.requires source != null
     */
    @Override
    public String convert(final Coordinates source) {
      return source.x() + "," + source.y();
    }
  }

  /**
   * used to convert the coordinate strings from the DB back to a Coordinate record in order to
   * retrieve them from the DB correctly.
   */
  @ReadingConverter
  public static class CoordinatesReadingConverter implements Converter<String, Coordinates> {

    /**
     * converts the stringed coordinates persisted in the DB back to a Coordinate record clas.
     *
     * @param source the source object to convert, which must be an instance of {@code String}
     *     (never {@code null})
     * @return a Coordinate record with the correct fields inside (the one retrieved as a string)
     * @spec.requires source != null
     */
    @Override
    public Coordinates convert(final String source) {
      // here you have a string with "x,y"
      final String[] components = source.split(",");
      final Integer x = Integer.valueOf(components[0]);
      final Integer y = Integer.valueOf(components[1]);
      return new Coordinates(x, y);
    }
  }

  /**
   * used to convert the Coordinates records to strings in order to be able to store them in the DB
   */
  @WritingConverter
  public static class CreationDateWritingConverter implements Converter<ZonedDateTime, String> {

    /**
     * converts the Coordinates record fields into a string to persist it in the DB
     *
     * @param source the source object to convert, which must be an instance of {@code
     *     Coordinates.class} (never {@code null}.
     * @return the string Version of the Cordinates.class fields
     * @spec.requires source != null
     */
    @Override
    public String convert(final ZonedDateTime source) {
      return source.toString();
    }
  }

  /**
   * used to convert the coordinate strings from the DB back to a Coordinate record in order to
   * retrieve them from the DB correctly.
   */
  @ReadingConverter
  public static class CreationDateReadingConverter implements Converter<String, ZonedDateTime> {

    /**
     * converts the stringed coordinates persisted in the DB back to a Coordinate record clas.
     *
     * @param source the source object to convert, which must be an instance of {@code String}
     *     (never {@code null})
     * @return a Coordinate record with the correct fields inside (the one retrieved as a string)
     * @spec.requires source != null
     */
    @Override
    public ZonedDateTime convert(final String source) {
      return ZonedDateTime.parse(source);
    }
  }
}
