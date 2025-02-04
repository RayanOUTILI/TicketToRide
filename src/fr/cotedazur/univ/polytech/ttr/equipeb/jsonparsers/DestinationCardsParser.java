package fr.cotedazur.univ.polytech.ttr.equipeb.jsonparsers;

import com.fasterxml.jackson.core.type.TypeReference;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DestinationCardsParser extends BaseParser<Map<String, List<Map<String, Object>>>> {
    private static final String NAME_SHORT_DESTINATIONS = "shortDestinations";
    private static final String NAME_LONG_DESTINATIONS = "longDestinations";
    private static final String START_FIELD = "start";
    private static final String END_FIELD = "end";
    private static final String POINTS_FIELD = "points";
    /**
     * Parses all destination cards (short and long) from a JSON file.
     *
     * @param filePath The path to the JSON file.
     * @return A list of all destination cards.
     */
    public List<DestinationCard> parseAllDestinationCards(String filePath) throws JsonParseException {
        Map<String, List<Map<String, Object>>> cardsData = parseJsonFile(filePath, new TypeReference<>() {});
        List<DestinationCard> allCards = new ArrayList<>();

        List<Map<String, Object>> shortCardsData = cardsData.get(NAME_SHORT_DESTINATIONS);
        List<Map<String, Object>> longCardsData = cardsData.get(NAME_LONG_DESTINATIONS);

        for (Map<String, Object> cardData : shortCardsData) {
            City start = getOrCreateCity((String) cardData.get(START_FIELD));
            City end = getOrCreateCity((String) cardData.get(END_FIELD));
            int points = (Integer) cardData.get(POINTS_FIELD);

            allCards.add(new DestinationCard(start, end, points));
        }

        for (Map<String, Object> cardData : longCardsData) {
            City start = getOrCreateCity((String) cardData.get(START_FIELD));
            City end = getOrCreateCity((String) cardData.get(END_FIELD));
            int points = (Integer) cardData.get(POINTS_FIELD);

            allCards.add(new DestinationCard(start, end, points));
        }

        return allCards;
    }

    /**
     * Parses only the short destination cards from a JSON file.
     *
     * @param filePath The path to the JSON file.
     * @return A list of short destination cards.
     */
    public List<DestinationCard> parseShortDestinationCards(String filePath) throws JsonParseException {
        Map<String, List<Map<String, Object>>> cardsData = parseJsonFile(filePath, new TypeReference<>() {});
        List<DestinationCard> shortCards = new ArrayList<>();

        List<Map<String, Object>> shortCardsData = cardsData.get(NAME_SHORT_DESTINATIONS);

        for (Map<String, Object> cardData : shortCardsData) {
            City start = getOrCreateCity((String) cardData.get(START_FIELD));
            City end = getOrCreateCity((String) cardData.get(END_FIELD));
            int points = (Integer) cardData.get(POINTS_FIELD);

            shortCards.add(new DestinationCard(start, end, points));
        }

        return shortCards;
    }

    /**
     * Parses only the long destination cards from a JSON file.
     *
     * @param filePath The path to the JSON file.
     * @return A list of long destination cards.
     */
    public List<DestinationCard> parseLongDestinationCards(String filePath) throws JsonParseException {
        Map<String, List<Map<String, Object>>> cardsData = parseJsonFile(filePath, new TypeReference<>() {});
        List<DestinationCard> longCards = new ArrayList<>();

        List<Map<String, Object>> longCardsData = cardsData.get(NAME_LONG_DESTINATIONS);

        for (Map<String, Object> cardData : longCardsData) {
            City start = getOrCreateCity((String) cardData.get(START_FIELD));
            City end = getOrCreateCity((String) cardData.get(END_FIELD));
            int points = (Integer) cardData.get(POINTS_FIELD);

            longCards.add(new DestinationCard(start, end, points));
        }

        return longCards;
    }

}

