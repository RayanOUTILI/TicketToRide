package fr.cotedazur.univ.polytech.ttr.equipeb.jsonparsers;

import com.fasterxml.jackson.core.type.TypeReference;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.LongDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DestinationCardsParser extends BaseParser<Map<String, List<Map<String, Object>>>> {

    /**
     * Parses all destination cards (short and long) from a JSON file.
     *
     * @param filePath The path to the JSON file.
     * @return A list of all destination cards.
     */
    public List<DestinationCard> parseAllDestinationCards(String filePath) throws JsonParseException {
        Map<String, List<Map<String, Object>>> cardsData = parseJsonFile(filePath, new TypeReference<>() {});
        List<DestinationCard> allCards = new ArrayList<>();

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

        return allCards;
    }

    /**
     * Parses only the short destination cards from a JSON file.
     *
     * @param filePath The path to the JSON file.
     * @return A list of short destination cards.
     */
    public List<ShortDestinationCard> parseShortDestinationCards(String filePath) throws JsonParseException {
        Map<String, List<Map<String, Object>>> cardsData = parseJsonFile(filePath, new TypeReference<>() {});
        List<ShortDestinationCard> shortCards = new ArrayList<>();

        List<Map<String, Object>> shortCardsData = cardsData.get("shortDestinations");

        for (Map<String, Object> cardData : shortCardsData) {
            City start = getOrCreateCity((String) cardData.get("start"));
            City end = getOrCreateCity((String) cardData.get("end"));
            int points = (Integer) cardData.get("points");

            shortCards.add(new ShortDestinationCard(start, end, points));
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
        Map<String, List<Map<String, Object>>> cardsData = parseJsonFile(filePath, new TypeReference<>() {});
        List<LongDestinationCard> longCards = new ArrayList<>();

        List<Map<String, Object>> longCardsData = cardsData.get("longDestinations");

        for (Map<String, Object> cardData : longCardsData) {
            City start = getOrCreateCity((String) cardData.get("start"));
            City end = getOrCreateCity((String) cardData.get("end"));
            int points = (Integer) cardData.get("points");

            longCards.add(new LongDestinationCard(start, end, points));
        }

        return longCards;
    }

}

