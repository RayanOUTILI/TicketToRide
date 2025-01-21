package fr.cotedazur.univ.polytech.ttr.equipeb.actions;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Claim route action
 */
public class ClaimRoute {
    private final RouteReadOnly route;
    private final Set<WagonCard> wagonCards;

    public ClaimRoute(RouteReadOnly route, List<WagonCard> wagonCards) {
        this.route = route;
        this.wagonCards = new HashSet<>(wagonCards);
    }

    public RouteReadOnly route() {
        return route;
    }

    public List<WagonCard> wagonCards() {
        return new ArrayList<>(wagonCards);
    }
}