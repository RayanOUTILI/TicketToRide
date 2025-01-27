package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ActionDrawWagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimStation;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;

import java.util.List;
import java.util.Optional;

/**
 * Interface to control the player actions
 */
public interface IPlayerActionsControllable {
    Action askAction();

    ClaimRoute askClaimRoute();

    ClaimStation askClaimStation();

    List<ShortDestinationCard> askDestinationCards(List<ShortDestinationCard> cards);

    void actionRefused(Action action, ReasonActionRefused reason);

    void actionCompleted(Action action);

    List<WagonCard> askWagonCardsForTunnel(int numberOfCards, Color acceptedColor);

    Optional<ActionDrawWagonCard> askDrawWagonCard(List<ActionDrawWagonCard> possibleActions);

    WagonCard askWagonCardFromShownCards();
}
