package animals;

import abstractAnimals.HomeAnimal;

import java.util.ArrayList;

public class Dog extends HomeAnimal {
    public Dog(String name, int birthday, int birthmonth, int birthyear) {
        super(name, birthday, birthmonth, birthyear);
    }

    public Dog(int id, String name, int birthday, int birthmonth, int birthyear, ArrayList<String> skills) {
        super(id, name, birthday, birthmonth, birthyear, skills);
    }

    @Override
    public String getClassName() {
        return "Dog";
    }

    @Override
    public String getType() {
        return "Home";
    }
}
