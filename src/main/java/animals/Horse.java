package animals;

import abstractAnimals.PackAnimal;

import java.util.ArrayList;

public class Horse extends PackAnimal {

    public Horse(String name, int birthday, int birthmonth, int birthyear) {
        super(name, birthday, birthmonth, birthyear);
    }

    public Horse(int id, String name, int birthday, int birthmonth, int birthyear, ArrayList<String> skills) {
        super(id, name, birthday, birthmonth, birthyear, skills);
    }

    @Override
    public String getClassName() {
        return "Horse";
    }

    @Override
    public String getType() {
        return "Pack";
    }
}
