package fr.cotedazur.univ.polytech.ttr.equipeb.factories;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.EasyBotEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.MediumBotEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.PlayerConsoleView;

import java.util.List;

public class PlayerFactory {

    /**
     * Crée un bot pour un joueur spécifique.
     * @param playerModel Le modèle du joueur.
     * @param gameModel Le modèle du jeu.
     * @return Un objet Player qui est un bot.
     */
    public Player createEasyBot(PlayerModel playerModel, GameModel gameModel) {
        return new Player(new EasyBotEngine(playerModel, gameModel, new PlayerConsoleView(playerModel.getIdentification())), playerModel);
    }

    /**
     * Crée trois bots (joueurs) à partir des modèles de joueurs fournis.
     * @param playerModels La liste des modèles de joueurs.
     * @param gameModel Le modèle du jeu.
     * @return Une liste de 3 joueurs créés à partir des modèles.
     */
    public List<Player> createThreeEasyBots(List<PlayerModel> playerModels, GameModel gameModel) {
        return playerModels.stream().map(playerModel -> createEasyBot(playerModel, gameModel)).toList();
    }

    public Player createMediumBot(PlayerModel playerModel, GameModel gameModel) {
        return new Player(new MediumBotEngine(playerModel, gameModel, new PlayerConsoleView(playerModel.getIdentification())), playerModel);
    }

    public List<Player> createThreeMediumBots(List<PlayerModel> playerModels, GameModel gameModel) {
        return playerModels.stream().map(playerModel -> createMediumBot(playerModel, gameModel)).toList();
    }

    public List<Player> createTwoEasyOneMediumBots(List<PlayerModel> playerModels, GameModel gameModel) {
        return List.of(
                createEasyBot(playerModels.get(0), gameModel),
                createEasyBot(playerModels.get(1), gameModel),
                createMediumBot(playerModels.get(2), gameModel)
        );
    }

}
