package fr.cotedazur.univ.polytech.ttr.equipeb.factories.data_modelisation;

import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.DestinationCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.MapFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.WagonCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.DestinationCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.WagonCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;

import java.util.List;

public class EuropeDatasFactory extends DataModelisationFactory{

    MapFactory mapFactory;
    WagonCardsFactory wagonCardsFactory;
    DestinationCardsFactory destinationCardsFactory;

    public EuropeDatasFactory(){
        this.mapFactory = new MapFactory();
        this.wagonCardsFactory = new WagonCardsFactory();
        this.destinationCardsFactory = new DestinationCardsFactory();
    }

    @Override
    public List<Route> createRoutes() {
        try {
            return mapFactory.getMapFromJson("data-europe/routes.json");
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected WagonCardDeck createWagonCardDeck() {
        return new WagonCardDeck(this.wagonCardsFactory.getWagonCards());
    }

    @Override
    protected DestinationCardDeck<DestinationCard> createshortDestinationCardDeck() {
        try {
            return new DestinationCardDeck<>(destinationCardsFactory.getShortDestinationCards());
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected DestinationCardDeck<DestinationCard> createlongDestinationCardDeck() {
        try {
            return new DestinationCardDeck<>(destinationCardsFactory.getLongDestinationCards());
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        }
    }
}
