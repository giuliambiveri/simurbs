package ch.usi.inf.bsc.sa4.lab02spring.serviceTests;

import static ch.usi.inf.bsc.sa4.lab02spring.model.BuildingType.CONDO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.usi.inf.bsc.sa4.lab02spring.model.*;
import ch.usi.inf.bsc.sa4.lab02spring.repository.CityRepository;
import ch.usi.inf.bsc.sa4.lab02spring.service.CityService;
import ch.usi.inf.bsc.sa4.lab02spring.utils.CityCreationParameters;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Testing the creation of a city and the correct store in the DB of it")
public class CityServiceTests {

  @Autowired private final CityService cityService;

  private static final Money nullMoney = new Money(0.0);

  @Autowired
  public CityServiceTests(CityService cityService) {
    this.cityService = cityService;
  }

  @Test
  @DisplayName(
      "Test that the city is persisted correctly in the database and that fields are correct")
  public void testCreateCity() {
    City result = cityService.createCity(new CityCreationParameters(10000.0, 5000.0));
    assertAll(
        () -> assertNotNull(result),
        () -> assertTrue(result.getBlockCounter() >= 0 && result.getBlockCounter() != null),
        () -> assertTrue(result.getPopulation() >= 0 && result.getPopulation() != null),
        () -> assertTrue(result.getSize() >= 0 && result.getSize() != null),
        () -> assertTrue(result.getRadius() >= 0 && result.getRadius() != null),
        () -> assertNotNull(result.getBlocks()),
        () -> assertNotNull(result.getId()));
  }

  @Test
  @DisplayName("correct storing of a city in the DB")
  public void testStoreCity() {
    CityBlock block =
        new ResidentialBlock(
            new Building(50, CONDO, 5000L),
            new Money(1500.0, Currency.CHF),
            nullMoney,
            nullMoney,
            nullMoney,
            nullMoney,
            nullMoney,
            nullMoney);
    HashMap<Coordinates, CityBlock> blockMap = new HashMap<>();

    // Adding elements to the HashMap
    blockMap.put(new Coordinates(1, 0), block);
    blockMap.put(new Coordinates(0, 1), block);

    // create the city to be persisted
    City city = new City(6, 100000L, 130, blockMap);
    City result = cityService.storeCity(city);
    assertAll(
        () -> assertEquals(city.getBlockCounter(), result.getBlockCounter()),
        () -> assertEquals(city.getBlocks(), result.getBlocks()),
        () -> assertEquals(city.getSize(), result.getSize()),
        () -> assertEquals(city.getId(), result.getId()),
        () -> assertEquals(city.getRadius(), result.getRadius()),
        () -> assertEquals(city.getPopulation(), result.getPopulation()),
        () -> assertEquals(city, result));
  }

  @Test
  @DisplayName("deleteCityById should delete the city, identified by the Id")
  public void testDeleteCityById() {

    CityRepository cityRepository = mock(CityRepository.class);
    doNothing().when(cityRepository).deleteById("testID");
    assertTrue(cityService.deleteCityById("testID"));
  }
}
