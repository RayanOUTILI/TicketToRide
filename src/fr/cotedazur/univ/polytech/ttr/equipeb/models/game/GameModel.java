package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.DestinationCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.WagonCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.score.ScoreComparator;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelControllable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelStats;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameModel implements
        IPlayerGameModel,
        IRoutesControllerGameModel,
        IVictoryControllerGameModel,
        IWagonCardsControllerGameModel,
        IDestinationCardsControllerGameModel,
        IScoreControllerGameModel,
        IStationControllerGameModel,
        IChooseRouteStationControllerGameModel,
        ICurrentPlayerScoreControllerGameModel,
        IStatsGameModel
{

    private final List<PlayerModel> playerModels;
    private final WagonCardDeck wagonCardDeck;
    private final DestinationCardDeck<DestinationCard> shortDestinationCardDeck;
    private final DestinationCardDeck<DestinationCard> longDestinationCardDeck;
    private final List<Route> routes;
    private final List<Route> removedRoutes;

    public GameModel(List<PlayerModel> playerModels, WagonCardDeck wagonCardDeck, DestinationCardDeck<DestinationCard> shortDestCardDeck, DestinationCardDeck<DestinationCard> longDestCardDeck, List<Route> routes) {
        this.playerModels = playerModels;
        this.wagonCardDeck = wagonCardDeck;
        this.shortDestinationCardDeck = shortDestCardDeck;
        this.longDestinationCardDeck = longDestCardDeck;
        this.routes = routes;
        this.removedRoutes = new ArrayList<>();
    }

    @Override
    public boolean isAllRoutesClaimed() {
        return routes.stream().allMatch(Route::isClaimed);
    }

    @Override
    public boolean shuffleWagonCardDeck() {
        return wagonCardDeck.shuffle();
    }

    @Override
    public boolean isWagonCardDeckEmpty() {
        return wagonCardDeck.isEmpty();
    }

    @Override
    public boolean fillWagonCardDeck() {
        return wagonCardDeck.fillDeck();
    }

    @Override
    public boolean removeCardFromShownCards(WagonCard card) {
        return wagonCardDeck.removeCardFromShownCards(card);
    }

    @Override
    public boolean placeNewWagonCardOnShownCards(WagonCard card) {
        return wagonCardDeck.addCardToShownCards(card);
    }

    @Override
    public boolean placeShownWagonCards(List<WagonCard> wagonCards) {
        return wagonCardDeck.replaceShownCards(wagonCards);
    }
    @Override
    public boolean replaceShownWagonCardsInCaseOfLocomotives(int minimumLocomotives) {
        while (getListOfShownWagonCards().stream().filter(c -> c.getColor() == Color.ANY).count() >= minimumLocomotives) {
            List<WagonCard> newShownCards = drawCardsFromWagonCardDeck(5);
            wagonCardDeck.replaceShownCards(newShownCards);
        }
        return true;
    }

    @Override
    public WagonCard drawCardFromWagonCardDeck() {
        return wagonCardDeck.drawCard();
    }

    @Override
    public List<WagonCard> drawCardsFromWagonCardDeck(int numberOfCards) {
        List<WagonCard> cards = new ArrayList<>();

        for (int i = 0; i < numberOfCards; i++) {
            if(!wagonCardDeck.isEmpty()) cards.add(wagonCardDeck.drawCard());
        }

        return cards;
    }

    @Override
    public List<WagonCard> getListOfShownWagonCards() {
        return wagonCardDeck.shownCards();
    }

    @Override
    public List<RouteReadOnly> getNonControllableRoutes() {
        return new ArrayList<>(routes);
    }

    public List<RouteReadOnly> getNonControllableAvailableRoutes() {
        return routes.stream()
                .filter(r -> !r.isClaimed())
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteReadOnly> getNonControllableAvailableRoutes(int maxLength) {
        return routes.stream()
                .filter(r -> !r.isClaimed() && r.getLength() < maxLength)
                .collect(Collectors.toList());
    }

    @Override
    public List<CityReadOnly> getNonControllableAvailableCities() {
        return routes.stream()
                .filter(r -> !r.isClaimed())
                .flatMap(r -> Stream.of(r.getFirstCity(), r.getSecondCity()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public boolean setAllRoutesNotClaimed() {
        routes.forEach(r -> r.setClaimerPlayer(null));
        return true;
    }

    @Override
    public boolean retrieveDeletedRoutes() {
        boolean added = routes.addAll(removedRoutes);
        removedRoutes.clear();
        return added;
    }

    @Override
    public boolean setAllRoutesIDs() {
        for (int i = 0; i < routes.size(); i++) {
            routes.get(i).setId(i);
        }
        return true;
    }

    @Override
    public Route getRoute(int id) {
        return routes.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Route getDoubleRouteOf(int id) {
        Route route = getRoute(id);
        if (route == null) return null;
        return routes.stream()
                .filter(r ->
                        ((r.getFirstCity().equals(route.getFirstCity()) && r.getSecondCity().equals(route.getSecondCity())) ||
                        (r.getFirstCity().equals(route.getSecondCity()) && r.getSecondCity().equals(route.getFirstCity())))
                        && r.getId() != route.getId()
                )
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean deleteRoute(int id) {
        Route route = getRoute(id);
        if (route == null) return false;
        boolean removed = routes.remove(route);
        if (removed) return removedRoutes.add(route);
        return false;
    }

    @Override
    public int getNbOfPlayers() {
        return playerModels.size();
    }

    @Override
    public boolean discardWagonCards(List<WagonCard> wagonCards) {
        if(wagonCards.isEmpty()) return true;
        return wagonCardDeck.addCardToDiscardPile(wagonCards);
    }

    @Override
    public boolean clearWagonCardsDeck() {
        return wagonCardDeck.clearDeck();
    }

    @Override
    public boolean setAllStationsNotClaimed() {
        getAllCities().forEach(city -> city.setOwner(null));
        return true;
    }

    @Override
    public boolean shuffleDestinationCardsDecks() {
        return shortDestinationCardDeck.shuffle() && longDestinationCardDeck.shuffle();
    }

    @Override
    public boolean isShortDestCardDeckEmpty() {
        return shortDestinationCardDeck.isEmpty();
    }

    @Override
    public boolean isLongDestCardDeckEmpty() {
        return longDestinationCardDeck.isEmpty();
    }

    @Override
    public List<DestinationCard> drawDestinationCards(int maximumCards) {
        return shortDestinationCardDeck.drawCard(maximumCards);
    }

    @Override
    public List<DestinationCard> drawLongDestinationCards(int maximumCards) {
        return longDestinationCardDeck.drawCard(maximumCards);
    }

    @Override
    public void returnShortDestinationCardsToTheBottom(List<DestinationCard> cards) {
        shortDestinationCardDeck.addCardsAtBottom(cards);
    }

    @Override
    public void returnLongDestinationCardsToTheBottom(List<DestinationCard> cards) {
        longDestinationCardDeck.addCardsAtBottom(cards);
    }

    @Override
    public List<RouteReadOnly> getAllRoutesClaimedByPlayer(PlayerIdentification player) {
        return routes.stream()
                .filter(r -> r.isClaimed() && r.getClaimerPlayer().equals(player))
                .collect(Collectors.toList());
    }

    @Override
    public IPlayerModelStats getPlayerWithIdentification(PlayerIdentification playerIdentification) {
        return playerModels.stream()
                .filter(p -> p.getIdentification().equals(playerIdentification))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<PlayerIdentification> getPlayersIdentification() {
        return playerModels.stream()
                .map(IPlayerModelControllable::getIdentification)
                .toList();
    }

    public PlayerModel getWinner() {
        if (playerModels == null || playerModels.isEmpty()) {
            return null;
        }

        return playerModels.stream()
                .min(new ScoreComparator())
                .orElse(null);
    }


    @Override
    public List<City> getAllCities() {
        return routes.stream()
                .flatMap(r -> Stream.of(r.getFirstCity(), r.getSecondCity()))
                .distinct()
                .toList();
    }

    @Override
    public City getCity(int id) {
        return routes.stream()
                .filter(r -> r.getFirstCity().getId() == id || r.getSecondCity().getId() == id)
                .map(Route::getFirstCity)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<City> getCitiesClaimedByPlayer(PlayerIdentification player) {
        return routes.stream()
                .flatMap(r -> Stream.of(r.getFirstCity(), r.getSecondCity()))
                .distinct()
                .filter(c -> player.equals(c.getOwner())).toList();
    }

    @Override
    public List<RouteReadOnly> getNonControllableAdjacentRoutes(CityReadOnly city){
        return routes.stream()
                .filter(r -> r.getFirstCity().equals(city) || r.getSecondCity().equals(city))
                .collect(Collectors.toList());
    }

    @Override
    public List<Route> getAdjacentRoutes(City city) {
        return routes.stream()
                .filter(r -> r.getFirstCity().equals(city) || r.getSecondCity().equals(city))
                .toList();
    }
}
