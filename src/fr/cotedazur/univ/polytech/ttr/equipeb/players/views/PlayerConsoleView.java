package fr.cotedazur.univ.polytech.ttr.equipeb.players.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;

import java.util.List;

/**
 * Player console view
 */
public class PlayerConsoleView extends IPlayerViewable implements IPlayerEngineViewable {
    public PlayerConsoleView(PlayerIdentification playerIdentification) {
        super(playerIdentification);
    }

    @Override
    public void displayReceivedWagonCards(WagonCard... wagonCards) {
        System.out.println("Player {" + this.getPlayerIdentification() + "} Received wagon cards: ");
        for (WagonCard wagonCard : wagonCards) {
            System.out.println(wagonCard);
        }
    }

    @Override
    public void displayReceivedWagonCards(List<WagonCard> wagonCards) {
        System.out.println("Player {" + this.getPlayerIdentification() + "} Received wagon cards: ");
        for (WagonCard wagonCard : wagonCards) {
            System.out.println(wagonCard);
        }
    }

    @Override
    public void displayReceivedDestinationCards(List<ShortDestinationCard> destinationCards) {
        System.out.println("Player {" + this.getPlayerIdentification() + "} Received destination cards: ");
        for (ShortDestinationCard destinationCard : destinationCards) {
            System.out.println(destinationCard);
        }
    }

    @Override
    public void displayActionRefused(Action action) {
        System.out.println("Player {" + this.getPlayerIdentification() + "} Action refused: " + action);
    }

    @Override
    public void displayActionCompleted(Action action) {
        System.out.println("Player {" + this.getPlayerIdentification() + "} Action completed: " + action);
    }
}
