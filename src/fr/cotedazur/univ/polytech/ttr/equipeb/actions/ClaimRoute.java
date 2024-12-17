package fr.cotedazur.univ.polytech.ttr.equipeb.actions;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import java.util.List;

/**
 * Claim route action
 */
public record ClaimRoute(RouteReadOnly route, List<WagonCard> wagonCards) {
}