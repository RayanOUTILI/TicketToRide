package fr.cotedazur.univ.polytech.ttr.equipeb;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
}
