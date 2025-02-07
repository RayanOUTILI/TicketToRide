package fr.cotedazur.univ.polytech.ttr.equipeb.stats.writers.csv;

import com.opencsv.CSVWriter;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.writers.StatsWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVStatsWriter extends StatsWriter {
    private final CSVWriter writer;

    public CSVStatsWriter(String path, String[] headers, boolean append) throws IOException {
        super();
        this.writer = new CSVWriter(new FileWriter(path, append));

        if (!append || needToWriteHeader(path)) {
            this.writer.writeNext(headers);
        }
    }

    private boolean needToWriteHeader(String path) {
        File file = new File(path);
        boolean fileExists = file.exists();
        boolean isEmpty = file.length() == 0;
        return !(fileExists) || isEmpty;
    }

    @Override
    public void push(){
        getBuffer().forEach(line -> writer.writeNext(line.getValues()));
        clearBuffer();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}
