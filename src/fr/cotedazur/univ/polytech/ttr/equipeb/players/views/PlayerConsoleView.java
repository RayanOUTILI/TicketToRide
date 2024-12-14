package fr.cotedazur.univ.polytech.ttr.equipeb.players.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;

/**
 * Player console view
 */
public class PlayerConsoleView extends IPlayerViewable {
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
}
