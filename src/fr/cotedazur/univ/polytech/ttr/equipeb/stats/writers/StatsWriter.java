package fr.cotedazur.univ.polytech.ttr.equipeb.stats.writers;

import fr.cotedazur.univ.polytech.ttr.equipeb.stats.PlayerStatsLine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class StatsWriter {

    private List<PlayerStatsLine> buffer;

    protected StatsWriter() {
        this.buffer = new ArrayList<>();
    }


    public void commit(PlayerStatsLine line){
        line.setCurrentTime(System.nanoTime());
        this.buffer.add(line);
    }

    public void clearBuffer() {
        buffer.clear();
    }

    public abstract void push();

    protected List<PlayerStatsLine> getBuffer() {
        return buffer;
    }

    public List<PlayerStatsLine> getReadableBuffer() {
        return Collections.unmodifiableList(buffer);
    }

    public abstract void close() throws Exception;
}
