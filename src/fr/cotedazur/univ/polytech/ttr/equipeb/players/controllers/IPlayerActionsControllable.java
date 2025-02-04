package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.*;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;

import java.util.List;
import java.util.Optional;

/**
 * Interface to control the player actions
 */
public interface IPlayerActionsControllable {
    Action askAction();

    ClaimObject<RouteReadOnly> askClaimRoute();

    ClaimObject<CityReadOnly> askClaimStation();

    List<ShortDestinationCard> askDestinationCards(List<ShortDestinationCard> cards);

    void actionRefused(Action action, ReasonActionRefused reason);

    void actionCompleted(Action action);

    List<WagonCard> askWagonCardsForTunnel(int numberOfCards, Color acceptedColor);

    Optional<ActionDrawWagonCard> askDrawWagonCard(List<ActionDrawWagonCard> possibleActions);

    WagonCard askWagonCardFromShownCards();

    RouteReadOnly askChooseRouteStation(CityReadOnly city);
}
