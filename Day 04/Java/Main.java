import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    private static List<Room> rooms = new ArrayList<>();

    public static void main(String[] args) {

        parseInput("./input");

        int sectorSum = 0;
        int northPoleSector = 0;

        for (Room r : rooms) {
            if (r.real) {
                sectorSum += r.sector;
                if (r.northPoleRoom) {
                    northPoleSector = r.sector;
                }
            }
        }
        System.out.printf("Part 1 answer: %d.\n", sectorSum);
        System.out.printf("Part 2 answer: %d.\n", northPoleSector);

    }

    private static void parseInput(String fileName) {
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach((line) -> {
                Room room = new Room(line);
                rooms.add(room);
            });
        } catch (IOException e) {
            System.out.printf("Could not open file '%s\n", fileName);
        }
    }
}

