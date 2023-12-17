import abstractAnimals.Animal;

import java.util.ArrayList;
import java.util.Scanner;

import java.time.Year;

public class View implements AutoCloseable, ViewInterface {
    @Override
    public void showMainMenu() {
        System.out.println("Главное меню:");
        System.out.println("0.Выход");
        System.out.println("1.Посмотреть список животных");
        System.out.println("2.Добавить животное");
        System.out.println("3.Удалить животное");
        System.out.println("4.Открыть параметры животного (Команды)");
    }

    @Override
    public void showAnimalMenu() {
        System.out.println("Меню животного:");
        System.out.println("0.Выход");
        System.out.println("1.Добавить новое умение (команду)");
        System.out.println("2.Удалить умение (команду)");
    }

    @Override
    public void showMessage(String msg) {
        System.out.println(msg);
        System.out.println();
    }

    public void showAnimalClasses(String[] animalClasses) {
        System.out.println("Виды животных:");

        int i = 1;
        for (String animalClass : animalClasses) {
            System.out.println("" + i + "." + getViewClassName(animalClass));
            i++;
        }
    }

    public String inputString(String query) {
        while (true) {
            System.out.print(query + ": ");
            Scanner scanner = new Scanner(System.in);
            String str = scanner.nextLine();
            System.out.println();
            str = str.replace(";", "").replace("|", "");

            if (!str.equals("")) return str;
        }
    }

    @Override
    public String inputName() {
        return inputString("Введите имя животного");
    }

    @Override
    public String inputNewSkill() {
        return inputString("Введите новое умение");
    }

    public int inputYear(String query) {
        int year = 2000;
        int currentYear = Year.now().getValue();

        boolean flag = true;
        while (flag) {
            flag = false;
            year = inputNumber(query);

            if (year > currentYear) {
                showMessage("Год больше текущего!");
                flag = true;
            }

            if (currentYear - year > 100) {
                showMessage("К сожалению столько не живут!");
                flag = true;
            }
        }

        return year;
    }

    @Override
    public int inputBirthyear() {
        return inputYear("Введите год рождения животного");
    }

    public int inputMonth(String query) {
        int month = 1;
        boolean flag = true;
        while (flag) {
            flag = false;
            month = inputNumber(query);

            if (month < 1 || month > 12) {
                showMessage("Месяц должен быть 1 - 12");
                flag = true;
            }
        }

        return month;
    }

    @Override
    public int inputBirthmonth() {
        return inputMonth("Введите номер месяца рождения животного (1-12)");
    }

    public int inputDay(String query) {
        int day = 1;
        boolean flag = true;
        while (flag) {
            flag = false;
            day = inputNumber(query);

            if (day < 1 || day > 31) {
                showMessage("День должен быть 1 - 31");
                flag = true;
            }
        }

        return day;
    }

    @Override
    public int inputBirthday() {
        return inputDay("Введите день рождения животного");
    }

    public int inputNumber(String query) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(query + ": ");
            String str = scanner.nextLine();
            System.out.println();

            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException ignored) {

            }
        }
    }

    @Override
    public int inputMenuNumber() {
        return inputNumber("Выберите пункт меню");
    }

    @Override
    public int inputAnimalIDforDelete() {
        return inputNumber("Введите ID животного для удаления (0 - отмена)");
    }

    @Override
    public int inputSkillIDforDelete() {
        return inputNumber("Введите ID умения для удаления (0 - отмена)");
    }

    @Override
    public int inputAnimalID() {
        return inputNumber("Введите ID животного для просмотра данных (0 - отмена)");
    }

    @Override
    public void showIdError(int id) {
        showMessage("ID:" + id + " не найден");
    }

    @Override
    public void showAnimalList(ArrayList<Animal> animals) {
        if (animals == null || animals.size() == 0) {
            System.out.println("Пока нет ни одного животного.");
            System.out.println();
        } else {
            System.out.println("Список животных:");
            for (Animal animal : animals) {
                if (animal != null) showAnimal(animal);
            }
            System.out.println();
        }
    }

    @Override
    public Integer selectAnimalClassId(String[] animalClasses) {
        int choice;
        while (true) {
            showAnimalClasses(animalClasses);
            choice = inputNumber("Выберите вид животного" + "");
            if (choice >= 1 && choice <= animalClasses.length) {
                return choice;
            }
        }
    }

    @Override
    public void showAnimalParams(Animal animal) {
        System.out.println("Параметры: ");
        System.out.println("Ид: " + animal.getId());
        System.out.println("Животное: " + getViewClassName(animal.getClassName()));
        System.out.println("Тип: " + getViewTypeName(animal.getType()));
        System.out.println("Имя: " + animal.getName());
        System.out.println("Дата рождения: " + Shelter.formatDate(animal.getBirthday(), animal.getBirthmonth(), animal.getBirthyear()));
        System.out.println("Возраст: " + Animal.calcAge(animal.getBirthday(), animal.getBirthmonth(), animal.getBirthyear()));
        System.out.println();
        showSkills(animal);
    }

    private void showSkills(Animal animal) {
        System.out.println("Навыки:");

        if (animal.getSkills().size() == 0) {
            System.out.println("Навыков еще нет.");
        } else {
            int i = 1;
            for (String skill : animal.getSkills()) {
                System.out.println("" + i + "." + skill);
                i++;
            }
        }
        System.out.println();
    }

    @Override
    public void close() {

    }

    private String getViewTypeName(String type) {
        return switch (type) {
            case "Pack" -> "Вьючное";
            case "Home" -> "Домашнее";
            default -> type;
        };
    }

    private String getViewClassName(String className) {
        return switch (className) {
            case "Cat" -> "Кошка";
            case "Dog" -> "Собака";
            case "Hamster" -> "Хомяк";
            case "Horse" -> "Лошадь";
            case "Donkey" -> "Осел";
            case "Camel" -> "Верблюд";
            default -> className;
        };
    }

    @Override
    public void showAnimal(Animal animal) {
        System.out.println("Ид:" + animal.getId() + " " + getViewTypeName(animal.getType()) + " " + getViewClassName(animal.getClassName()) + ": " + animal.getName() + " Возраст: " + Animal.calcAge(animal.getBirthday(), animal.getBirthmonth(), animal.getBirthyear()));
    }

    @Override
    public void showGoodBye() {
        System.out.println("До новых встреч!");
        System.out.println();
    }

    @Override
    public void showDateError() {
        showMessage("Такой даты не существует");
    }
}
