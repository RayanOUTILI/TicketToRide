package fr.cotedazur.univ.polytech.ttr.equipeb.players.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

import java.util.ArrayList;
import java.util.List;

public class CompositePlayerView extends IPlayerViewable{

    List<IPlayerViewable> views;

    public CompositePlayerView(){
        this.views = new ArrayList<>();
    }

    public void addView(IPlayerViewable view){
        this.views.add(view);
    }

    @Override
    public void setPlayerIdentification(PlayerIdentification playerIdentification) {
        views.forEach(v -> v.setPlayerIdentification(playerIdentification));
    }

    @Override
    public void displayReceivedWagonCards(WagonCard... wagonCards) {
        views.forEach(v -> v.displayReceivedWagonCards(wagonCards));
    }

    @Override
    public void displayReceivedWagonCards(List<WagonCard> wagonCards) {
        views.forEach(v -> v.displayReceivedWagonCards(wagonCards));
    }

    @Override
    public void displayClaimedRoute(RouteReadOnly route) {
        views.forEach(v -> v.displayClaimedRoute(route));
    }

    @Override
    public void displayReceivedDestinationCards(List<DestinationCard> destinationCards) {
        views.forEach(v -> v.displayReceivedDestinationCards(destinationCards));
    }

    @Override
    public void displayNewScore(int score) {
        views.forEach(v -> v.displayNewScore(score));
    }

    @Override
    public void displayClaimedStation(CityReadOnly city, List<WagonCard> wagonCards, int stationsLeft) {
        views.forEach(v -> v.displayClaimedStation(city, wagonCards, stationsLeft));
    }
}
