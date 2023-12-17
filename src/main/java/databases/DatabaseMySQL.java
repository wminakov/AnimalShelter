package databases;

import abstractAnimals.Animal;

import java.sql.*;
import java.util.ArrayList;

import java.util.HashMap;

public class DatabaseMySQL extends Database {
    private final String url;
    private final String user;
    private final String password;
    private final String database;
    private Connection myConnect;

    public DatabaseMySQL(String database, String user, String password, int port, String host) throws SQLException, ClassNotFoundException {
        this.url = "jdbc:mysql://" + host + ":" + port + "/";
        this.user = user;
        this.password = password;
        this.database = database;

        try {
            myConnect = mysqlConnect();
        } finally {

        }

        try {
            initDB();
        } finally {

        }
    }

    private void initDB() throws SQLException {
        try (Statement statement = myConnect.createStatement()) {

            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + database + ";");
            statement.execute("USE " + database);

            String sql = "SELECT COUNT(*) AS table_count FROM information_schema.tables WHERE table_schema = '" + database + "' AND table_name = 'animalTypes'";
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                int tableCount = resultSet.getInt("table_count");
                if (tableCount == 0) {
                    statement.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS animalTypes (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                type VARCHAR(50)
                            );""");

                    statement.executeUpdate("""
                            INSERT INTO animalTypes (type) VALUES ('Dog');
                            """);
                    statement.executeUpdate("""
                            INSERT INTO animalTypes (type) VALUES ('Cat');
                            """);
                    statement.executeUpdate("""
                            INSERT INTO animalTypes (type) VALUES ('Hamster');
                            """);
                    statement.executeUpdate("""
                            INSERT INTO animalTypes (type) VALUES ('Horse');
                            """);
                    statement.executeUpdate("""
                            INSERT INTO animalTypes (type) VALUES ('Donkey');
                            """);
                    statement.executeUpdate("""
                            INSERT INTO animalTypes (type) VALUES ('Camel');
                            """);
                }
            }

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS animals (
                        id INT AUTO_INCREMENT PRIMARY KEY,    
                        animalType INT,             
                        name VARCHAR(50),
                        birthday DATE,
                        skills TEXT,
                        FOREIGN KEY (animalType) REFERENCES animalTypes(id)
                    );""");

        }
    }

    public Connection mysqlConnect() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(this.url, this.user, this.password);
        } finally {

        }
    }

    public void disconnect() {
        try {
            myConnect.close();
        } catch (SQLException ignored) {

        }
    }

    @Override
    public void addAnimal(Animal animal) throws SQLException {
        String sql = ("""
                INSERT INTO animals
                (animalType, name, birthday)
                VALUES (?, ?, ?);
                """);

        try (PreparedStatement statement = myConnect.prepareStatement(sql)) {
            statement.setInt(1, getAnimalTypeID(animal.getClassName()));
            statement.setString(2, animal.getName());

            Date date = Date.valueOf(animal.getBirthyear() + "-" + animal.getBirthmonth() + "-" + animal.getBirthday());
            statement.setDate(3, date);

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Data not added");
            }
        }
    }

    private int getAnimalTypeID(String typeName) throws SQLException {
        String sql = ("""
                SELECT id
                FROM animalTypes
                WHERE type = ?;
                """);

        try (PreparedStatement statement = myConnect.prepareStatement(sql)) {
            statement.setString(1, typeName);
            ResultSet set = statement.executeQuery();

            while (set.next()) {
                return set.getInt("id");
            }
        }

        throw new SQLException("Did not find the type of animal in the database table");
    }

    @Override
    public void delAnimal(int id) throws SQLException {
        String sql = ("""
                DELETE 
                FROM animals
                WHERE id = ?;
                """);

        try (PreparedStatement statement = myConnect.prepareStatement(sql)) {
            statement.setInt(1, id);

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new SQLException("The data has not been deleted");
            }
        }
    }

    @Override
    public HashMap<String, String> getAnimal(int id) throws SQLException {
        String sql = ("""
                SELECT *
                FROM animals
                LEFT JOIN animalTypes
                ON animals.animalType = animalTypes.id
                WHERE animals.id = ?;
                """);

        HashMap<String, String> animal = new HashMap<>();

        try (PreparedStatement statement = myConnect.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();

            while (set.next()) {
                animal.put("id", set.getString("id"));
                animal.put("name", set.getString("name"));
                animal.put("skills", set.getString("skills"));
                animal.put("birthday", set.getString("birthday"));
                animal.put("type", set.getString("type"));

                return animal;
            }
        }

        return null;
    }

    @Override
    public void updateAnimal(Animal animal) throws SQLException {
        String sql = ("""
                UPDATE animals
                SET
                name = ?,
                birthday = ?,
                skills = ?
                WHERE id = ?;
                """);

        try (PreparedStatement statement = myConnect.prepareStatement(sql)) {
            statement.setString(1, animal.getName());

            Date date = Date.valueOf(animal.getBirthyear() + "-" + animal.getBirthmonth() + "-" + animal.getBirthday());
            statement.setDate(2, date);

            StringBuilder skills = new StringBuilder();
            for (String skill : animal.getSkills()) {
                skills.append(skill).append("|");
            }
            if (animal.getSkills().size() > 0) {
                skills.delete(skills.length() - 1, skills.length());
            }

            statement.setString(3, skills.toString());

            statement.setInt(4, animal.getId());

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Data not updated");
            }
        }
    }

    @Override
    public ArrayList<HashMap<String, String>> getAnimalsList() throws SQLException {
        ArrayList<HashMap<String, String>> animalList = new ArrayList<>();

        String sql = ("""
                SELECT *
                FROM animals
                LEFT JOIN animalTypes
                ON animals.animalType = animalTypes.id;
                """);

        try (PreparedStatement statement = myConnect.prepareStatement(sql)) {
            ResultSet set = statement.executeQuery();

            while (set.next()) {
                HashMap<String, String> animal = new HashMap<>();
                animal.put("id", set.getString("id"));
                animal.put("name", set.getString("name"));
                animal.put("skills", set.getString("skills"));
                animal.put("birthday", set.getString("birthday"));
                animal.put("type", set.getString("type"));

                animalList.add(animal);
            }
        }

        return animalList;
    }

    @Override
    public void close() throws IllegalStateException {
        disconnect();
        super.close();
    }
}
