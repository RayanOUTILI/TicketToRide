package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;

public class GameResult {
    private final PlayerIdentification winner;
    private final PlayerType winnerType;
    /*private final Map<PlayerIdentification, Integer> playerPositions;
    private final Map<PlayerIdentification, Integer> playerScores;
    private final Map<PlayerIdentification, List<WagonCard>> playerWagonCards;
    private final Map<PlayerIdentification, List<ShortDestinationCard>> playerShortDestinationCards;
    private final Map<PlayerIdentification, List<LongDestinationCard>> playerLongDestinationCards;
    private final Map<PlayerIdentification, Integer> playerStations;
    private final Map<PlayerIdentification, List<Route>> playerRoutes;
    private final List<WagonCard> drawPile;*/
    private final int totalTurns;
    private final int numberOfBots;
    //private final int numberOfRoutes;

    public GameResult(PlayerIdentification winner, PlayerType winnerType, int totalTurns,
                      int numberOfBotsint numberOfRoutes) {
        this.winner = winner;
        this.winnerType = winnerType;
        this.totalTurns = totalTurns;
        this.numberOfBots = numberOfBots;
    }

    public PlayerIdentification getWinner() {
        return winner;
    }

    public int getTotalTurns() {
        return totalTurns;
    }

    public int getNumberOfBots() {
        return numberOfBots;
    }
}