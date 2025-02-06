package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ActionDrawWagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IPlayerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * This class defines the MediumBotEngine, an bot for the game "Ticket to Ride" that uses a more strategic approach
 * compared to the EasyBot. It considers available routes, destination cards, and stations to make decisions.
 */
public class MediumBotEngine extends BotEngineWithRandom {

    /**
     * Constructs a MediumBotEngine with a specified player model, game model, and view.
     *
     * @param playerModel the model of the player this bot controls.
     * @param gameModel the model of the game.
     * @param view the view interface used for displaying game actions.
     */
    public MediumBotEngine(IPlayerModel playerModel, IPlayerGameModel gameModel, IPlayerEngineViewable view) {
        super(gameModel, playerModel, view);
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

    ////// Methods to determine the bot's next action //////
    private boolean shouldPickCards() {
        return playerModel.getNumberOfWagonCards() < 10 && !gameModel.isWagonCardDeckEmpty();
    }

    /**
     * Checks if the bot should claim a route.
     *
     * @return true if the bot should claim a route, otherwise false.
     */
    private boolean shouldClaimRoute() {
        return chooseRoute() != null;
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
        return playerModel.getDestinationCards().size() < 3 && !gameModel.isShortDestCardDeckEmpty();
    }

    ////// Methods to determine what the bot asks for //////

    @Override
    protected Optional<ActionDrawWagonCard> chooseActionDrawWagonCard(List<ActionDrawWagonCard> possibleActions) {
        return Optional.of(possibleActions.get(random.nextInt(possibleActions.size())));
    }

    @Override
    protected List<DestinationCard> chooseInitialDestinationCards(List<DestinationCard> cards) {
        int index1 = random.nextInt(cards.size());
        int index2 = random.nextInt(cards.size());
        return List.of(cards.get(index1), cards.get(index2));
    }

    /**
     * Finds the best available route for the bot to claim, based on the routes' priority.
     *
     * @return the best route to claim, or null if no valid route is available.
     */
    @Override
    protected RouteReadOnly chooseRoute() {
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
    @Override
    protected CityReadOnly chooseCityToPlaceStation(List<CityReadOnly> availableCities) {
        return availableCities.get(random.nextInt(availableCities.size()));
    }

    /**
     * Prioritizes destination cards based on their relevance to the routes already claimed by the bot.
     * The bot keeps the most valuable cards.
     *
     * @param cards the list of destination cards to prioritize.
     * @return the list of prioritized destination cards.
     */
    @Override
    protected List<DestinationCard> chooseDestinationCards(List<DestinationCard> cards) {
        return cards.stream()
                .sorted(Comparator.comparingInt(this::evaluateDestinationCardPriority).reversed())
                .limit(2)
                .toList();
    }

    @Override
    protected WagonCard getWantedWagonCard(List<WagonCard> shownCards) {
        return shownCards.get(random.nextInt(shownCards.size()));
    }

    @Override
    protected RouteReadOnly chooseRouteFromCity(List<RouteReadOnly> availableRoutes) {
        int randomIndex = random.nextInt(availableRoutes.size());
        return availableRoutes.get(randomIndex);
    }

    /**
     * Evaluates the priority of a destination card based on its points and relevance to the bot's claimed routes.
     *
     * @param card the destination card to evaluate.
     * @return the priority score for the destination card.
     */
    private int evaluateDestinationCardPriority(DestinationCard card) {
        int priority = card.getPoints();
        for (RouteReadOnly route : gameModel.getAllRoutesClaimedByPlayer(playerModel.getIdentification())) {
            if (card.getCities().contains(route.getFirstCity()) || card.getCities().contains(route.getSecondCity())) {
                priority += 10;
            }
        }

        return priority;
    }
}
