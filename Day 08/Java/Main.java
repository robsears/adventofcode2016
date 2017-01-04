import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    private static List<String> data = new ArrayList<>();

    public static void main(String[] args) {

        parseInput("./input");
        Screen screen = new Screen(50, 6);
	    for (String s : data) {
            screen.performOperation(s);
        }

        System.out.printf("There are %d lights on.\n\n", screen.countLit());
        screen.debug = true;
        screen.printScreen();
    }

    private static void parseInput(String fileName) {
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(data::add);
        } catch (IOException e) {
            System.out.printf("Could not open file '%s'\n", fileName);
        }
    }
}

