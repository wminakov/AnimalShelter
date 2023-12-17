package databases;

import abstractAnimals.Animal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface DatabaseInterface {
    void addAnimal(Animal animal) throws SQLException, IOException;

    void delAnimal(int id) throws SQLException, IOException;

    HashMap<String, String> getAnimal(int id) throws SQLException, IOException;

    void updateAnimal(Animal animal) throws SQLException, IOException;

    ArrayList<HashMap<String, String>> getAnimalsList() throws SQLException, IOException;
}
