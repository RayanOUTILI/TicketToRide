package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.objectivebot;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ActionDrawWagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IPlayerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.BotEngineControllable;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.utils.CitiesGraphUtils;

import java.util.*;




public class ObjectiveBotEngine extends BotEngineControllable {
    private final Map<DestinationCard, List<RouteReadOnly>> routesForObjective;
    private boolean allObjectivesCompleted;

    public ObjectiveBotEngine(IPlayerGameModel gameModel, IPlayerModel playerModel, IPlayerEngineViewable view) {
        super(gameModel, playerModel, view);
        allObjectivesCompleted = false;
        routesForObjective = new HashMap<>();
    }

    @Override
    protected Optional<ActionDrawWagonCard> chooseActionDrawWagonCard(List<ActionDrawWagonCard> possibleActions) {
        List<WagonCard> neededWagonCards = chooseNeededCards(gameModel.getListOfShownWagonCards());
        if (neededWagonCards.isEmpty()) {
            return Optional.of(ActionDrawWagonCard.DRAW_FROM_DECK);
        }
        return Optional.of(ActionDrawWagonCard.DRAW_FROM_SHOWN_CARDS);
    }

    /**
     * Checks if the bot can claim a given route based on the player's available cards and the route's length.
     *
     * @param route the route to check.
     * @return true if the bot can claim the route, otherwise false.
     */
    private boolean canTakeRoute(RouteReadOnly route) {
        int requiredLength = route.getLength();
        if (route.getType() == RouteType.TUNNEL) {
            requiredLength += 1;
        }
        return !route.isClaimed() &&
                playerModel.getNumberOfWagonCardsIncludingAnyColor(route.getColor()) >= requiredLength
                && playerModel.getNumberOfWagons() >= route.getLength()
                && route.getNbLocomotives() <= playerModel.getNumberOfWagonCardsIncludingAnyColor(Color.ANY);
    }

    @Override
    public Action askAction() {
        updateStateOfRoutes();
        checkRoutesForClaiming();
        checkRoutesForObjectiveCompletion();

        if (shouldClaimRoute()) {
            return Action.CLAIM_ROUTE;
        }
        if (!gameModel.isWagonCardDeckEmpty() && !allObjectivesCompleted) {
            return Action.PICK_WAGON_CARD;
        }
        if (!gameModel.isShortDestCardDeckEmpty() && allObjectivesCompleted) {
            return Action.PICK_DESTINATION_CARDS;
        }
        if (!gameModel.isWagonCardDeckEmpty()) {
            return Action.PICK_WAGON_CARD;
        }
        return Action.STOP;
    }

    @Override
    public boolean reset() {
        routesForObjective.clear();
        allObjectivesCompleted = false;
        return true;
    }

    private void updateStateOfRoutes() {
        for (Map.Entry<DestinationCard, List<RouteReadOnly>> entry : routesForObjective.entrySet()) {
            List<RouteReadOnly> updatedRoutes = new ArrayList<>();
            for (RouteReadOnly route : entry.getValue()) {

                for (RouteReadOnly gameRoute : gameModel.getNonControllableRoutes()) {
                    if (route.getFirstCity().equals(gameRoute.getFirstCity()) && route.getSecondCity().equals(gameRoute.getSecondCity())) {
                        updatedRoutes.add(gameRoute);
                        break;
                    }
                }
            }
            routesForObjective.put(entry.getKey(), updatedRoutes);
        }
    }


    private boolean shouldClaimRoute() {
        return !routesForObjective.isEmpty()
                && chooseRoute() != null;
    }

