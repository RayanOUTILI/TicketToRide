package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.*;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IPlayerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.utils.RandomGenerator;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * This class defines the MediumBotEngine, an bot for the game "Ticket to Ride" that uses a more strategic approach
 * compared to the EasyBot. It considers available routes, destination cards, and stations to make decisions.
 */
public class MediumBotEngine implements IPlayerActionsControllable {
    private final IPlayerGameModel gameModel;
    private final IPlayerModel playerModel;
    private final Optional<IPlayerEngineViewable> view;
    private final RandomGenerator random;
    /**
     * Constructs a MediumBotEngine with a specified player model, game model, and view.
     *
     * @param playerModel the model of the player this bot controls.
     * @param gameModel the model of the game.
     * @param view the view interface used for displaying game actions.
     */
    public MediumBotEngine(IPlayerModel playerModel, IPlayerGameModel gameModel, IPlayerEngineViewable view) {
        this.gameModel = gameModel;
        this.playerModel = playerModel;
        this.view = view != null ? Optional.of(view) : Optional.empty();
        this.random = new RandomGenerator();
    }

    /**
     * Determines the next action for the bot to take based on game conditions.
     * The bot can decide to claim a route, place a station, pick destination cards, or pick a wagon card.
     *
     * @return the next action for the bot.
     */
    @Override
    public Action askAction() {
        if (shouldPickCards()) {
            return Action.PICK_WAGON_CARD;
        }
        if (shouldClaimRoute()) {
            return Action.CLAIM_ROUTE;
        }
        if (shouldPlaceStation()) {
            return Action.PLACE_STATION;
        }
        if (shouldPickDestinationCards()) {
            return Action.PICK_DESTINATION_CARDS;
        }
        if (gameModel.isWagonCardDeckEmpty() && !gameModel.isShortDestCardDeckEmpty()) {
            return Action.PICK_DESTINATION_CARDS;
        }
        if (!gameModel.isWagonCardDeckEmpty()) {
            return Action.PICK_WAGON_CARD;
        }
        return Action.STOP;
    }

    private boolean shouldPickCards() {
        return playerModel.getNumberOfWagonCards() < 10 && !gameModel.isWagonCardDeckEmpty();
    }

    /**
     * Determines the route the bot should claim, based on the best available option.
     *
     * @return the claim route action, or null if no route is available.
     */
    @Override
    public ClaimObject<RouteReadOnly> askClaimRoute() {
        RouteReadOnly bestRoute = findBestRouteToClaim();
        if (bestRoute == null) {
            return null;
        }
        return new ClaimObject<>(bestRoute, playerModel.getWagonCardsIncludingAnyColor(bestRoute.getColor(), bestRoute.getLength(), 0));
    }

    /**
     * Chooses a city where the bot will place a station from the available cities.
     *
     * @return the claim station action for the chosen city.
     */
    @Override
    public ClaimObject<CityReadOnly> askClaimStation() {
        List<CityReadOnly> availableCities = gameModel.getNonControllableAvailableCities();
        if (availableCities.isEmpty()) {
            return null;
        }
        CityReadOnly bestCity = chooseCityToPlaceStation(availableCities);
        return new ClaimObject<>(bestCity, playerModel.getWagonCardsIncludingAnyColor(3 - (playerModel.getStationsLeft() - 1)));
    }

    /**
     * Determines the destination cards the bot will keep based on their priority.
     *
     * @param cards the list of destination cards available to the bot.
     * @return a list of selected destination cards.
     */
    @Override
    public List<ShortDestinationCard> askDestinationCards(List<ShortDestinationCard> cards) {
        return prioritizeDestinationCards(cards);
    }

    @Override
    public void actionRefused(Action action, ReasonActionRefused reason) {
        view.ifPresent(iPlayerEngineViewable -> iPlayerEngineViewable.displayActionRefused(action, reason));
    }

    @Override
    public void actionCompleted(Action action) {
        view.ifPresent(iPlayerEngineViewable -> iPlayerEngineViewable.displayActionCompleted(action));
    }

    @Override
    public List<WagonCard> askWagonCardsForTunnel(int numberOfCards, Color acceptedColor) {
        return playerModel.getWagonCardsOfColor(acceptedColor, numberOfCards);
    }

