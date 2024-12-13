package fr.cotedazur.univ.polytech.ttr.equipeb.game_engine;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

public class ScoreController {

    // TODO: Add interface
    private GameModel gameModel;

    public ScoreController(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void calculateScoreForPlayer(Player player) {
        throw new Error("Not implemented");
    }

    public Player checkForWinner(){
        
    }


}
