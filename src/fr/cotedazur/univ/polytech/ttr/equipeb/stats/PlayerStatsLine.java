package fr.cotedazur.univ.polytech.ttr.equipeb.stats;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.action.StatAction;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.action.StatActionStatus;

import java.util.UUID;

public class PlayerStatsLine {

    public static final String[] headers = new String[]{
            "timestamp",
            "game_id",
            "player_id",
            "player_type",
            "player_color",
            "current_turn",
            "action",
            "action_status",
            "displayed_score",
            "wagons_cards_hand_count",
            "destination_cards_hand_count",
            "calculated_current_destination_score"
    };

    public long currentTime;

    public UUID gameId;
    public UUID playerId;
    public PlayerType playerType;
    public PlayerIdentification playerColor;

    public int currentTurn;
    public StatAction action;
    public StatActionStatus actionStatus;

    public int score;
    public int wagonsCards;
    public int destinationCards;

    public int currentDestinationScore;

    public PlayerStatsLine(UUID playerId, PlayerIdentification playerColor, PlayerType playerType) {
        this.gameId = null;
        this.playerId = playerId;
        this.playerColor = playerColor;
        this.playerType = playerType;
        clearLine();
    }

    public void clearLine(){
        this.currentTime = -1;
        this.currentTurn = -1;
        this.action = null;
        this.actionStatus = null;
        this.score = -1;
        this.wagonsCards = -1;
        this.destinationCards = -1;
        this.currentDestinationScore = -1;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public PlayerIdentification getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(PlayerIdentification playerColor) {
        this.playerColor = playerColor;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public StatAction getAction() {
        return action;
    }

    public void setAction(StatAction action) {
        this.action = action;
    }

    public StatActionStatus getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(StatActionStatus actionStatus) {
        this.actionStatus = actionStatus;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getWagonsCards() {
        return wagonsCards;
    }

    public void setWagonsCards(int wagonsCards) {
        this.wagonsCards = wagonsCards;
    }

    public int getDestinationCards() {
        return destinationCards;
    }

    public void setDestinationCards(int destinationCards) {
        this.destinationCards = destinationCards;
    }

    public int getCurrentDestinationScore() {
        return currentDestinationScore;
    }

    public void setCurrentDestinationScore(int currentDestinationScore) {
        this.currentDestinationScore = currentDestinationScore;
    }

    public PlayerStatsLine cloneWithTurn(){
        PlayerStatsLine line = new PlayerStatsLine(this.playerId, this.playerColor, this.playerType);
        line.setGameId(this.gameId);
        line.setCurrentTurn(this.currentTurn);
        return line;
    }

    public String[] getValues(){
        return new String[]{
                String.valueOf(currentTime),
                gameId.toString(),
                playerId.toString(),
                playerType.toString(),
                playerColor.getLabel(),
                String.valueOf(currentTurn),
                action.toString(),
                actionStatus.toString(),
                String.valueOf(score),
                String.valueOf(wagonsCards),
                String.valueOf(destinationCards),
                String.valueOf(currentDestinationScore)
        };
    }
}
