package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.*;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ActionDrawWagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
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

    List<DestinationCard> askDestinationCards(List<DestinationCard> cards);

    List<DestinationCard> askInitialDestinationCards(List<DestinationCard> cards);

    void actionRefused(Action action, ReasonActionRefused reason);

    void actionCompleted(Action action);

    void actionStop();

    List<WagonCard> askWagonCardsForTunnel(int numberOfCards, Color acceptedColor);

    Optional<ActionDrawWagonCard> askDrawWagonCard(List<ActionDrawWagonCard> possibleActions);

    WagonCard askWagonCardFromShownCards();

    RouteReadOnly askChooseRouteStation(CityReadOnly city);
}