    private void checkRoutesForClaiming() {
        for (Map.Entry<DestinationCard, List<RouteReadOnly>> entry : routesForObjective.entrySet()) {
            for (RouteReadOnly route : entry.getValue()) {
                if (route.isClaimed()) {

                    if (route.getClaimerPlayer() != playerModel.getIdentification()) {
                        List<RouteReadOnly> allAvailableRoutes = new ArrayList<>(gameModel.getNonControllableAvailableRoutes());
                        List<RouteReadOnly> claimedRoutes = gameModel.getAllRoutesClaimedByPlayer(playerModel.getIdentification());
                        allAvailableRoutes.addAll(claimedRoutes);
                        Map<City, Map<City, Integer>> citiesGraph = CitiesGraphUtils.getGraphFromRoutes(allAvailableRoutes);
                        routesForObjective.put(entry.getKey(),
                                CitiesGraphUtils.findShortestPathBetweenCities(citiesGraph,
                                        allAvailableRoutes,
                                        entry.getKey().getStartCity(),
                                        entry.getKey().getEndCity()));
                        break;
                    } else {
                        List<RouteReadOnly> routes = new ArrayList<>(entry.getValue());
                        routes.remove(route);
                        routesForObjective.put(entry.getKey(), routes);
                    }
                }
            }
        }
    }


    private void checkRoutesForObjectiveCompletion() {
        List<DestinationCard> completedObjectives = new ArrayList<>();

        for (Map.Entry<DestinationCard, List<RouteReadOnly>> entry : routesForObjective.entrySet()) {
            boolean completed = true;
            for (RouteReadOnly route : entry.getValue()) {
                if (!route.isClaimed() || route.getClaimerPlayer() != playerModel.getIdentification()) {
                    completed = false;
                    break;
                }

            }
            if (completed) {
                completedObjectives.add(entry.getKey());
            }
        }

        for (DestinationCard card : completedObjectives) {
            routesForObjective.remove(card);
        }

        if (routesForObjective.isEmpty()) {
            allObjectivesCompleted = true;
        }
    }

    @Override
    protected RouteReadOnly chooseRoute() {
        List<RouteReadOnly> allNeededRoutes = new ArrayList<>();
        for (List<RouteReadOnly> routes : routesForObjective.values()) {
            allNeededRoutes.addAll(routes);
        }
        List<RouteReadOnly> usefulClaimableRoutes = allNeededRoutes.stream().filter(this::canTakeRoute).toList();

        if (usefulClaimableRoutes.isEmpty()) return null;

        return usefulClaimableRoutes.getFirst();
    }

    /**
     * The bot will never place a station.
     */
    @Override
    protected CityReadOnly chooseCityToPlaceStation(List<CityReadOnly> availableCities) {
        return null;
    }

    /**
     * Used to determine which destination cards to keep.
     * The bot will choose the destination card with the shortest path between the cities.
     * If there are multiple destination cards with the same shortest path, the bot will choose two of them.
     *
     * @param cards the list of destination cards available to the bot.
     * @return the list of the destination cards with the shortest path between the cities.
     */
    @Override
    protected List<DestinationCard> chooseDestinationCards(List<DestinationCard> cards) {
        List<RouteReadOnly> allAvailableRoutes = new ArrayList<>(gameModel.getNonControllableAvailableRoutes());
        List<RouteReadOnly> claimedRoutes = gameModel.getAllRoutesClaimedByPlayer(playerModel.getIdentification());
        allAvailableRoutes.addAll(claimedRoutes);

        List<DestinationPath> destinationPaths = new ArrayList<>();
        Map<City, Map<City, Integer>> citiesGraph = CitiesGraphUtils.getGraphFromRoutes(allAvailableRoutes);
        for (DestinationCard card : cards) {
            List<RouteReadOnly> routes = CitiesGraphUtils.findShortestPathBetweenCities(citiesGraph,
                    allAvailableRoutes,
                    card.getStartCity(),
                    card.getEndCity());

            int routeLength = calculateRouteLength(routes, claimedRoutes);

            DestinationPath destinationPath = new DestinationPath(card, routes, routeLength);
            destinationPaths.add(destinationPath);
        }

        List<DestinationPath> destinationPathsSorted = destinationPaths.stream()
                .sorted()
                .toList();

        List<DestinationPath> chosenDestinationPaths = destinationPathsSorted.stream()
                .filter(n -> n.equals(destinationPathsSorted.getFirst()))
                .limit(2)
                .toList();
        List<DestinationCard> chosenCards = chosenDestinationPaths.stream().map(DestinationPath::getDestinationCard).toList();

        for (DestinationPath destinationPath : chosenDestinationPaths) {
            routesForObjective.put(destinationPath.getDestinationCard(), destinationPath.getRoutes());
        }
        return chosenCards;
    }

