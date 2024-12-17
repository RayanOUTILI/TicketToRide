package fr.cotedazur.univ.polytech.ttr.equipeb.players.views;


import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

import java.util.List;

/**
 * Interface for the player viewable
 */
public abstract class IPlayerViewable {
    private PlayerIdentification playerIdentification;

    public PlayerIdentification getPlayerIdentification() {
        return playerIdentification;
    }

    protected IPlayerViewable(PlayerIdentification playerIdentification) {
        this.playerIdentification = playerIdentification;
    }

    public abstract void displayReceivedWagonCards(WagonCard... wagonCards);

    public void displayClaimedRoute(RouteReadOnly route) {
        System.out.println("Player " + playerIdentification + " claimed route " + route);
    }

    public abstract void displayReceivedDestinationCards(List<DestinationCard> destinationCards);
}
