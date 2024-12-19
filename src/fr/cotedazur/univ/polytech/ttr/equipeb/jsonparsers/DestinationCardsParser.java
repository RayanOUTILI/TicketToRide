package fr.cotedazur.univ.polytech.ttr.equipeb.jsonparsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.LongDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parser for reading destination cards from a JSON file.
 * Supports loading short and long destination cards separately.
 */
public class DestinationCardsParser {
    private final ObjectMapper objectMapper;
    private final Map<String, City> cityMap;

    /**
     * Constructor initializing the JSON object mapper and the city map.
     */
    public DestinationCardsParser() {
        this.objectMapper = new ObjectMapper();
        this.cityMap = new HashMap<>();
    }

    /**
     * Parses all destination cards (short and long) from a JSON file.
     *
     * @param filePath The path to the JSON file.
     * @return A list of all destination cards (short and long combined).
     */
    public List<DestinationCard> parseAllDestinationCards(String filePath) throws JsonParseException {
        List<DestinationCard> allCards = new ArrayList<>();

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new JsonParseException("Le fichier JSON est introuvable.");
            }

            Map<String, List<Map<String, Object>>> cardsData = objectMapper.readValue(inputStream, new TypeReference<>() {});
            List<Map<String, Object>> shortCardsData = cardsData.get("shortDestinations");
            List<Map<String, Object>> longCardsData = cardsData.get("longDestinations");

            for (Map<String, Object> cardData : shortCardsData) {
                City start = getOrCreateCity((String) cardData.get("start"));
                City end = getOrCreateCity((String) cardData.get("end"));
                int points = (Integer) cardData.get("points");

                allCards.add(new ShortDestinationCard(start, end, points));
            }

            for (Map<String, Object> cardData : longCardsData) {
                City start = getOrCreateCity((String) cardData.get("start"));
                City end = getOrCreateCity((String) cardData.get("end"));
                int points = (Integer) cardData.get("points");

                allCards.add(new LongDestinationCard(start, end, points));
            }

        } catch (IOException | IllegalArgumentException e) {
            throw new JsonParseException("Erreur lors du parsing des cartes de destination.", e);
        }

        return allCards;
    }

    /**
     * Parses only the short destination cards from a JSON file.
     *
     * @param filePath The path to the JSON file.
     * @return A list of short destination cards.
     */
    public List<ShortDestinationCard> parseShortDestinationCards(String filePath) throws JsonParseException {
        List<ShortDestinationCard> shortCards = new ArrayList<>();

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new JsonParseException("Le fichier JSON est introuvable.");
            }

            Map<String, List<Map<String, Object>>> cardsData = objectMapper.readValue(inputStream, new TypeReference<>() {});

            List<Map<String, Object>> shortCardsData = cardsData.get("shortDestinations");

            for (Map<String, Object> cardData : shortCardsData) {
                City start = getOrCreateCity((String) cardData.get("start"));
                City end = getOrCreateCity((String) cardData.get("end"));
                int points = (Integer) cardData.get("points");

                shortCards.add(new ShortDestinationCard(start, end, points));
            }

        } catch (IOException | IllegalArgumentException e) {
            throw new JsonParseException("Error while parsing short destination cards.", e);
        }

        return shortCards;
    }

    /**
     * Parses only the long destination cards from a JSON file.
     *
     * @param filePath The path to the JSON file.
     * @return A list of long destination cards.
     */
    public List<LongDestinationCard> parseLongDestinationCards(String filePath) throws JsonParseException {
        List<LongDestinationCard> longCards = new ArrayList<>();

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new JsonParseException("File not found.");
            }

            Map<String, List<Map<String, Object>>> cardsData = objectMapper.readValue(inputStream, new TypeReference<>() {});
            List<Map<String, Object>> longCardsData = cardsData.get("longDestinations");

            for (Map<String, Object> cardData : longCardsData) {
                City start = getOrCreateCity((String) cardData.get("start"));
                City end = getOrCreateCity((String) cardData.get("end"));
                int points = (Integer) cardData.get("points");

                longCards.add(new LongDestinationCard(start, end, points));
            }

        } catch (IOException | IllegalArgumentException e) {
            throw new JsonParseException("Error while parsing long destination cards.", e);
        }

        return longCards;
    }

    /**
     * Get the city with the given name or create it if it does not exist.
     * Uses a map to ensure that cities are created only once.
     *
     * @param cityName The name of the city.
     * @return The city.
     */
    private City getOrCreateCity(String cityName) {
        return cityMap.computeIfAbsent(cityName, City::new);
    }
}
