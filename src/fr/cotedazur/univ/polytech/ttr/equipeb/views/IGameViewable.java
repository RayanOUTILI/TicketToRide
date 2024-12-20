package fr.cotedazur.univ.polytech.ttr.equipeb.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.endgame.EndGameReasons;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

public interface IGameViewable {
    void displayEndGameReason(EndGameReasons reason);
    void displayPlayerScore(PlayerIdentification playerId, int score);
    void displayWinner(PlayerIdentification playerId, int score);
}