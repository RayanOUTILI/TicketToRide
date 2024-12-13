package fr.cotedazur.univ.polytech.ttr.equipeb.players;

import fr.cotedazur.univ.polytech.ttr.equipeb.cards.Card;
import fr.cotedazur.univ.polytech.ttr.equipeb.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.NotEnoughCardsException;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.RouteAlreadyClaimedException;
import fr.cotedazur.univ.polytech.ttr.equipeb.map.Route;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Card> hand;
    private List<Route> ownedRoutes;
    private int score;

    public Player() {
        this.hand = new ArrayList<>();
        this.ownedRoutes = new ArrayList<>();
        this.score = 0;
    }

    public void pickCard(Card card) {
        this.hand.add(card);
    }

    /**
     * Claims a route by using the player's wagon cards. The player needs to have
     * enough cards to claim the route.
     * If successful, the corresponding number of wagons will be placed on the route
     * and points will be awarded.
     *
     * @param route The route the player wants to claim.
     * @throws NotEnoughCardsException      if the player does not have enough wagon
     *                                      cards to claim the route.
     * @throws RouteAlreadyClaimedException if the route is already claimed by
     *                                      another player.
     */
    public void claimRoute(Route route) throws NotEnoughCardsException, RouteAlreadyClaimedException {
        if (route.isClaimed()) {
            throw new RouteAlreadyClaimedException("This route has already been claimed by another player.");
        }

        int routeLength = route.getCapacity();
        int wagonCount = countWagonsInHand();

        if (wagonCount < routeLength) {
            throw new NotEnoughCardsException("Not enough Wagon cards to claim this route.");
        }

        for (int i = 0; i < routeLength; i++) {
            removeWagonCard();
        }

        route.addWagons(routeLength);
        ownedRoutes.add(route);
        updateScore(routeLength);
    }

    /**
     * Counts the number of Wagon cards in the player's hand.
     *
     * @return The number of Wagon cards in the player's hand.
     */
    private int countWagonsInHand() {
        int count = 0;
        for (Card card : hand) {
            if (card instanceof WagonCard) {
                count++;
            }
        }
        return count;
    }

    /**
     * Removes a Wagon card from the player's hand. If no Wagon cards are available,
     * an exception is thrown.
     */
    private void removeWagonCard() {
        for (Card card : hand) {
            if (card instanceof WagonCard) {
                hand.remove(card);
                return;
            }
        }
        throw new IllegalStateException("No Wagon cards left to remove.");
    }

    private void updateScore(int routeLength) {
        int points = calculatePoints(routeLength);
        this.score += points;
    }

    private int calculatePoints(int routeLength) {
        switch (routeLength) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 4;
            case 4:
                return 7;
            case 5:
                return 10;
            case 6:
                return 15;
            default:
                return 0;
        }
    }

    public int getScore() {
        return score;
    }

    public void showHand() {
        if (hand.isEmpty()) {
            System.out.println("The hand is empty.");
        } else {
            System.out.println("Player's hand :");
            for (Card card : hand) {
                System.out.println(card.toString());
            }
        }
    }

    public List<Card> getHand() {
        return hand;
    }

    public List<Route> getOwnedRoutes() {
        return ownedRoutes;
    }
}
