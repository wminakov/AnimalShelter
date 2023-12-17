package abstractAnimals;

import animals.AnimalInterface;

import java.time.Period;
import java.util.ArrayList;

import java.time.LocalDate;

public abstract class Animal implements AnimalInterface{

    private final int birthday;
    private final int birthmonth;
    private final int birthyear;
    private final String name;
    private int id;

    private ArrayList<String> skills;

    protected Animal(String name, int birthday, int birthmonth, int birthyear) {
        this.birthday = birthday;
        this.birthmonth = birthmonth;
        this.birthyear = birthyear;
        this.skills = new ArrayList<>();
        this.name = name;
    }

    protected Animal(int id, String name, int birthday, int birthmonth, int birthyear, ArrayList<String> skills) {
        this.birthday = birthday;
        this.birthmonth = birthmonth;
        this.birthyear = birthyear;
        this.skills = skills;
        this.id = id;
        this.name = name;
    }

    @Override
    public void addAnimalSkill(String skill) {
        skills.add(skill);
    }

    @Override
    public void delAnimalSkill(int index) {
        skills.remove(index);
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBirthday() {
        return birthday;
    }

    public int getBirthmonth() {
        return birthmonth;
    }

    public int getBirthyear() {
        return birthyear;
    }

    public String getName() {
        return name;
    }

    public static String calcAge(int birthday, int birthmonth, int birthyear) {
        LocalDate currentDate = LocalDate.now();
        LocalDate animalBirthday = LocalDate.of(birthyear, birthmonth, birthday);

        Period age = Period.between(animalBirthday, currentDate);

        return age.getYears() + " г. " + age.getMonths() + " мес. " + age.getDays() + " дн.";
    }

    @Override
    public String toString() {
        return "id:" + getId() + " " + getClassName() + ": " + getName() + " Age: " + Animal.calcAge(getBirthday(), getBirthmonth(), getBirthyear());
    }
}
