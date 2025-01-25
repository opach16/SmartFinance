package com.konrad.smartfinance;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static Logger instance;
    private FileWriter fileWriter;
    private static final String LOG_FILE = "highTransactions.log";

    private Logger() {
        try {
            fileWriter = new FileWriter(LOG_FILE, true);
        } catch (IOException e) {
            System.err.println("Cannot create a log file." + e.getMessage());
        }
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String message) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        try {
            fileWriter.write(date + " " + message + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            System.err.println("Cannot write to file. " + e.getMessage());
        }
    }
}
