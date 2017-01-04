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
        runPartOne();
        runPartTwo();

    }

    public static void runPartOne() {
        run("Part 1", false);
    }
    public static void runPartTwo() {
        run("Part 2", true);
    }

    // Run the simulation
    public static void run(String name, boolean stopOnRevisit) {
        Traveler traveler = new Traveler("N");
        for (String direction : directions) {
            traveler.move(direction, stopOnRevisit);

            // If this is the second time the traveler has reached this point
            // and we're stopping after a revisit, break the loop.
            if (traveler.secondStop && stopOnRevisit) {
                break;
            }
        }
        System.out.printf("%s: The traveler is %d blocks from the starting point.\n", name, traveler.countBlocks());
    }

    // Read the input from an external file into a class variable for later use.
    public static void parseInput(String fileName) {
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach( (line) -> {
                String[] dirs = line.split(", ");
                for (String dir : dirs) {
                    directions.add(dir);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

