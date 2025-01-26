package fr.cotedazur.univ.polytech.ttr.equipeb.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.endgame.EndGameReasons;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

public interface IGameViewable {
    void displayEndGameReason(PlayerIdentification playerIdentification, int nbOfWagons);
    void displayWinner(PlayerIdentification playerId, int score);
}