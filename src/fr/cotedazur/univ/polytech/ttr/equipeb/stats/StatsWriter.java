package fr.cotedazur.univ.polytech.ttr.equipeb.stats;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StatsWriter {

    List<String[]> lines;
    CSVWriter writer;

    public StatsWriter(String path, String[] headers, boolean append) throws IOException {
        this.writer = new CSVWriter(new FileWriter(path, append));

        if (!append || needToWriteHeader(path)) {
            this.writer.writeNext(headers);
        }

        this.lines = new ArrayList<>();
    }

    private boolean needToWriteHeader(String path) {
        File file = new File(path);
        boolean fileExists = file.exists();
        boolean isEmpty = file.length() == 0;
        return !(fileExists) || isEmpty;
    }

    public void commit(String[] line){
        this.lines.add(line);
    }

    public void push(){
        writer.writeAll(lines);
        lines.clear();
    }

    public void close() throws IOException {
        writer.close();
    }
}