    private int calculateRouteLength(List<RouteReadOnly> routes, List<RouteReadOnly> claimedRoutes) {
        int length = 0;
        for (RouteReadOnly route : routes) {
            if (!claimedRoutes.contains(route)) {
                length += route.getLength();
            }
        }

        return length;
    }

    @Override
    protected List<DestinationCard> chooseInitialDestinationCards(List<DestinationCard> cards) {
        allObjectivesCompleted = false;
        List<DestinationCard> chosenCards = findDestinationsWithMostCommonCities(cards);
        if (chosenCards.getFirst() == null) {
            List<DestinationCard> mutableCards = new ArrayList<>(cards);
            mutableCards.sort(Comparator.comparingInt(DestinationCard::getPoints));
            chosenCards = mutableCards.reversed().subList(0, Math.min(2, mutableCards.size()));
        }

        List<RouteReadOnly> allAvailableRoutes = gameModel.getNonControllableAvailableRoutes();
        Map<City, Map<City, Integer>> citiesGraph = CitiesGraphUtils.getGraphFromRoutes(allAvailableRoutes);
        for (DestinationCard card : chosenCards) {
            List<RouteReadOnly> routes = CitiesGraphUtils.findShortestPathBetweenCities(citiesGraph, allAvailableRoutes, card.getStartCity(), card.getEndCity());
            routesForObjective.put(card, routes);
        }
        return chosenCards;
    }

    /**
     * The bot will choose the wagon card that is needed to claim the routes for the destination cards.
     *
     * @param shownCards the list of shown wagon cards.
     * @return the wagon card the bot wants.
     */
    @Override
    protected WagonCard getWantedWagonCard(List<WagonCard> shownCards) {
        List<WagonCard> neededCards = chooseNeededCards(shownCards);
        if (neededCards.isEmpty()) {
            return null;
        }
        return neededCards.getFirst();
    }

    private List<WagonCard> chooseNeededCards(List<WagonCard> cards) {
        List<RouteReadOnly> allNeededRoutes = new ArrayList<>();
        for (List<RouteReadOnly> routes : routesForObjective.values()) {
            allNeededRoutes.addAll(routes);
        }
        List<Color> neededColors = allNeededRoutes.stream().map(RouteReadOnly::getColor).toList();
        return cards.stream().filter(card -> neededColors.contains(card.getColor())).toList();
    }

    /**
     * The bot will never choose a route from a city, because he doesn't claim any stations.
     */
    @Override
    protected RouteReadOnly chooseRouteFromCity(List<RouteReadOnly> availableRoutes) {
        return null;
    }

    /**
     * Used to determine which destination cards to keep.
     * @param destinationCards the list of destination cards available to the bot.
     * @return the list of the destination cards with the most common cities between them.
     * If there are no destination cards with common cities, an empty list is returned.
     */
    private List<DestinationCard> findDestinationsWithMostCommonCities(List<DestinationCard> destinationCards){
        int maxCommon = 0;
        List<DestinationCard> bestPair = new ArrayList<>();
        bestPair.add(null);
        bestPair.add(null);

        for (int i = 0; i < destinationCards.size(); i++) {
            for (int j = i + 1; j < destinationCards.size(); j++) {
                int commonCount = countCommonCities(destinationCards.get(i), destinationCards.get(j));
                if (commonCount > maxCommon) {
                    maxCommon = commonCount;
                    bestPair.set(0,destinationCards.get(i));
                    bestPair.set(1,destinationCards.get(j));
                }
            }
        }
        return bestPair;
    }

    private int countCommonCities(DestinationCard dest1, DestinationCard dest2) {
        List<City> attributes1 = new ArrayList<>(dest1.getCities());
        List<City> attributes2 = new ArrayList<>(dest2.getCities());
        attributes1.retainAll(attributes2);
        return attributes1.size();
    }
}