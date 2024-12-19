package fr.cotedazur.univ.polytech.ttr.equipeb.factories;

import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.jsonparsers.DestinationCardsParser;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.LongDestinationCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class for creating destination cards using a JSON parser.
 */
public class DestinationCardsFactory {
    private final DestinationCardsParser parser;

    /**
     * Constructor initializing the factory with a destination cards parser.
     */
    public DestinationCardsFactory() {
        this.parser = new DestinationCardsParser();
    }

    /**
     * Retrieves all destination cards (short and long combined) from the JSON file.
     * 
     * @return A list of all destination cards.
     */
    public List<DestinationCard> getAllDestinationCards() throws JsonParseException {
        return parser.parseAllDestinationCards("data-europe/destination-cards.json");
    }

    /**
     * Retrieves only the short destination cards from the JSON file.
     * 
     * @return A list of short destination cards.
     */
    public List<ShortDestinationCard> getShortDestinationCards() throws JsonParseException {
        return parser.parseShortDestinationCards("data-europe/destination-cards.json");
    }

    /**
     * Retrieves only the long destination cards from the JSON file.
     * 
     * @return A list of long destination cards.
     */
    public List<LongDestinationCard> getLongDestinationCards() throws JsonParseException {
        return parser.parseLongDestinationCards("data-europe/destination-cards.json");
    }
}
