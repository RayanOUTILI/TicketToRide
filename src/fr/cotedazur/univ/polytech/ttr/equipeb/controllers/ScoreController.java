package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IScoreControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelControllable;

import java.util.List;

public class ScoreController {
    IScoreControllerGameModel gameModel;

    public ScoreController( IScoreControllerGameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void updateScore(IPlayerModelControllable player) {
        int score = gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())
                            .stream()
                            .mapToInt(route -> switch (route.getLength()) {
                                case 1 -> 1;
                                case 2 -> 2;
                                case 3 -> 4;
                                case 4 -> 7;
                                case 6 -> 15;
                                case 8 -> 21;
                                default -> 0;
                            })
                            .sum();
        player.setScore(score);
    }

    public void calculateFinalScores(){
        List<IPlayerModelControllable> players = gameModel.getPlayers();


    }
}
