package abstractAnimals;

import java.util.ArrayList;

public abstract class PackAnimal extends Animal {
    protected PackAnimal(String name, int birthday, int birthmonth, int birthyear) {
        super(name, birthday, birthmonth, birthyear);
    }

    protected PackAnimal(int id, String name, int birthday, int birthmonth, int birthyear, ArrayList<String> skills) {
        super(id, name, birthday, birthmonth, birthyear, skills);
    }
}
