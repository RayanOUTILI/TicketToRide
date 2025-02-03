package fr.cotedazur.univ.polytech.ttr.equipeb;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import fr.cotedazur.univ.polytech.ttr.equipeb.engine.GameEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.DestinationCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.MapFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.PlayerFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.WagonCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.DestinationCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.WagonCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.PlayerConsoleView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        int verboseLevel = 2;

        //on lit le paramètre --verbose=X si présent
        for (String arg : args) {
            if (arg.startsWith("--verbose=")) {
                try {
                    verboseLevel = Integer.parseInt(arg.split("=")[1]);
                } catch (NumberFormatException e) {
                    logger.info("Niveau de verbosité invalide, utilisation du niveau par défaut (INFO)");
                }
            }
        }

        setLogLevel(verboseLevel);

        try {
            logger.debug("Démarrage de l'application...");

            List<Route> routes = (new MapFactory()).getMapFromJson();
            WagonCardDeck wagonCardDeck = new WagonCardDeck((new WagonCardsFactory()).getWagonCards());
            DestinationCardDeck destinationCardDeck = new DestinationCardDeck((new DestinationCardsFactory()).getShortDestinationCards());

            PlayerFactory playerFactory = new PlayerFactory();

            List<PlayerModel> playerModels = List.of(
                    new PlayerModel(PlayerIdentification.BLUE, PlayerType.EASY_BOT, new PlayerConsoleView(PlayerIdentification.BLUE)),
                    new PlayerModel(PlayerIdentification.RED, PlayerType.EASY_BOT, new PlayerConsoleView(PlayerIdentification.RED)),
                    new PlayerModel(PlayerIdentification.GREEN, PlayerType.MEDIUM_BOT, new PlayerConsoleView(PlayerIdentification.GREEN))
            );

            GameModel gameModel = new GameModel(playerModels, wagonCardDeck, destinationCardDeck, routes);
            List<Player> players = playerFactory.createTwoEasyOneMediumBots(playerModels, gameModel);

            GameEngine gameEngine = new GameEngine(gameModel, players);
            gameEngine.initGame();
            gameEngine.initPlayers();
            gameEngine.startGame();

            logger.debug("Fin du jeu");

        } catch (JsonParseException e) {
            logger.error("Erreur lors de la lecture du JSON: {}", e.getMessage());
        }
    }

    /**
     * Change dynamiquement le niveau de log.
     * @param verboseLevel Niveau de verbosité : 1=ERROR/WARN, 2=INFO, 3=DEBUG.
     */
    public static void setLogLevel(int verboseLevel) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        switch (verboseLevel) {
            case 1 -> loggerContext.getLogger("fr.cotedazur.univ.polytech.ttr.equipeb").setLevel(Level.WARN);
            case 2 -> loggerContext.getLogger("fr.cotedazur.univ.polytech.ttr.equipeb").setLevel(Level.INFO);
            case 3 -> loggerContext.getLogger("fr.cotedazur.univ.polytech.ttr.equipeb").setLevel(Level.DEBUG);
            default -> loggerContext.getLogger("fr.cotedazur.univ.polytech.ttr.equipeb").setLevel(Level.INFO);
        }
    }
}
