package logs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Log implements logInterface, AutoCloseable {
    private final String LogFilePath;

    public Log() throws IOException {
        LogFilePath = "./log.txt";
        File file = new File(LogFilePath);

        if (!file.exists()) {
            try {
                boolean res = file.createNewFile();
            } finally {

            }
        }

        clear();
    }

    @Override
    public void append(String msg) {
        try (FileWriter fw = new FileWriter(LogFilePath, true)) {
            fw.write(msg);
        } catch (IOException ignored) {

        }
    }

    @Override
    public void clear() {
        try (FileWriter fw = new FileWriter(LogFilePath, false)) {
            fw.write("");
        } catch (IOException ignored) {

        }
    }

    @Override
    public void close() throws IOException {

    }
}
