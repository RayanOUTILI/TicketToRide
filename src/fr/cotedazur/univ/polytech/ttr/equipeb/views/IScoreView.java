package fr.cotedazur.univ.polytech.ttr.equipeb.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

public interface IScoreView {
    void displayCompletedDestination(PlayerIdentification playerId, DestinationCard destinationCard);
    void displayFailedDestination(PlayerIdentification playerId, DestinationCard destinationCard);
    void displayPlayerHasLongestPath(PlayerIdentification playerId,int length);
    void displayRemainingStationsScore(PlayerIdentification playerId, int score);
}
