import databases.Database;
import databases.DatabaseFiles;
import databases.DatabaseMySQL;
import logs.Log;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        try (View view = new View()) {
            try (Log log = new Log()) {
                HashMap<String, String> config = readConfig();

                if (getConfigParam(config, "dbtype").equals("mysql")) {
                    int mysql_port = 3306;
                    try {
                        mysql_port = Integer.parseInt(getConfigParam(config, "mysql_port"));
                    } catch (NumberFormatException ignored) {

                    }

                    try (Database db = new DatabaseMySQL("Shelter", getConfigParam(config, "mysql_user"), getConfigParam(config, "mysql_pass"), mysql_port, getConfigParam(config, "mysql_host"))) {
                        Shelter shelter = new Shelter(view, log, db);
                        shelter.start();
                        db.useResource();
                    } catch (Exception e) {
                        log.append(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
                        view.showMessage(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
                    }
                } else {
                    try (Database db = new DatabaseFiles(getConfigParam(config, "file_db_path"))) {
                        Shelter shelter = new Shelter(view, log, db);
                        shelter.start();
                        db.useResource();
                    } catch (IllegalStateException | IOException e) {
                        log.append(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
                        view.showMessage(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
                    }
                }
            } catch (IOException e) {
                view.showMessage(e.getMessage());
            }
        } catch (Exception ignored) {

        }
    }

    private static HashMap<String, String> readConfig() {
        HashMap<String, String> config = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("config.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] arrLine = line.replace("\n", "").split("=", -1);

                if (arrLine.length == 2) config.put(arrLine[0], arrLine[1]);
            }
        } catch (IOException ignored) {

        }

        return config;
    }

    private static String getConfigParam(HashMap<String, String> config, String params) {
        String res = config.get(params);
        if (res != null) return res;

        return "";
    }
}

