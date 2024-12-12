package fr.cotedazur.univ.polytech.ttr.equipeb;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

public class Main {
    public static void main(String[] args) {

        /*Acceptance Criteria US1
            The deck must contain at least 1 wagon card
            The player must have an inventory of cards
        */
        Player player = new Player();
        player.showHand();
        player.pickCard();
        player.pickCard();
        player.pickCard();
        player.showHand();
    }
}
