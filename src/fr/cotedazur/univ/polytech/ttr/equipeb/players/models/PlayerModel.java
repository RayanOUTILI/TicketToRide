package fr.cotedazur.univ.polytech.ttr.equipeb.players.models;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerViewable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class representing the player model
 */
public class PlayerModel implements IPlayerModel, IPlayerModelControllable {
    private final PlayerIdentification playerIdentification;
    private final PlayerType playerType;
    private final List<WagonCard> wagonCards;
    private final List<DestinationCard> destinationCards;
    private final List<DestinationCard> discardDestinationCards;
    private final Optional<IPlayerViewable> view;
    private final List<RouteReadOnly> chosenRouteStations;
    private int stationsLeft;
    private int score;
    private int numberOfWagons;
    private int longestContinuousRoute;
    private int completedObjectiveCards;

    public PlayerModel(PlayerIdentification playerIdentification, PlayerType playerType, IPlayerViewable view) {
        this.playerIdentification = playerIdentification;
        this.playerType = playerType;
        this.wagonCards = new ArrayList<>();
        this.destinationCards = new ArrayList<>();
        this.discardDestinationCards = new ArrayList<>();
        this.chosenRouteStations = new ArrayList<>();
        this.view = Optional.ofNullable(view);
        this.score = 0;
        this.stationsLeft = 0;
        this.numberOfWagons = 0;
        this.longestContinuousRoute = 0;
        this.completedObjectiveCards = 0;
    }

    public PlayerModel(PlayerIdentification playerIdentification, IPlayerViewable view) {
        this(playerIdentification, PlayerType.EASY_BOT, view);
    }

    public PlayerModel(PlayerIdentification playerIdentification) {
        this(playerIdentification, null);
    }

    public PlayerIdentification getIdentification() {
        return playerIdentification;
    }

    public int getLongestContinuousRouteLength() {
        return longestContinuousRoute;
    }

    public int getNumberOfCompletedObjectiveCards() {
        return completedObjectiveCards;
    }

    @Override
    public void receivedWagonCard(WagonCard wagonCard) {
        wagonCards.add(wagonCard);
        this.view.ifPresent(v -> v.displayReceivedWagonCards(wagonCard));
    }

    @Override
    public void receivedWagonCards(List<WagonCard> wagonCards) {
        this.wagonCards.addAll(wagonCards);
        this.view.ifPresent(v -> v.displayReceivedWagonCards(wagonCards));
    }

    @Override
    public List<WagonCard> removeWagonCards(List<WagonCard> wagonCards) {
        List<WagonCard> removedCards = new ArrayList<>();
        for (WagonCard card : wagonCards) {
            if (!this.wagonCards.remove(card)) {
                return removedCards;
            }
            removedCards.add(card);
        }
        return removedCards;
    }

    @Override
    public List<WagonCard> getWagonCardsHand() {
        return new ArrayList<>(wagonCards);
    }

    @Override
    public void replaceRemovedWagonCards(List<WagonCard> wagonCards) {
        this.wagonCards.addAll(wagonCards);
    }

    @Override
    public void notifyClaimedRoute(RouteReadOnly route) {
        this.view.ifPresent(v -> v.displayClaimedRoute(route));
    }

    @Override
    public void setScore(int score) {
        if(this.score != score) {
            this.score = score;
            this.view.ifPresent(v -> v.displayNewScore(score));
        }

    }

    @Override
    public int getScore() {
        return this.score;
    }

    public void receiveDestinationCards(List<DestinationCard> destinationCards) {
        this.destinationCards.addAll(destinationCards);
        this.view.ifPresent(v -> v.displayReceivedDestinationCards(new ArrayList<>(destinationCards)));
    }

    @Override
    public List<DestinationCard> getDestinationCards() {
        return destinationCards;
    }

    @Override
    public List<DestinationCard> getDiscardDestinationCards() {
        return discardDestinationCards;
    }

    @Override
    public void defineStartingStationsNumber(int size) {
        this.stationsLeft = size;
    }

    @Override
    public int getStationsLeft() {
        return this.stationsLeft;
    }

    @Override
    public void decrementStationsLeft() {
        this.stationsLeft--;
    }

    @Override
    public void notifyClaimedStation(CityReadOnly city, List<WagonCard> wagonCards) {
        this.view.ifPresent(v -> v.displayClaimedStation(city, wagonCards, stationsLeft));
    }

    @Override
    public boolean setNumberOfWagons(int startingWagonCards) {
        this.numberOfWagons = startingWagonCards;
        return true;
    }

    @Override
    public int getNumberOfWagons() {
        return numberOfWagons;
    }