    @Override
    public Optional<ActionDrawWagonCard> askDrawWagonCard(List<ActionDrawWagonCard> possibleActions) {
        if (possibleActions == null || possibleActions.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(possibleActions.get(random.nextInt(possibleActions.size())));
    }

    @Override
    public WagonCard askWagonCardFromShownCards() {
        List<WagonCard> shownCards = gameModel.getListOfShownWagonCards();
        return shownCards.get(random.nextInt(shownCards.size()));
    }

    @Override
    public RouteReadOnly askChooseRouteStation(CityReadOnly city) {
        List<RouteReadOnly> availableRoutes = gameModel.getNonControllableAdjacentRoutes(city);
        if(availableRoutes.isEmpty()) return null;
        int randomIndex = random.nextInt(availableRoutes.size());
        return availableRoutes.get(randomIndex);
    }

    /**
     * Checks if the bot should claim a route.
     *
     * @return true if the bot should claim a route, otherwise false.
     */
    private boolean shouldClaimRoute() {
        return findBestRouteToClaim() != null;
    }

    /**
     * Checks if the bot should place a station.
     *
     * @return true if the bot has stations left to place and meets the wagon card requirement, otherwise false.
     */
    private boolean shouldPlaceStation() {
        return playerModel.getStationsLeft() > 0 &&  playerModel.getWagonCardsIncludingAnyColor(3 - (playerModel.getStationsLeft() - 1)).size() == 3 - (playerModel.getStationsLeft() - 1);
    }

    /**
     * Checks if the bot should pick destination cards.
     *
     * @return true if the bot has fewer than 3 destination cards and the destination deck is not empty, otherwise false.
     */
    private boolean shouldPickDestinationCards() {
        return playerModel.getDestinationCardsHand().size() < 3 && !gameModel.isShortDestCardDeckEmpty();
    }

    /**
     * Finds the best available route for the bot to claim, based on the routes' priority.
     *
     * @return the best route to claim, or null if no valid route is available.
     */
    private RouteReadOnly findBestRouteToClaim() {
        List<RouteReadOnly> availableRoutes = gameModel.getNonControllableAvailableRoutes();

        return availableRoutes.stream()
                .filter(this::canTakeRoute)
                .filter(route -> route.getType() == RouteType.TRAIN)
                .max(Comparator.comparingInt(this::evaluateRoutePriority))
                .orElse(null);
    }

    /**
     * Checks if the bot can claim a given route based on the player's available cards and the route's length.
     *
     * @param route the route to check.
     * @return true if the bot can claim the route, otherwise false.
     */
    private boolean canTakeRoute(RouteReadOnly route) {
        return !route.isClaimed() &&
                playerModel.getNumberOfWagonCardsIncludingAnyColor(route.getColor()) >= route.getLength() && playerModel.getNumberOfWagons() >= route.getLength();
    }

    /**
     * Evaluates the priority of a route for the bot to claim. Routes connected to destination cards have a higher priority.
     *
     * @param route the route to evaluate.
     * @return the priority score for the route.
     */
    private int evaluateRoutePriority(RouteReadOnly route) {
        return switch (route.getLength()) {
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 4;
            case 4 -> 7;
            case 6 -> 15;
            case 8 -> 21;
            default -> 0;
        };
    }

    /**
     * Chooses the best city for placing a station from a list of available cities.
     *
     * @param availableCities the list of cities where the bot can place a station.
     * @return the city to place the station in.
     */
    private CityReadOnly chooseCityToPlaceStation(List<CityReadOnly> availableCities) {
        return availableCities.get(random.nextInt(availableCities.size()));
    }

    /**
     * Prioritizes destination cards based on their relevance to the routes already claimed by the bot.
     * The bot keeps the most valuable cards.
     *
     * @param cards the list of destination cards to prioritize.
     * @return the list of prioritized destination cards.
     */
    private List<ShortDestinationCard> prioritizeDestinationCards(List<ShortDestinationCard> cards) {
        return cards.stream()
                .sorted(Comparator.comparingInt(this::evaluateDestinationCardPriority).reversed())
                .limit(2)
                .toList();
    }

    /**
     * Evaluates the priority of a destination card based on its points and relevance to the bot's claimed routes.
     *
     * @param card the destination card to evaluate.
     * @return the priority score for the destination card.
     */
    private int evaluateDestinationCardPriority(ShortDestinationCard card) {
        int priority = card.getPoints();
        for (RouteReadOnly route : gameModel.getAllRoutesClaimedByPlayer(playerModel.getPlayerIdentification())) {
            if (card.getCities().contains(route.getFirstCity()) || card.getCities().contains(route.getSecondCity())) {
                priority += 10;
            }
        }

        return priority;
    }
}
