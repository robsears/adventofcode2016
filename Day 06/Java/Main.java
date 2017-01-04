import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    static List<String> data = new ArrayList<>();
    static HashMap<Integer, List<String>> histogram = new HashMap<>();

    public static void main(String[] args) {

        parseInput("./input");
        populateHistogram();
        runPartOne();
        runPartTwo();

    }

    public static void runPartOne() {
        String part1 = "";
        for (Integer pos : histogram.keySet()) {
            part1 += getMostCommon(pos);
        }
        System.out.printf("Part 1 answer: %s\n", part1);
    }

    public static void runPartTwo() {
        String part2 = "";
        for (Integer pos : histogram.keySet()) {
            part2 += getLeastCommon(pos);
        }
        System.out.printf("Part 2 answer: %s\n", part2);
    }

    public static String getLeastCommon(Integer position) {
        List<String> characters = histogram.get(position);
        Collections.sort(characters);

        int charCount = 0;
        String charCheck = characters.get(0);

        int maxChar = Integer.MAX_VALUE;
        String mostCommon = "";

        for (String s : characters) {
            if (charCheck.equals(s)) {
                charCount++;
            } else {
                if (charCount < maxChar) {
                    maxChar = charCount;
                    mostCommon = charCheck;
                }
                charCount = 1;
                charCheck = s;
            }
        }
        if (charCount < maxChar) {
            mostCommon = charCheck;
        }
        return mostCommon;
    }

    public static String getMostCommon(Integer position) {
        List<String> characters = histogram.get(position);
        Collections.sort(characters);

        int charCount = 0;
        String charCheck = characters.get(0);

        int maxChar = 0;
        String mostCommon = "";

        for (String s : characters) {
            if (charCheck.equals(s)) {
                charCount++;
            } else {
                if (charCount > maxChar) {
                    maxChar = charCount;
                    mostCommon = charCheck;
                }
                charCount = 1;
                charCheck = s;
            }
        }
        if (charCount > maxChar) {
            mostCommon = charCheck;
        }
        return mostCommon;
    }

    public static void populateHistogram() {
        for (int i=0; i<8; i++) {
            List<String> characters = new ArrayList<>();
            histogram.put(i, characters);
        }
        for (String line : data) {
            char[] chars = line.toCharArray();
            for (int i=0; i<chars.length; i++) {
                List<String> characters = histogram.get(i);
                characters.add(String.valueOf(chars[i]));
                histogram.put(i, characters);
            }
        }
    }

    public static void parseInput(String fileName) {
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(data::add);
        } catch (IOException e) {
            System.out.printf("Could not read '%s'\n", fileName);
        }
    }
}

