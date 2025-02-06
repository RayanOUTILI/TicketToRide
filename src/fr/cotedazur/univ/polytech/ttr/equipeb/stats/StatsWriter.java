package fr.cotedazur.univ.polytech.ttr.equipeb.stats;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatsWriter {

    List<PlayerStatsLine> buffer;
    CSVWriter writer;

    public StatsWriter(String path, String[] headers, boolean append) throws IOException {
        this.writer = new CSVWriter(new FileWriter(path, append));

        if (!append || needToWriteHeader(path)) {
            this.writer.writeNext(headers);
        }

        this.buffer = new ArrayList<>();
    }

    private boolean needToWriteHeader(String path) {
        File file = new File(path);
        boolean fileExists = file.exists();
        boolean isEmpty = file.length() == 0;
        return !(fileExists) || isEmpty;
    }

    public void commit(PlayerStatsLine line){
        line.setCurrentTime(System.nanoTime());
        this.buffer.add(line);
    }

    public void push(){
        buffer.forEach(line -> writer.writeNext(line.getValues()));
        buffer.clear();
    }

    public List<PlayerStatsLine> getReadableBuffer() {
        return Collections.unmodifiableList(buffer);
    }

    public void close() throws IOException {
        writer.close();
    }
}
