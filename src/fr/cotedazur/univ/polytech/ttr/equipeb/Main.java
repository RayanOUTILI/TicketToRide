package fr.cotedazur.univ.polytech.ttr.equipeb;

import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.RouteAlreadyClaimedException;
import fr.cotedazur.univ.polytech.ttr.equipeb.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ObjectiveCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.CardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.NotEnoughCardsException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Ticket to Ride!");

        City city1 = new City("Paris");
        City city2 = new City("Berlin");
        Route route = new Route(city1, city2, 3);

        Player player = new Player();
        System.out.println("A player just joined the game.");

        System.out.println("Player's pick 3 cards...");
        player.pickCard(new WagonCard());
        player.pickCard(new WagonCard());
        player.pickCard(new WagonCard());
        CardDeck deck = new CardDeck();
        deck.addCard(new ObjectiveCard());
        deck.addCard(new ObjectiveCard());
        deck.addCard(new ObjectiveCard());
        player.pickCard(deck.drawCard());
        player.pickCard(deck.drawCard());
        System.out.println("Player's hand : " + player.getHand());

        System.out.println("Player tries to claim a route between"
                + city1.getName() + " and " + city2.getName() + ".");

        try {
            player.claimRoute(route);
            System.out.println("Route claimed with success!");
            System.out.println("Current score : " + player.getScore());
        } catch (NotEnoughCardsException | RouteAlreadyClaimedException e) {
            System.out.println("Impossible to claim the road : " + e.getMessage());
        }

        System.out.println("Player's hand : ");
        player.showHand();
    }
}
