package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.WagonCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;

import java.util.List;

public interface IVictoryControllerGameModel {

    boolean isAllRoutesClaimed();

    boolean isWagonCardDeckEmpty();

    boolean fillWagonCardDeck();

}
