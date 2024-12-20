package fr.cotedazur.univ.polytech.ttr.equipeb.jsonparsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/*
 * Base class for JSON parsers.
 */
public abstract class BaseParser<T> {
    protected final ObjectMapper objectMapper;
    protected final Map<String, City> cityMap;

    /**
     * Constructor initializing the JSON object mapper and the city map.
     */
    protected BaseParser() {
        this.objectMapper = new ObjectMapper();
        this.cityMap = new HashMap<>();
    }

    /**
     * Parses a JSON file into an object of the expected type.
     *
     * @param filePath The path to the JSON file.
     * @param typeReference The type reference for JSON deserialization.
     * @return The parsed object.
     */
    protected T parseJsonFile(String filePath, TypeReference<T> typeReference) throws JsonParseException {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new JsonParseException("File not found: " + filePath);
            }
            return objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            throw new JsonParseException("Error while parsing JSON file: " + filePath, e);
        }
    }

    /**
     * Get the city with the given name or create it if it does not exist.
     *
     * @param cityName The name of the city.
     * @return The city.
     */
    protected City getOrCreateCity(String cityName) {
        return cityMap.computeIfAbsent(cityName, City::new);
    }
}
