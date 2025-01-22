package fr.cotedazur.univ.polytech.ttr.equipeb.players.models;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.PlayerConsoleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class representing the player model
 */
public class PlayerModel implements IPlayerModel, IPlayerModelControllable {
    private final PlayerIdentification playerIdentification;
    private final List<WagonCard> wagonCards;
    private final List<DestinationCard> destinationCards;
    private final IPlayerViewable view;
    private int stationsLeft;
    private int score;

    public PlayerModel(PlayerIdentification playerIdentification, IPlayerViewable view) {
        this.playerIdentification = playerIdentification;
        this.wagonCards = new ArrayList<>();
        this.destinationCards = new ArrayList<>();
        this.view = view;
        this.score = 0;
        this.stationsLeft = 0;
    }

    public PlayerIdentification getIdentification() {
        return playerIdentification;
    }

    @Override
    public void receivedWagonCard(WagonCard wagonCard) {
        wagonCards.add(wagonCard);
        if(view != null) this.view.displayReceivedWagonCards(wagonCard);
    }

    @Override
    public void receivedWagonCards(List<WagonCard> wagonCards) {
        this.wagonCards.addAll(wagonCards);
        if(view != null) this.view.displayReceivedWagonCards(wagonCards);
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
    public void replaceRemovedWagonCards(List<WagonCard> wagonCards) {
        this.wagonCards.addAll(wagonCards);
    }

    @Override
    public void notifyClaimedRoute(RouteReadOnly route) {
        if(view != null) this.view.displayClaimedRoute(route);
    }

    @Override
    public void setScore(int score) {
        if(this.score != score) {
            this.score = score;
            if(view != null) this.view.displayNewScore(score);
        }

    }

    @Override
    public int getScore() {
        return this.score;
    }

    public void receivedDestinationCards(List<ShortDestinationCard> destinationCards) {
        this.destinationCards.addAll(destinationCards);
        if(view != null) this.view.displayReceivedDestinationCards(destinationCards);
    }

    @Override
    public List<DestinationCard> getDestinationCardsHand() {
        return destinationCards;
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
        if(view != null) this.view.displayClaimedStation(city, wagonCards, stationsLeft);
    }

    @Override
    public int getNumberOfWagonCards() {
        return wagonCards.size();
    }

    @Override
    public List<DestinationCard> getDestinationCards() {
        return destinationCards;
    }

    @Override
    public List<WagonCard> getWagonCards(int numberOfCards) {
        List<WagonCard> cards = new ArrayList<>();
        for (int i = 0; i < numberOfCards && i < wagonCards.size(); i++) {
            cards.add(wagonCards.get(i));
        }
        return cards;
    }

    //TODO: There is too much logic in this method, it should be refactored
    @Override
    public List<WagonCard> getWagonCardsIncludingAnyColor(int numberOfCards) {
        Map<Color, Long> colorCount = wagonCards.stream()
                .filter(card -> !card.getColor().equals(Color.ANY))
                .collect(Collectors.groupingBy(WagonCard::getColor, Collectors.counting()));

        Color mostFrequentColor = colorCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

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
                    .limit(numberOfCards - selectedCards.size())
                    .toList();
            selectedCards.addAll(anyCards);
        }

        return selectedCards;
    }

    @Override
    public List<WagonCard> getWagonCardsIncludingAnyColor(Color color, int numberOfCards) {
        List<WagonCard> cards = new ArrayList<>();
        for (WagonCard card : wagonCards) {
            if (card.getColor().equals(color) || card.getColor().equals(Color.ANY)) {
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
}