    @Override
    public void removeWagons(int numberOfWagons) {
        this.numberOfWagons -= numberOfWagons;
    }

    @Override
    public void addChosenRouteStation(RouteReadOnly route) {
        this.chosenRouteStations.add(route);
    }

    @Override
    public List<RouteReadOnly> getSelectedStationRoutes() {
        return new ArrayList<>(this.chosenRouteStations);
    }

    @Override
    public boolean clearDestinationCards() {
        this.destinationCards.clear();
        this.discardDestinationCards.clear();
        return true;
    }

    @Override
    public boolean clearChosenRouteStations() {
        this.chosenRouteStations.clear();
        return true;
    }

    @Override
    public boolean clearScore() {
        this.score = 0;
        return true;
    }

    @Override
    public boolean clearStationsLeft() {
        this.stationsLeft = 0;
        return true;
    }

    @Override
    public boolean clearNumberOfWagons() {
        this.numberOfWagons = 0;
        return true;
    }

    @Override
    public void setNumberOfCompletedObjectiveCards(int numberOfCompletedObjectiveCards) {
        this.completedObjectiveCards = numberOfCompletedObjectiveCards;
    }

    @Override
    public void setLongestContinuousRouteLength(int length) {
        this.longestContinuousRoute = length;
    }

    @Override
    public boolean discardDestinationCard(List<DestinationCard> destinationCards) {
        this.discardDestinationCards.addAll(destinationCards);
        return true;
    }

    @Override
    public int getNumberOfWagonCards() {
        return wagonCards.size();
    }

    @Override
    public List<WagonCard> getWagonCards(int numberOfCards) {
        List<WagonCard> cards = new ArrayList<>();
        for (int i = 0; i < numberOfCards && i < wagonCards.size(); i++) {
            cards.add(wagonCards.get(i));
        }
        return cards;
    }

    @Override
    public List<WagonCard> getWagonCardsIncludingAnyColor(int numberOfCards) {
        Color mostFrequentColor = getMostFrequentColor();

        List<WagonCard> selectedCards = new ArrayList<>();
        if (mostFrequentColor != null) {
            selectedCards = wagonCards.stream()
                    .filter(card -> card.getColor().equals(mostFrequentColor))
                    .limit(numberOfCards)
                    .collect(Collectors.toList());
        }

        if (selectedCards.size() < numberOfCards) {
            List<WagonCard> anyCards = wagonCards.stream()
                    .filter(card -> card.getColor().equals(Color.ANY))
                    .limit((long) numberOfCards - selectedCards.size())
                    .toList();
            selectedCards.addAll(anyCards);
        }

        return selectedCards;
    }

    private Color getMostFrequentColor() {
        Map<Color, Long> colorCount = wagonCards.stream()
                .filter(card -> !card.getColor().equals(Color.ANY))
                .collect(Collectors.groupingBy(WagonCard::getColor, Collectors.counting()));

        return colorCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }


    @Override
    public List<WagonCard> getWagonCardsIncludingAnyColor(Color color, int numberOfCards, int numberLocomotives) {
        List<WagonCard> cards = new ArrayList<>();
        if(color == Color.ANY) color = getMostFrequentColor();
        for(WagonCard card : wagonCards) {
            if(card.getColor() == color && numberOfCards > 0) {
                cards.add(card);
                numberOfCards--;
            }
            else if(card.getColor() == Color.ANY && numberLocomotives > 0) {
                cards.add(card);
                numberLocomotives--;
            }
        }

        if(numberOfCards > 0 ) {
            for(WagonCard card : wagonCards) {
                if(card.getColor() == Color.ANY && numberOfCards > 0 && !cards.contains(card)) {
                    cards.add(card);
                    numberOfCards--;
                }
            }
        }

        if(numberLocomotives > 0 || numberOfCards > 0) {
            return new ArrayList<>();
        }
        return cards;
    }

    @Override
    public List<WagonCard> getWagonCardsOfColor(Color color, int numberOfCards) {
        List<WagonCard> cards = new ArrayList<>();
        for (WagonCard card : wagonCards) {
            if (card.getColor() == color) {
                cards.add(card);
                if (cards.size() == numberOfCards) {
                    break;
                }
            }
        }
        return cards;
    }

    @Override
    public int getNumberOfWagonCardsIncludingAnyColor(Color color) {
        return (int) wagonCards.stream().filter(c -> c.getColor().equals(color) || c.getColor().equals(Color.ANY)).count();
    }

    public PlayerIdentification getPlayerIdentification(){
        return this.playerIdentification;
    }

    public PlayerType getPlayerType(){
        return this.playerType;
    }
}
