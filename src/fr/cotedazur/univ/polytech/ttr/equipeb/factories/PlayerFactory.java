package fr.cotedazur.univ.polytech.ttr.equipeb.factories;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.EasyBotEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.MediumBotEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;

import java.util.List;

public class PlayerFactory {

    /**
     * Crée un bot pour un joueur spécifique.
     * @param playerModel Le modèle du joueur.
     * @param gameModel Le modèle du jeu.
     * @return Un objet Player qui est un bot.
     */
    public Player createEasyBot(PlayerModel playerModel, GameModel gameModel, IPlayerEngineViewable playerEngineViewable) {
        return new Player(new EasyBotEngine(playerModel, gameModel, playerEngineViewable), playerModel);
    }

    public Player createEasyBotWithoutView(PlayerModel playerModel, GameModel gameModel) {
        return new Player(new EasyBotEngine(playerModel, gameModel), playerModel);
    }

    public List<Player> createThreeEasyBotsWithoutViews(List<PlayerModel> playerModels, GameModel gameModel) {
        return playerModels.stream().map(playerModel -> createEasyBotWithoutView(playerModel, gameModel)).toList();
    }

    /**
     * Crée trois bots (joueurs) à partir des modèles de joueurs fournis.
     * @param playerModels La liste des modèles de joueurs.
     * @param gameModel Le modèle du jeu.
     * @return Une liste de 3 joueurs créés à partir des modèles.
     */
/*    public List<Player> createThreeEasyBots(List<PlayerModel> playerModels, GameModel gameModel, ) {
        return playerModels.stream().map(playerModel -> createEasyBot(playerModel, gameModel)).toList();
    }*/

    public Player createMediumBot(PlayerModel playerModel, GameModel gameModel, IPlayerEngineViewable playerEngineViewable) {
        return new Player(new MediumBotEngine(playerModel, gameModel, playerEngineViewable), playerModel);
    }

/*    public List<Player> createThreeMediumBots(List<PlayerModel> playerModels, GameModel gameModel) {
        return playerModels.stream().map(playerModel -> createMediumBot(playerModel, gameModel)).toList();
    }*/

    public List<Player> createTwoEasyOneMediumBots(List<PlayerModel> playerModels, GameModel gameModel, List<IPlayerEngineViewable> playerEngineViewables) {
        return List.of(
                createEasyBot(playerModels.get(0), gameModel, playerEngineViewables.get(0)),
                createEasyBot(playerModels.get(1), gameModel, playerEngineViewables.get(1)),
                createMediumBot(playerModels.get(2), gameModel, playerEngineViewables.get(2))
        );
    }

    public List<Player> createTwoMediumOneEasyBots(List<PlayerModel> playerModels, GameModel gameModel, List<IPlayerEngineViewable> playerEngineViewables) {
        return List.of(
                createMediumBot(playerModels.get(0), gameModel, playerEngineViewables.get(0)),
                createMediumBot(playerModels.get(1), gameModel, playerEngineViewables.get(1)),
                createEasyBot(playerModels.get(2), gameModel, playerEngineViewables.get(2))
        );
    }

}
