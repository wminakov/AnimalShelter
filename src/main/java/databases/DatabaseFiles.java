package databases;

import abstractAnimals.Animal;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseFiles extends Database {
    private final String DBFilePath;

    public DatabaseFiles(String path) throws IOException {
        if (path.equals("")) throw new IOException("The path to the database file is not specified in config.txt");

        DBFilePath = path;
        File file = new File(DBFilePath);

        if (!file.exists()) {
            try {
                boolean res = file.createNewFile();
            } catch (IOException ignored) {
                throw new IOException("Could not create a database file!");
            }
        }
    }

    @Override
    public void addAnimal(Animal animal) throws IOException {
        int lastId = getLastID();
        lastId++;
        animal.setId(lastId);

        try (FileWriter fw = new FileWriter(DBFilePath, true)) {
            fw.write(createDBLine(animal));
        }
    }

    @Override
    public void delAnimal(int id) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(DBFilePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] arrLine = line.split(";");
                if (Integer.parseInt(arrLine[0]) != id) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                }
            }
        }

        try (FileOutputStream fileOut = new FileOutputStream(DBFilePath)) {
            fileOut.write(sb.toString().getBytes());
        }
    }

    @Override
    public HashMap<String, String> getAnimal(int id) throws IOException {
        HashMap<String, String> animal = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(DBFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] arrLine = line.replace("\n", "").split(";", -1);
                if (Integer.parseInt(arrLine[0]) == id) {
                    animal.put("id", arrLine[0]);
                    animal.put("name", arrLine[2]);
                    animal.put("skills", arrLine[4]);
                    animal.put("birthday", arrLine[3]);
                    animal.put("type", arrLine[1]);

                    return animal;
                }
            }
        }

        return null;
    }

    @Override
    public void updateAnimal(Animal animal) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(DBFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] arrLine = line.split(";");
                if (Integer.parseInt(arrLine[0]) != animal.getId()) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                } else {
                    sb.append(createDBLine(animal));
                }
            }
        }

        try (FileOutputStream fileOut = new FileOutputStream(DBFilePath)) {
            fileOut.write(sb.toString().getBytes());
        }
    }

    @Override
    public ArrayList<HashMap<String, String>> getAnimalsList() throws IOException {
        ArrayList<HashMap<String, String>> animalList = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(DBFilePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] animalArray = parseDatabaseLine(line.replace("\n", ""));

                HashMap<String, String> animal = new HashMap<>();
                animal.put("id", animalArray[0]);
                animal.put("name", animalArray[2]);
                animal.put("skills", animalArray[4]);
                animal.put("birthday", animalArray[3]);
                animal.put("type", animalArray[1]);

                animalList.add(animal);
            }
        }

        return animalList;
    }

    public Integer getLastID() throws IOException {
        String result;

        try (RandomAccessFile file = new RandomAccessFile(DBFilePath, "r")) {
            long fileLength = file.length();

            StringBuilder lastLine = new StringBuilder();
            long pointer = fileLength - 1;

            while (pointer >= 0) {
                file.seek(pointer);
                char currentChar = (char) file.readByte();

                if (currentChar == '\n' && lastLine.length() > 0) {
                    break;
                }

                lastLine.insert(0, currentChar);
                pointer--;
            }

            result = lastLine.toString().replace("\n", "");
        }

        if (!result.equals("")) return Integer.parseInt(result.split(";")[0]);

        return 0;
    }

    private String[] parseDatabaseLine(String line) {
        return line.split(";", -1);
    }

    private String createDBLine(Animal animal) {
        StringBuilder sb = new StringBuilder();

        //id
        sb.append(animal.getId()).append(";");

        // type
        sb.append(animal.getClassName()).append(";");

        // name
        sb.append(animal.getName()).append(";");

        // date birthday
        sb.append(animal.getBirthyear()).append("-").append(animal.getBirthmonth()).append("-").append(animal.getBirthday()).append(";");

        // skills
        for (String skill : animal.getSkills()) {
            sb.append(skill).append("|");
        }
        if (animal.getSkills().size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }

        sb.append("\n");

        return sb.toString();
    }
}
