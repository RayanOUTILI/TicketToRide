package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;

import java.util.List;

public interface IStationControllerGameModel {
    List<City> getAllCities();
    City getCity(int id);
    boolean discardWagonCards(List<WagonCard> wagonCards);
}
