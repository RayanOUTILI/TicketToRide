package fr.cotedazur.univ.polytech.ttr.equipeb;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.*;
import fr.cotedazur.univ.polytech.ttr.equipeb.engine.GameEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.DestinationCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.MapFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.PlayerFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.WagonCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.DestinationCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.WagonCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.PlayerConsoleView;
import fr.cotedazur.univ.polytech.ttr.equipeb.simulations.GameSimulator;
import fr.cotedazur.univ.polytech.ttr.equipeb.args.CommandLineArgs;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.GameExecutorFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.data_modelisation.EuropeDatasFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.game_actions.EuropeActionsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.players.PlayerFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.views.StatsViewFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.views.ViewFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.views.ViewOptions;
import fr.cotedazur.univ.polytech.ttr.equipeb.simulations.GameExecutor;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.PlayerStatsLine;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.StatsWriter;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.views.GameStatisticsView;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.views.PlayerStatisticsView;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.GameConsoleView;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IGameViewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Main {

    private static final String FILE_PATH = "stats/gamestats.csv";

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        CommandLineArgs commandLineArgs = CommandLineArgs.parse(args);

        StatsWriter statsWriter;

        // If the user wants to output the results in a CSV file, we create a StatsWriter
        // If not, we set it to null to avoid creating or editing the CSV file
        if (commandLineArgs.getViewOptions().contains(ViewOptions.CSV)) {
            statsWriter = new StatsWriter(FILE_PATH, PlayerStatsLine.headers, true);
        } else {
            statsWriter = null;
        }

        commandLineArgs.getPlayersTypesToPlay().forEach(gameExecutionInfos -> {
            GameExecutor game = GameExecutorFactory.createGame(
                    new EuropeDatasFactory(),
                    new EuropeActionsFactory(),
                    new PlayerFactory(),
                    new ViewFactory(new StatsViewFactory(statsWriter, gameExecutionInfos.getLabel())),
                    gameExecutionInfos.getPlayersType(),
                    commandLineArgs.getViewOptions()
            );

            game.execute(gameExecutionInfos.getExecutionNumber());
        });

        if (statsWriter != null) statsWriter.close();
    }

    /**
     * Classe pour gérer les arguments en ligne de commande avec JCommander.
     */
    private static class CLIArgs {
        @Parameter(names = "--verbose", description = "Niveau de verbosité : 1=ERROR/WARN, 2=INFO, 3=DEBUG", required = false)
        private int verbose = 2;

        @Parameter(description = "Main parameter")
        private List<String> main = new ArrayList<>();
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
