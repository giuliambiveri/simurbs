package ch.usi.inf.bsc.sa4.lab02spring.configuration;

import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.RestrictionsDTO;
import ch.usi.inf.bsc.sa4.lab02spring.model.BlockType;
import ch.usi.inf.bsc.sa4.lab02spring.model.Coordinates;
import ch.usi.inf.bsc.sa4.lab02spring.model.Money;
import com.nimbusds.jose.shaded.gson.*;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestrictionsDTODeserializer implements JsonDeserializer<RestrictionsDTO> {

  private static final String BLOCK_TYPE_RESTRICTION = "blockTypeRestriction";
  private static final String LOCAL_LIMIT_CONSTRUCTION_COST = "localLimitConstructionCost";
  private static final String LOCAL_LIMIT_RENT_COST = "localLimitRentCost";
  private static final String LOCAL_LIMIT_TRANSPORTATION_COST = "localLimitTransportationCost";

  @Override
  public RestrictionsDTO deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    final JsonObject jsonObject = json.getAsJsonObject();

    final Gson gson = new Gson();

    final String coordinatesString = jsonObject.getAsJsonPrimitive("coordinates").getAsString();
    final String regex = "^Coordinates\\[x=([+-]?\\d+), y=([+-]?\\d+)\\]";

    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(coordinatesString);

    Coordinates coordinates = null;

    if (matcher.find()) {
      coordinates =
          new Coordinates(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
    }

    BlockType blockTypeRestriction = null;

    if (jsonObject.has(BLOCK_TYPE_RESTRICTION)
        && !jsonObject.get(BLOCK_TYPE_RESTRICTION).isJsonNull()) {
      blockTypeRestriction =
          BlockType.valueOf(jsonObject.getAsJsonPrimitive(BLOCK_TYPE_RESTRICTION).getAsString());
    }

    Money localLimitConstructionCost = null;

    if (jsonObject.has(LOCAL_LIMIT_CONSTRUCTION_COST)
        && !jsonObject.get(LOCAL_LIMIT_CONSTRUCTION_COST).isJsonNull()) {
      localLimitConstructionCost =
          gson.fromJson(jsonObject.get(LOCAL_LIMIT_CONSTRUCTION_COST), Money.class);
    }

    Money localLimitRentCost = null;

    if (jsonObject.has(LOCAL_LIMIT_RENT_COST)
        && !jsonObject.get(LOCAL_LIMIT_RENT_COST).isJsonNull()) {
      localLimitRentCost = gson.fromJson(jsonObject.get(LOCAL_LIMIT_RENT_COST), Money.class);
    }

    Money localLimitTransportationCost = null;

    if (jsonObject.has(LOCAL_LIMIT_TRANSPORTATION_COST)
        && !jsonObject.get(LOCAL_LIMIT_TRANSPORTATION_COST).isJsonNull()) {
      localLimitTransportationCost =
          gson.fromJson(jsonObject.get(LOCAL_LIMIT_TRANSPORTATION_COST), Money.class);
    }

    return new RestrictionsDTO(
        coordinates,
        localLimitTransportationCost,
        localLimitConstructionCost,
        localLimitRentCost,
        blockTypeRestriction);
  }
}
