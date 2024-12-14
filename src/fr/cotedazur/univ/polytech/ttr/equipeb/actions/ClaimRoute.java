package fr.cotedazur.univ.polytech.ttr.equipeb.actions;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.NonControllableRoute;
import java.util.List;

/**
 * Claim route action
 */
public record ClaimRoute(NonControllableRoute route, List<WagonCard> wagonCards) {
}