import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    public static List<String> directions = new ArrayList<>();

    public static void main(String[] args) {

        parseInput("./input");

        run("Part 1", "keypad1");
        run("Part 2", "keypad2");

    }

    public static void run(String name, String keypad) {
        List<String> password = new ArrayList<>();
        ButtonPad pad = new ButtonPad("./"+keypad);
        for (String s : directions) {
            char[] dirs = s.toCharArray();
            for (char c : dirs) {
                pad.move(c);
            }
            password.add(pad.current.value);
        }
        System.out.printf("%s: ", name);
        printSecret(password);
    }

    public static void printSecret(List<String> password) {
        System.out.printf("Password is ");
        for (String b : password) {
            System.out.printf("%s", b);
        }
        System.out.printf("\n");
    }

    // Read the input from an external file into a class variable for later use.
    public static void parseInput(String fileName) {
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach( (dir) -> directions.add(dir));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

