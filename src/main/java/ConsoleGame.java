import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.Random;
import java.util.Scanner;

public class ConsoleGame {
    static Scanner scan = new Scanner(System.in);
    static Random rand = new Random();

    public static void main(String[] args) {
        game();
    }

    public static void game() {

        welcome();

        do {
            int result = menu();

            if (result == 0) {

                System.out.println("Спасибо за игру");
                break;
            }
            if (result == 1) {
                int roundResult = playRound();
                gameResult(roundResult);
            }
        } while (true);

    }

    public static int menu() {
        String inputStr = null;
        int input = -1;
        System.out.println("0 - Выход");
        System.out.println("1 - Новая игра");

        do {
            try {
                inputStr = scan.nextLine();
                input = Integer.parseInt(inputStr);

                if (input == 0 || input == 1) {
                    break;
                } else {
                    System.out.println("\tВы ввели не корректные данные. Попробуйте еще раз");
                }
            } catch (NumberFormatException e) {
                System.out.println("\tВы ввели не корректные данные. Попробуйте еще раз");
            }

        } while (true);

        return input;
    }

    public static void welcome() {
        System.out.println("\tДоброе пожаловать в игру 'от 1 до 100'");
        System.out.println("Правила игры: угадай моё 'загаданное число'. И я покажу где находится вирус");
    }

    public static int playRound() {
        int randomNumber = rand.nextInt(1, 101);
        welcome();
        System.out.println("Игра началась! Посмотрим на что ты способен");
        int attempts = 1;
        String inputStr = null;
        int input = -1;
        do {
            // Проверяем правильность ввода данных
            do {
                try {
                    inputStr = scan.nextLine().trim();
                    input = Integer.parseInt(inputStr);
                    if (input > 0 && input < 101) {
                        break;
                    } else {
                        System.out.println("\tДиапазон от 1 до 100. Вы ввели не корректные данные");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\tДиапазон от 1 до 100. Вы ввели не корректные данные");
                }
            } while (true);

            if (input < randomNumber) {
                System.out.println("Я сам в шоке, но, загаданное число больше, брат");
                attempts++;
            } else if (input > randomNumber) {
                System.out.println("Не ожидал от тебя такого. Загаданное число меньше, брат");
                attempts++;
            }
        } while (input != randomNumber);

        return attempts;
    }

    public static void gameResult(int roundResult) {


        ClassLoader classloader = ConsoleGame.class.getClassLoader();
        URL resource = classloader.getResource("bestResult.txt");

        if (resource == null) {
            System.out.println("Системная ошибка bestResult.txt не найдет!");
        }

        try {
            assert resource != null;
            Path path = Paths.get(resource.toURI());
            String content = Files.readString(path);
            int bestResult = Integer.parseInt(content.trim());

            System.out.printf("Тебе удалось найти секретное число за %s попыток\n", roundResult);

            if (roundResult < bestResult) {
                System.out.printf("Новый рекорд установлен - %s\n", roundResult);

                try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                    writer.write(String.valueOf(roundResult));
                }
            } else {
                System.out.println("Рекорд не побит");
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }


}
