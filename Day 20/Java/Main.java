import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {

        List<Long> allowed = new ArrayList<>();

        List<String> lines = parseInput("./input");

        Collections.sort(lines, (o1, o2) -> {
            String[] range1 = o1.split("-");
            String[] range2 = o2.split("-");
            Long l1 = Long.parseLong(range1[0]);
            Long l2 = Long.parseLong(range2[0]);
            if (l1 == l2) {
                return 0;
            }
            else if (l1 < l2) {
                return -1;
            }
            return 1;
        });

        Long lowerBound = -1L;
        Long upperBound = -1L;
        for (String line : lines) {
            String[] range = line.split("-");
            Long low = Long.parseLong(range[0]);
            Long high = Long.parseLong(range[1]);

            if (lowerBound < 0 && upperBound < 0) {
                lowerBound = low;
                upperBound = high;
            }
            else {
                if (low >= lowerBound && low <= upperBound) {
                    if (high > upperBound) {
                        upperBound = high;
                    }
                } else if (low == upperBound + 1) {
                    if (high > upperBound) {
                        upperBound = high;
                    }
                } else {
                    allowed.add(upperBound + 1);
                    lowerBound = upperBound + 1;
                    upperBound = high;
                }
            }
        }
        System.out.printf("Part 1 answer: Lowest unblocked address: %d\n", allowed.get(0));
        System.out.printf("Part 2 answer: Total unblocked addresses: %d\n", allowed.size());
    }

    public static List<String> parseInput(String fileName) {
        List<String> parsed = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(parsed::add);
        } catch (IOException e) {
            System.out.printf("Could not open file '%s'\n", fileName);
        }
        return parsed;
    }
}

