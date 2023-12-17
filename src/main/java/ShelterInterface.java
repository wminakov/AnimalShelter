import abstractAnimals.Animal;

import java.util.ArrayList;

public interface ShelterInterface {
    ArrayList<Animal> getAnimals();

    void addAnimal(Animal animal);

    void  delAnimal(int id);

    Animal getAnimal(int id);

    void updateAnimal(Animal animal);
}
