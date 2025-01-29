package fr.cotedazur.univ.polytech.ttr.equipeb.players.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

import java.util.List;

/**
 * Interface for the player viewable
 */
public abstract class IPlayerViewable {
    private final PlayerIdentification playerIdentification;

    public PlayerIdentification getPlayerIdentification() {
        return playerIdentification;
    }

    protected IPlayerViewable(PlayerIdentification playerIdentification) {
        this.playerIdentification = playerIdentification;
    }

    public abstract void displayReceivedWagonCards(WagonCard... wagonCards);

    public abstract void displayReceivedWagonCards(List<WagonCard> wagonCards);

    public abstract void displayClaimedRoute(RouteReadOnly route);

    public abstract void displayReceivedDestinationCards(List<ShortDestinationCard> destinationCards);

    public abstract void displayNewScore(int score);

    public abstract void displayClaimedStation(CityReadOnly city, List<WagonCard> wagonCards, int stationsLeft);
}
