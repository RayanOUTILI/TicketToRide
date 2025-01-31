package fr.cotedazur.univ.polytech.ttr.equipeb.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

public class ScoreConsoleView implements IScoreView{
    @Override
    public void displayCompletedDestination(PlayerIdentification playerId, DestinationCard destinationCard) {
        System.out.println("Player {" + playerId + "} has a destination between " + destinationCard.getStartCity()
                + " and " + destinationCard.getEndCity()
                + " (" + destinationCard.getPoints() + " points)");
    }

    @Override
    public void displayFailedDestination(PlayerIdentification playerId, DestinationCard destinationCard) {
        System.out.println("Player {" + playerId + "} failed a destination between " + destinationCard.getStartCity()
                + " and " + destinationCard.getEndCity()
                + " (" + -destinationCard.getPoints() + " points)");
    }

    @Override
    public void displayPlayerHasLongestPath(PlayerIdentification playerId, int length) {
        System.out.println("Player {" + playerId + "} has the longest path (" + length + " routes)");
    }

    @Override
    public void displayRemainingStationsScore(PlayerIdentification playerId, int score) {
        System.out.println("Player {" + playerId + "} has " + score + " points from remaining stations");
    }
}
