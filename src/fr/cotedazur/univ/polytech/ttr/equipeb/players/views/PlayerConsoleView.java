package fr.cotedazur.univ.polytech.ttr.equipeb.players.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
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

    private void print(String message) {
        System.out.println("Player {" + this.getPlayerIdentification() + "} " + message);
    }

    @Override
    public void displayReceivedWagonCards(WagonCard... wagonCards) {
        StringBuilder sb = new StringBuilder();
        sb.append("Received wagon cards: ");
        for (WagonCard wagonCard : wagonCards) {
            sb.append(wagonCard).append(" ");
        }
        print(sb.toString());
    }

    @Override
    public void displayReceivedWagonCards(List<WagonCard> wagonCards) {
        StringBuilder sb = new StringBuilder();
        sb.append("Received wagon cards: ");
        for (WagonCard wagonCard : wagonCards) {
            sb.append(wagonCard).append(" ");
        }
        print(sb.toString());
    }

    @Override
    public void displayClaimedRoute(RouteReadOnly route)  {
        print("Claimed route: " + route);
    }

    @Override
    public void displayReceivedDestinationCards(List<ShortDestinationCard> destinationCards) {
        StringBuilder sb = new StringBuilder();
        sb.append("Received destination cards: ");
        for (DestinationCard destinationCard : destinationCards) {
            sb.append(destinationCard).append(" ");
        }
        print(sb.toString());
    }

    @Override
    public void displayNewScore(int score) {
        print("New score: " + score);
    }

    @Override
    public void displayClaimedStation(CityReadOnly city, List<WagonCard> wagonCards, int stationsLeft) {
        StringBuilder sb = new StringBuilder();
        sb.append("Claimed station: ").append(city).append(" with wagon cards: ");
        for (WagonCard wagonCard : wagonCards) {
            sb.append(wagonCard).append(" ");
        }
        sb.append("Stations left: ").append(stationsLeft);
        print(sb.toString());
    }

    @Override
    public void displayActionRefused(Action action, ReasonActionRefused reason) {
        System.out.println("Player {" + this.getPlayerIdentification() + "} Action refused: " + action + " -> " + reason);
    }

    @Override
    public void displayActionCompleted(Action action) {
        System.out.println("Player {" + this.getPlayerIdentification() + "} Action completed: " + action);
    }
}
