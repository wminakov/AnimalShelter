package abstractAnimals;

import java.util.ArrayList;

public abstract class HomeAnimal extends Animal {
    protected HomeAnimal(String name, int birthday, int birthmonth, int birthyear) {
        super(name, birthday, birthmonth, birthyear);
    }

    protected HomeAnimal(int id, String name, int birthday, int birthmonth, int birthyear, ArrayList<String> skills) {
        super(id, name, birthday, birthmonth, birthyear, skills);
    }
}
