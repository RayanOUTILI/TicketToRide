package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IScoreControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.score.CityPair;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IScoreView;

import java.util.*;
import java.util.stream.Collectors;

import static fr.cotedazur.univ.polytech.ttr.equipeb.utils.CitiesGraphUtils.findLengthBetweenAllCityInGraph;
import static fr.cotedazur.univ.polytech.ttr.equipeb.utils.CitiesGraphUtils.getGraphFromRoutes;

public class EndGameScoreController extends Controller {
    private static final int LONGEST_PATH_BONUS = 10;
    private final IScoreControllerGameModel gameModel;
    private final Optional<IScoreView> scoreView;
    private final List<PlayerIdentification> playersWithLongestPath = new ArrayList<>();
    private int longestPath = 0;

    public EndGameScoreController(IScoreControllerGameModel gameModel, IScoreView scoreView) {
        this.gameModel = gameModel;
        this.scoreView = Optional.ofNullable(scoreView);
    }

    public EndGameScoreController(IScoreControllerGameModel gameModel) {
        this(gameModel, null);
    }

    @Override
    public boolean initGame() {
        return true;
    }

    @Override
    public boolean initPlayer(Player player) {
        player.setScore(0);
        return true;
    }

    @Override
    public Optional<ReasonActionRefused> doAction(Player player) {
        int finalScore = getFinalScore(player);

        if(this.playersWithLongestPath.isEmpty()) {
            this.playersWithLongestPath.addAll(calculatePlayerWithTheLongestContinuousRoute());
        }

        if(this.playersWithLongestPath.contains(player.getIdentification())) {
            player.setLongestContinuousRouteLength(longestPath);
            finalScore += LONGEST_PATH_BONUS;
        }

        player.setScore(finalScore);

        return Optional.empty();
    }

    @Override
    public boolean resetPlayer(Player player) {
        return player.clearScore();
    }

    @Override
    public boolean resetGame() {
        this.playersWithLongestPath.clear();
        return true;
    }

    private int calculateDestinationCardsScore(Player player) {
        // Its necessary to remove the length of routes that is "claimed" with stations
        // Please note that the station implementation is not yet implemented here
        List<DestinationCard> destinationCards = player.getDestinationCards();
        List<RouteReadOnly> claimedRoutes = gameModel.getAllRoutesClaimedByPlayer(player.getIdentification());
        List<RouteReadOnly> claimedRoutesWithStations = player.getSelectedStationRoutes();

        // Combine all routes
        List<RouteReadOnly> allRoutes = new ArrayList<>();
        allRoutes.addAll(claimedRoutes);
        allRoutes.addAll(claimedRoutesWithStations);

        // Find all pairs of cities with continuous routes
        Map<City, Map<City, Integer>> claimedPlayerRouteGraph = getGraphFromRoutes(allRoutes);
        Set<CityPair> allCityPairs = findLengthBetweenAllCityInGraph(claimedPlayerRouteGraph);

        // Calculate the score based on destination cards
        return destinationCards.stream()
                .mapToInt(card -> {
                    CityPair pair = new CityPair(card.getStartCity(), card.getEndCity());
                    if (allCityPairs.contains(pair)) {
                        scoreView.ifPresent(v -> v.displayCompletedDestination(player.getIdentification(), card));
                        player.setNumberOfCompletedObjectiveCards(1);
                        return card.getPoints();
                    } else {
                        scoreView.ifPresent(v -> v.displayFailedDestination(player.getIdentification(), card));
                        return -card.getPoints();
                    }
                })
                .sum();
    }

    private int calculateRemainingStationsScore(Player player) {
        int score = player.getScore() + player.getStationsLeft() * 4;
        scoreView.ifPresent(v -> v.displayRemainingStationsScore(player.getIdentification(), score));
        return score;
    }

    private List<PlayerIdentification> calculatePlayerWithTheLongestContinuousRoute() {
        Map<PlayerIdentification, Integer> playerLongestPaths = gameModel.getPlayersIdentification().stream()
                .map(player -> {
                    List<RouteReadOnly> claimedRoutes = gameModel.getAllRoutesClaimedByPlayer(player);
                    Set<CityPair> allCityPairs = findLengthBetweenAllCityInGraph(getGraphFromRoutes(claimedRoutes));
                    int longestPath = allCityPairs.stream()
                            .mapToInt(CityPair::getMaxLength)
                            .max()
                            .orElse(0);
                    return new AbstractMap.SimpleEntry<>(player, longestPath);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (playerLongestPaths.isEmpty()) return Collections.emptyList();
        int maxLongestPath = Collections.max(playerLongestPaths.values());

        List<PlayerIdentification> playersWithLongestPath = playerLongestPaths.entrySet().stream()
                .filter(entry -> entry.getValue() == maxLongestPath)
                .map(Map.Entry::getKey)
                .toList();

        this.longestPath = maxLongestPath;

        playersWithLongestPath.forEach(
                player -> scoreView.ifPresent(v -> v.displayPlayerHasLongestPath(player, maxLongestPath))
        );

        return playersWithLongestPath;
    }

    private int getFinalScore(Player player){
        int score = player.getScore();

        score += calculateDestinationCardsScore(player);
        score += calculateRemainingStationsScore(player);

        return score;
    }
}
