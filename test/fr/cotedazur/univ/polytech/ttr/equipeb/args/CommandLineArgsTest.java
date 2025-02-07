package fr.cotedazur.univ.polytech.ttr.equipeb.args;

import com.beust.jcommander.ParameterException;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.views.ViewOptions;
import fr.cotedazur.univ.polytech.ttr.equipeb.simulations.GameExecutionInfos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandLineArgsTest {

    private CommandLineArgs commandLineArgs;

    @BeforeEach
    void setUp() {
        commandLineArgs = new CommandLineArgs();
    }

    @Test
    void parseWithValidArgs() {
        String[] args = {"--csv", "--nbOfGames", "10"};
        CommandLineArgs result = CommandLineArgs.parse(args);

        assertNotNull(result);
        assertTrue(result.getViewOptions().contains(ViewOptions.CSV));
        assertEquals(10, result.getPlayersTypesToPlay().get(0).getExecutionNumber());
    }

    @Test
    void parseWithInvalidArgsThrowsException() {
        String[] args = {"--invalid"};
        assertThrows(ParameterException.class, () -> CommandLineArgs.parse(args));
    }

    @Test
    void parseWithVerboseOption() {
        String[] args = {"--verbose", "3"};
        assertThrows(ParameterException.class, () -> CommandLineArgs.parse(args));
    }

    @Test
    void validateThrowsExceptionIfNoValidOption() {
        commandLineArgs = new CommandLineArgs();
        assertThrows(ParameterException.class, commandLineArgs::validate);
    }

    @Test
    void validateThrowsExceptionIfMultipleConflictingOptions() {
        commandLineArgs.setDemo(true);
        commandLineArgs.setTwothousands(true);
        commandLineArgs.setNbOfGames(10);
        assertThrows(ParameterException.class, commandLineArgs::validate);
    }

    @Test
    void getViewOptionsReturnsCorrectOptions() {
        commandLineArgs.setCsv(true);
        commandLineArgs.setDatabase(true);
        commandLineArgs.setTwothousands(true);
        commandLineArgs.setDemo(true);

        List<ViewOptions> viewOptions = commandLineArgs.getViewOptions();

        assertTrue(viewOptions.contains(ViewOptions.CSV));
        assertTrue(viewOptions.contains(ViewOptions.CLI_STATS));
        assertTrue(viewOptions.contains(ViewOptions.CLI_VERBOSE));
        assertTrue(viewOptions.contains(ViewOptions.DATABASE));
    }

    @Test
    void getViewOptionsReturnsEmptyListWhenNoOptionsAreSet() {
        List<ViewOptions> viewOptions = commandLineArgs.getViewOptions();
        assertTrue(viewOptions.isEmpty());
    }

    @Test
    void getPlayersTypesToPlayReturnsCorrectGameExecutionInfos() {
        commandLineArgs.setTwothousands(true);
        commandLineArgs.setDemo(true);
        commandLineArgs.setNbOfGames(5);

        List<GameExecutionInfos> gameExecutionInfos = commandLineArgs.getPlayersTypesToPlay();

        assertEquals(4, gameExecutionInfos.size());
    }

    @Test
    void settersModifyValuesCorrectly() {
        commandLineArgs.setCsv(true);
        assertTrue(commandLineArgs.getViewOptions().contains(ViewOptions.CSV));

        commandLineArgs.setDatabase(true);
        assertTrue(commandLineArgs.getViewOptions().contains(ViewOptions.DATABASE));

        commandLineArgs.setDemo(true);
        assertTrue(commandLineArgs.getViewOptions().contains(ViewOptions.CLI_VERBOSE));

        commandLineArgs.setTwothousands(true);
        assertTrue(commandLineArgs.getViewOptions().contains(ViewOptions.CLI_STATS));
    }
}
