package fr.cotedazur.univ.polytech.ttr.equipeb.actions;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import java.util.List;

/**
 * Claim route action
 */
public class ClaimRoute {
    private final RouteReadOnly route;
    private final List<WagonCard> wagonCards;

    public ClaimRoute(RouteReadOnly route, List<WagonCard> wagonCards) {
        this.route = route;
        this.wagonCards = wagonCards;
    }

    public RouteReadOnly route() {
        return route;
    }

    public List<WagonCard> wagonCards() {
        return wagonCards;
    }
}