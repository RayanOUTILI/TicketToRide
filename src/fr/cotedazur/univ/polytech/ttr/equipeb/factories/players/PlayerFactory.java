package fr.cotedazur.univ.polytech.ttr.equipeb.factories.players;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.EasyBotEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.MediumBotEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;

import java.util.ArrayList;
import java.util.List;

public class PlayerFactory {

    public List<Player> createPlayers(List<PlayerType> playersTypes, List<PlayerModel> playerModels, GameModel gameModel, List<IPlayerEngineViewable> playerEngineViewables) {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < playersTypes.size(); i++) {
            players.add(
                    createPlayer(
                        playersTypes.get(i),
                        playerModels.get(i),
                        gameModel,
                        playerEngineViewables.get(i)
                )
            );
        }
        return players;
    }

    private Player createPlayer(PlayerType playerType, PlayerModel playerModel, GameModel gameModel, IPlayerEngineViewable playerEngineViewable){
        return new Player(
                switch (playerType) {
                    case EASY_BOT -> new EasyBotEngine(playerModel, gameModel, playerEngineViewable);
                    case MEDIUM_BOT -> new MediumBotEngine(playerModel, gameModel, playerEngineViewable);
                    case GOD_BOT -> throw new UnsupportedOperationException("God bot is not implemented yet");
                },
                playerModel
        );
    }
}
