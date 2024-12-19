package fr.cotedazur.univ.polytech.ttr.equipeb.players.models;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.PlayerConsoleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the player model
 */
public class PlayerModel implements IPlayerModel, IPlayerModelControllable {
    private final PlayerIdentification playerIdentification;
    private final List<WagonCard> wagonCards;
    private final List<DestinationCard> destinationCards;
    private final IPlayerViewable view;

    public PlayerModel(PlayerIdentification playerIdentification) {
        this.playerIdentification = playerIdentification;
        this.wagonCards = new ArrayList<>();
        this.destinationCards = new ArrayList<>();
        this.view = new PlayerConsoleView(playerIdentification);
    }

    public PlayerIdentification getIdentification() {
        return playerIdentification;
    }

    @Override
    public void receivedWagonCard(WagonCard wagonCard) {
        wagonCards.add(wagonCard);
        this.view.displayReceivedWagonCards(wagonCard);
    }

    @Override
    public void receivedWagonCards(List<WagonCard> wagonCards) {
        this.wagonCards.addAll(wagonCards);
        this.view.displayReceivedWagonCards(wagonCards);
    }

    @Override
    public List<WagonCard> removeWagonCards(List<WagonCard> wagonCards) {
        List<WagonCard> removedCards = new ArrayList<>();
        for (WagonCard card : wagonCards) {
            if (!this.wagonCards.remove(card)) {
                return removedCards;
            }
            removedCards.add(card);
        }
        return removedCards;
    }

    @Override
    public void replaceRemovedWagonCards(List<WagonCard> wagonCards) {
        this.wagonCards.addAll(wagonCards);
    }

    @Override
    public void notifyClaimedRoute(RouteReadOnly route) {
        this.view.displayClaimedRoute(route);
    }

    @Override
    public void receivedDestinationCards(List<DestinationCard> destinationCards) {
        this.destinationCards.addAll(destinationCards);
        this.view.displayReceivedDestinationCards(destinationCards);
    }

    @Override
    public int getNumberOfWagonCards() {
        return wagonCards.size();
    }

    @Override
    public List<WagonCard> getWagonCards(int numberOfCards) {
        List<WagonCard> cards = new ArrayList<>();
        for (int i = 0; i < numberOfCards && i < wagonCards.size(); i++) {
            cards.add(wagonCards.get(i));
        }
        return cards;
    }
}
