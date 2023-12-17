import abstractAnimals.Animal;

import java.util.ArrayList;

public interface ViewInterface {
    void showAnimal(Animal animal);

    void showMainMenu();

    void showAnimalMenu();

    void showMessage(String msg);

    void showDateError();

    void showGoodBye();

    void showIdError(int id);

    int inputAnimalID();

    int inputSkillIDforDelete();

    int inputAnimalIDforDelete();

    int inputBirthyear();

    int inputBirthmonth();

    int inputBirthday();

    int inputMenuNumber();

    String inputNewSkill();

    String inputName();

    void showAnimalList(ArrayList<Animal> animals);

    Integer selectAnimalClassId(String[] animalClasses);

    void showAnimalParams(Animal animal);
}
