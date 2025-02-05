package fr.cotedazur.univ.polytech.ttr.equipeb.actions;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;

import java.util.List;

public class ClaimObject<T> {
    private final T claimable;
    private final List<WagonCard> wagonCards;

    public ClaimObject(T claimable, List<WagonCard> wagonCards) {
        this.claimable = claimable;
        this.wagonCards = wagonCards;
    }

    public T getClaimable() {
        return claimable;
    }

    public List<WagonCard> wagonCards() {
        return wagonCards;
    }
}
