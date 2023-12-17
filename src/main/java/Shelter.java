import abstractAnimals.Animal;
import animals.*;
import databases.Database;
import logs.Log;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Shelter implements ShelterInterface {
    private final View view;
    private Log log;
    private final Database db;

    public Shelter(View view, Log log, Database db) {
        this.log = log;
        this.view = view;
        this.db = db;
    }

    public void start() {
        int choice = -1;
        while (choice != 0) {
            view.showMainMenu();
            choice = view.inputMenuNumber();

            switch (choice) {
                case 0:
                    view.showGoodBye();
                    break;
                case 1:
                    view.showAnimalList(getAnimals());
                    break;
                case 2:
                    String className = getAnimalClasses()[view.selectAnimalClassId(getAnimalClasses()) - 1];
                    String name = view.inputName();
                    int birthyear = view.inputBirthyear();
                    int birthmonth = view.inputBirthmonth();
                    int birthday = view.inputBirthday();

                    if (!checkDate(birthday, birthmonth, birthyear)) {
                        view.showDateError();
                        break;
                    }

                    addAnimal(createNewAnimal(className, name, birthyear, birthmonth, birthday));
                    view.showAnimalList(getAnimals());
                    break;
                case 3:
                    view.showAnimalList(getAnimals());

                    int id = view.inputAnimalIDforDelete();

                    if (id == 0) break;

                    Animal delAnimal = getAnimal(id);

                    if (delAnimal != null) {
                        delAnimal(id);
                        view.showAnimalList(getAnimals());
                    } else {
                        view.showIdError(id);
                    }

                    break;
                case 4:
                    view.showAnimalList(getAnimals());

                    int curr_id = view.inputAnimalID();
                    if (curr_id == 0) break;

                    Animal currentAnimal = getAnimal(curr_id);
                    if (currentAnimal != null) {
                        int choice2 = -1;
                        while (choice2 != 0) {
                            view.showAnimalParams(currentAnimal);
                            view.showAnimalMenu();
                            choice2 = view.inputMenuNumber();

                            switch (choice2) {
                                case 1:
                                    currentAnimal.addAnimalSkill(view.inputNewSkill());
                                    updateAnimal(currentAnimal);
                                    break;
                                case 2:
                                    if (currentAnimal.getSkills().size() == 0) break;

                                    view.showAnimalParams(currentAnimal);
                                    int id_skill = view.inputSkillIDforDelete();

                                    if (id_skill < 1 || id_skill > currentAnimal.getSkills().size()) break;

                                    currentAnimal.delAnimalSkill(id_skill - 1);

                                    try {
                                        db.updateAnimal(currentAnimal);
                                    } catch (Exception e) {
                                        log.append(e.getMessage());
                                    }

                                    break;
                            }
                        }
                    } else {
                        view.showIdError(curr_id);
                    }
                    break;
            }
        }
    }

    public ArrayList<Animal> getAnimals() {
        ArrayList<HashMap<String, String>> animalList = new ArrayList<>();
        ArrayList<Animal> animals = new ArrayList<>();

        try {
            animalList =  db.getAnimalsList();
        } catch (Exception e) {
            log.append(e.getMessage());
            view.showMessage(e.getMessage());
        }

        for (HashMap<String, String> animalHashMap: animalList) {
            animals.add(createAnimal(animalHashMap));
        }

        return animals;
    }

    @Override
    public void addAnimal(Animal animal) {
        try {
            db.addAnimal(animal);
        } catch (Exception e) {
            log.append(e.getMessage());
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public void delAnimal(int id) {
        try {
            db.delAnimal(id);
        } catch (Exception e) {
            log.append(e.getMessage());
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public Animal getAnimal(int id) {
        HashMap<String, String> animalHashMap = null;
        try {
            animalHashMap = db.getAnimal(id);
        } catch (Exception e) {
            log.append(e.getMessage());
            view.showMessage(e.getMessage());
        }

        Animal animal = createAnimal(animalHashMap);
        return animal;
    }

    @Override
    public void updateAnimal(Animal animal) {
        try {
            db.updateAnimal(animal);
        } catch (Exception e) {
            log.append(e.getMessage());
            view.showMessage(e.getMessage());
        }
    }

    private Animal createNewAnimal(String className, String name, int birthyear, int birthmonth, int birthday) {
        Animal animal = null;

        switch (className) {
            case "Dog" -> {
                animal = new Dog(name, birthday, birthmonth, birthyear);
            }
            case "Cat" -> {
                animal = new Cat(name, birthday, birthmonth, birthyear);
            }
            case "Hamster" -> {
                animal = new Hamster(name, birthday, birthmonth, birthyear);
            }
            case "Camel" -> {
                animal = new Camel(name, birthday, birthmonth, birthyear);
            }
            case "Horse" -> {
                animal = new Horse(name, birthday, birthmonth, birthyear);
            }
            case "Donkey" -> {
                animal = new Donkey(name, birthday, birthmonth, birthyear);
            }
        }

        return animal;
    }

    private Animal createAnimal(HashMap<String, String> animalHashMap) {
        Animal animal = null;

        if (animalHashMap == null) return animal;

        String[] birth = animalHashMap.get("birthday").split("-", -1);

        int birthday = Integer.parseInt(birth[2]);
        int birthmonth = Integer.parseInt(birth[1]);
        int birthyear = Integer.parseInt(birth[0]);

        ArrayList<String> skills = new ArrayList<>();

        if (animalHashMap.get("skills") != null && !animalHashMap.get("skills").equals("")) {
            String[] arrSkills = animalHashMap.get("skills").split("\\|", -1);
            skills = new ArrayList<>(Arrays.asList(arrSkills));
        }

        switch (animalHashMap.get("type")) {
            case "Dog" -> {
                animal = new Dog(Integer.parseInt(animalHashMap.get("id")), animalHashMap.get("name"), birthday, birthmonth, birthyear, skills);
            }
            case "Cat" -> {
                animal = new Cat(Integer.parseInt(animalHashMap.get("id")), animalHashMap.get("name"), birthday, birthmonth, birthyear, skills);
            }
            case "Hamster" -> {
                animal = new Hamster(Integer.parseInt(animalHashMap.get("id")), animalHashMap.get("name"), birthday, birthmonth, birthyear, skills);
            }
            case "Camel" -> {
                animal = new Camel(Integer.parseInt(animalHashMap.get("id")), animalHashMap.get("name"), birthday, birthmonth, birthyear, skills);
            }
            case "Horse" -> {
                animal = new Horse(Integer.parseInt(animalHashMap.get("id")), animalHashMap.get("name"), birthday, birthmonth, birthyear, skills);
            }
            case "Donkey" -> {
                animal = new Donkey(Integer.parseInt(animalHashMap.get("id")), animalHashMap.get("name"), birthday, birthmonth, birthyear, skills);
            }
        }

        return animal;
    }

    public static String formatDate(int day, int month, int year) {
        StringBuilder sb = new StringBuilder();

        sb.append(year);
        sb.append("-");
        if (month < 10) sb.append("0");
        sb.append(month);
        sb.append("-");
        if (day < 10) sb.append("0");
        sb.append(day);

        return sb.toString();
    }

    public static boolean checkDate(int day, int month, int year) {
        try {
            LocalDate date = LocalDate.parse(formatDate(day, month, year));
        } catch (DateTimeParseException e) {
            return false;
        }

        return true;
    }

    private String[] getAnimalClasses() {
        String[] animalTypes = new String[6];

        animalTypes[0] = "Cat";
        animalTypes[1] = "Dog";
        animalTypes[2] = "Hamster";
        animalTypes[3] = "Horse";
        animalTypes[4] = "Donkey";
        animalTypes[5] = "Camel";

        return animalTypes;
    }
}
