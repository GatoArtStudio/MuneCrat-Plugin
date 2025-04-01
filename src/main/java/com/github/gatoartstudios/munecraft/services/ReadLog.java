package com.github.gatoartstudios.munecraft.services;

import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.core.implement.ServiceThread;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;

import java.io.File;
import java.io.RandomAccessFile;

public class ReadLog extends ServiceThread {
    /**
     * Read the last log file and send each new line to the EventDispatcher to be logged.
     * <p>
     * This method is called when the service starts and it runs in a separate thread.
     * <p>
     * It reads the last log file from the beginning to the end and then wait for new lines to be written.
     * <p>
     * If there is an error, it logs the error to the EventDispatcher.
     *
     * @throws Exception If there is an error reading the log file.
     */
    @Override
    protected void runService() throws Exception {
        try {
            File logFile = new File("logs/latest.log");
            if (!logFile.exists()) {
                EventDispatcher.dispatchLogging("The log file does not exist.");
                LoggerCustom.error("The log file does not exist.");
                return;
            }

            RandomAccessFile reader = new RandomAccessFile(logFile, "r");
            reader.seek(logFile.length()); // Start from the end of the file to avoid old logs

            while (!Thread.currentThread().isInterrupted()) {
                String line = reader.readLine();
                if (line != null) {
                    EventDispatcher.dispatchLogging(line);
                } else {
                    Thread.sleep(100); // Wait for 100 milliseconds to avoid high CPU usage
                }
            }
            reader.close();
        } catch (Exception e) {
            EventDispatcher.dispatchLogging("Error in ReadLog: " + e.getMessage());
            LoggerCustom.error("Error in ReadLog: " + e.getMessage());
        }
    }
}
