package fr.cotedazur.univ.polytech.ttr.equipeb.jsonparsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.LongDestinationCard;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Parser for reading destination cards from a JSON file.
 * Supports loading short and long destination cards separately.
 */
public class DestinationCardsParser {
    private final ObjectMapper objectMapper;

    /**
     * Constructor initializing the JSON object mapper.
     */
    public DestinationCardsParser() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Parses all destination cards (short and long) from a JSON file.
     *
     * @param filePath The path to the JSON file.
     * @return A list of all destination cards (short and long combined).
     */
    public List<DestinationCard> parseAllDestinationCards(String filePath) {
        List<DestinationCard> allCards = new ArrayList<>();

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            Map<String, List<Map<String, Object>>> cardsData = objectMapper.readValue(inputStream, new TypeReference<>() {});

            List<Map<String, Object>> shortCardsData = cardsData.get("shortDestinations");
            List<Map<String, Object>> longCardsData = cardsData.get("longDestinations");

            for (Map<String, Object> cardData : shortCardsData) {
                String start = (String) cardData.get("start");
                String end = (String) cardData.get("end");
                int points = (Integer) cardData.get("points");

                allCards.add(new ShortDestinationCard(start, end, points));
            }

            for (Map<String, Object> cardData : longCardsData) {
                String start = (String) cardData.get("start");
                String end = (String) cardData.get("end");
                int points = (Integer) cardData.get("points");

                allCards.add(new LongDestinationCard(start, end, points));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return allCards;
    }

    /**
     * Parses only the short destination cards from a JSON file.
     *
     * @param filePath The path to the JSON file.
     * @return A list of short destination cards.
     */
    public List<ShortDestinationCard> parseShortDestinationCards(String filePath) {
        List<ShortDestinationCard> shortCards = new ArrayList<>();

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            Map<String, List<Map<String, Object>>> cardsData = objectMapper.readValue(inputStream, new TypeReference<>() {});

            List<Map<String, Object>> shortCardsData = cardsData.get("shortDestinations");

            for (Map<String, Object> cardData : shortCardsData) {
                String start = (String) cardData.get("start");
                String end = (String) cardData.get("end");
                int points = (Integer) cardData.get("points");

                shortCards.add(new ShortDestinationCard(start, end, points));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return shortCards;
    }

    /**
     * Parses only the long destination cards from a JSON file.
     *
     * @param filePath The path to the JSON file.
     * @return A list of long destination cards.
     */
    public List<LongDestinationCard> parseLongDestinationCards(String filePath) {
        List<LongDestinationCard> longCards = new ArrayList<>();

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            Map<String, List<Map<String, Object>>> cardsData = objectMapper.readValue(inputStream, new TypeReference<>() {});

            List<Map<String, Object>> longCardsData = cardsData.get("longDestinations");

            for (Map<String, Object> cardData : longCardsData) {
                String start = (String) cardData.get("start");
                String end = (String) cardData.get("end");
                int points = (Integer) cardData.get("points");

                longCards.add(new LongDestinationCard(start, end, points));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return longCards;
    }
}
