package fr.cotedazur.univ.polytech.ttr.equipeb.simulations;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;

import java.util.List;

public class GameExecutionInfos {
    private List<PlayerType> players;
    private int executionNumber;
    private String label;

    public GameExecutionInfos(List<PlayerType> players, int executionNumber) {
        this.players = players;
        this.executionNumber = executionNumber;
        label = null;
    }

    public void setCustomLabel(String label){
        this.label = label;
    }

    public String getLabel() {
        if (label != null) {
            return label;
        }
        return players.stream()
                .map(PlayerType::name)
                .reduce((a, b) -> a + " - " + b)
                .orElse("");
    }

    public List<PlayerType> getPlayersType() {
        return players;
    }

    public int getExecutionNumber() {
        return executionNumber;
    }
}
