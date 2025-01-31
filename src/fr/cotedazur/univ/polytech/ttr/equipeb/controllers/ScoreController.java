package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IScoreControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.score.CityPair;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelControllable;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IScoreView;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.ScoreConsoleView;

import java.util.*;
import java.util.stream.Collectors;

import static fr.cotedazur.univ.polytech.ttr.equipeb.utils.CitiesGraphUtils.findLengthBetweenAllCityInGraph;
import static fr.cotedazur.univ.polytech.ttr.equipeb.utils.CitiesGraphUtils.getGraphFromRoutes;

public class ScoreController {
    IScoreControllerGameModel gameModel;
    private final IScoreView scoreView;

    public ScoreController( IScoreControllerGameModel gameModel) {
        this.gameModel = gameModel;
        this.scoreView = new ScoreConsoleView();
    }

    public int calculatePlacedRoutesScore(IPlayerModelControllable player) {
        return gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())
                            .stream()
                            .mapToInt(route -> switch (route.getLength()) {
                                case 1 -> 1;
                                case 2 -> 2;
                                case 3 -> 4;
                                case 4 -> 7;
                                case 6 -> 15;
                                case 8 -> 21;
                                default -> 0;
                            })
                            .sum();
    }

    private int calculateDestinationCardsScore(IPlayerModelControllable player) {
        // Its necessary to remove the length of routes that is "claimed" with stations
        // Please note that the station implementation is not yet implemented here
        List<DestinationCard> destinationCards = player.getDestinationCardsHand();
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
                        scoreView.displayCompletedDestination(player.getIdentification(), card);
                        return card.getPoints();
                    } else {
                        scoreView.displayFailedDestination(player.getIdentification(), card);
                        return -card.getPoints();
                    }
                })
                .sum();
    }

    private int calculateRemainingStationsScore(IPlayerModelControllable player) {
        int score = player.getScore() + player.getStationsLeft() * 4;
        scoreView.displayRemainingStationsScore(player.getIdentification(), score);
        return score;
    }

    private List<IPlayerModelControllable> calculatePlayerWithTheLongestContinuousRoute() {
        Map<IPlayerModelControllable, Integer> playerLongestPaths = gameModel.getPlayers().stream()
                .map(player -> {
                    List<RouteReadOnly> claimedRoutes = gameModel.getAllRoutesClaimedByPlayer(player.getIdentification());
                    Set<CityPair> allCityPairs = findLengthBetweenAllCityInGraph(getGraphFromRoutes(claimedRoutes));
                    int longestPath = allCityPairs.stream()
                            .mapToInt(CityPair::getMaxLength)
                            .max()
                            .orElse(0);
                    return new AbstractMap.SimpleEntry<>(player, longestPath);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        int maxLongestPath = Collections.max(playerLongestPaths.values());

        List<IPlayerModelControllable> playersWithLongestPath = playerLongestPaths.entrySet().stream()
                .filter(entry -> entry.getValue() == maxLongestPath)
                .map(Map.Entry::getKey)
                .toList();

        playersWithLongestPath.forEach(
                player -> scoreView.displayPlayerHasLongestPath(player.getIdentification(), maxLongestPath)
        );

        return playersWithLongestPath;
    }

    private int getFinalScore(IPlayerModelControllable player){
        int score = 0;
        score += calculatePlacedRoutesScore(player);
        score += calculateDestinationCardsScore(player);
        score += calculateRemainingStationsScore(player);

        return score;
    }

    public void setFinalScores(){
        HashMap<IPlayerModelControllable, Integer> finalScores = new HashMap<>();
        gameModel.getPlayers().forEach(player -> finalScores.put(player, getFinalScore(player)));
        calculatePlayerWithTheLongestContinuousRoute().forEach(player -> finalScores.put(player, finalScores.get(player) + 10));
        finalScores.forEach(IPlayerModelControllable::setScore);
    }
}
