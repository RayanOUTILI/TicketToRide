package fr.cotedazur.univ.polytech.ttr.equipeb.args;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.views.ViewOptions;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.simulations.GameExecutionInfos;

import java.util.ArrayList;
import java.util.List;

public class CommandLineArgs {

    @Parameter(names = {"--csv"}, description = "Output the results in a CSV file")
    private boolean csv;

    @Parameter(names = {"--2thousands"}, description = "Play 2*1000 games, 1000 - Best bot vs Best bot, 1000 - Best bot vs Others")
    private boolean twothousands;

    @Parameter(names = {"--demo"}, description = "Run the demo")
    private boolean demo;

    public static CommandLineArgs parse(String[] args) {
        CommandLineArgs commandLineArgs = new CommandLineArgs();
        JCommander.newBuilder()
                .addObject(commandLineArgs)
                .build()
                .parse(args);
        commandLineArgs.validate();

        return commandLineArgs;
    }

    private void validate() {
        if (!csv && !twothousands && !demo) {
            throw new ParameterException("At least one of --csv, --2thousands or --demo must be specified.");
        }

        if (demo && twothousands) {
            throw new ParameterException("Cannot specify both --demo and --2thousands.");
        }
    }


    public List<ViewOptions> getViewOptions() {
        List<ViewOptions> viewOptions = new ArrayList<>();

        if (csv) viewOptions.add(ViewOptions.CSV);
        if (twothousands) viewOptions.add(ViewOptions.CLI_STATS);
        if (demo) viewOptions.add(ViewOptions.CLI_VERBOSE);

        return viewOptions;
    }

    public List<GameExecutionInfos> getPlayersTypesToPlay(){
        List<GameExecutionInfos> playersTypes = new ArrayList<>();
        if (twothousands) {
            playersTypes.add(new GameExecutionInfos(
                    List.of(PlayerType.OBJECTIVE_BOT, PlayerType.MEDIUM_BOT, PlayerType.EASY_BOT),
                    1000
            ));

            playersTypes.add(new GameExecutionInfos(
                    List.of(PlayerType.MEDIUM_BOT, PlayerType.MEDIUM_BOT, PlayerType.MEDIUM_BOT),
                    1000
            ));
        }

        if (demo) {
            playersTypes.add(new GameExecutionInfos(
                    List.of(PlayerType.OBJECTIVE_BOT, PlayerType.MEDIUM_BOT, PlayerType.EASY_BOT),
                    1

            ));
        }

        return playersTypes;

    }
}
