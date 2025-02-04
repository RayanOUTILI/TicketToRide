package fr.cotedazur.univ.polytech.ttr.equipeb.models.score;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;

import java.util.Comparator;

public class ScoreComparator implements Comparator<PlayerModel> {
    @Override
    public int compare(PlayerModel o1, PlayerModel o2) {
        if(o1.getScore() != o2.getScore()) return o2.getScore() - o1.getScore();
        if(o1.getNumberOfCompletedObjectiveCards() != o2.getNumberOfCompletedObjectiveCards()) return o2.getNumberOfCompletedObjectiveCards() - o1.getNumberOfCompletedObjectiveCards();
        return o2.getLongestContinuousRouteLength() - o1.getLongestContinuousRouteLength();
    }
}
