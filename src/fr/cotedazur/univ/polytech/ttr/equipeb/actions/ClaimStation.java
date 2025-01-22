package fr.cotedazur.univ.polytech.ttr.equipeb.actions;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;

import java.util.List;

public class ClaimStation {
    private final CityReadOnly city;
    private final List<WagonCard> wagonCards;

    public ClaimStation(CityReadOnly city, List<WagonCard> wagonCards) {
        this.city = city;
        this.wagonCards = wagonCards;
    }

    public CityReadOnly city() {
        return city;
    }

    public List<WagonCard> wagonCards() {
        return wagonCards;
    }
}
