package animals;

import abstractAnimals.HomeAnimal;

import java.util.ArrayList;

public class Cat extends HomeAnimal {
    public Cat(String name, int birthday, int birthmonth, int birthyear) {
        super(name, birthday, birthmonth, birthyear);
    }

    public Cat(int id, String name, int birthday, int birthmonth, int birthyear, ArrayList<String> skills) {
        super(id, name, birthday, birthmonth, birthyear, skills);
    }

    @Override
    public String getClassName() {
        return "Cat";
    }

    @Override
    public String getType() {
        return "Home";
    }
}
