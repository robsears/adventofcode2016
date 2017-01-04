import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {

        List<String> data        = parseInput("./input");
        List<String> newData     = rotateData(data);


        List<Triangle> triangles1 = countValid(data);
        List<Triangle> triangles2 = countValid(newData);

        System.out.printf("Part 1: There are %d valid triangles.\n", triangles1.size());
        System.out.printf("Part 2: There are %d valid triangles.\n", triangles2.size());

    }

    public static List<String> rotateData(List<String> data) {
        int z=0;
        List<String> rotated  = new ArrayList<>();
        List<String> chunks   = new ArrayList<>();
        for (String line : data) {
            chunks.add(line);
            if (chunks.size() == 3) {
                for (String newLine : flipChunks(chunks)) {
                    rotated.add(newLine);
                    Triangle t = new Triangle(newLine.split("_"));
                    if (t.isPossible) {z++;}
                }
                chunks.clear();
            }
        }
        return rotated;
    }

    public static List<String> flipChunks(List<String> chunks) {
        List<String> parts    = new ArrayList<>();
        List<String> rotated  = new ArrayList<>();
        for (String line : chunks) {
            for (String part : line.split("_")) {
                if (!part.isEmpty()) {
                    parts.add(part);
                }
            }
        }
        for (int i=0; i<3; i++) {
            String flippedLine = String.format("%s_%s_%s",
                    parts.get(i),
                    parts.get(i+3),
                    parts.get(i+6)
            );
            rotated.add(flippedLine);
        }

        return rotated;
    }

    public static List<Triangle> countValid(List<String> data) {
        List<Triangle> valid = new ArrayList<>();
        for (String line : data) {
            String[] legs = line.split("_");
            Triangle t = new Triangle(legs);
            if (t.isPossible) {
                valid.add(t);
            }
        }
        return valid;
    }

    public static List<String> parseInput(String fileName) {
        List<String> parsed = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach((line) -> {
                String cleaned = line.replaceAll("[\\s|\\u00A0]+","_").replaceAll("^_","");
                parsed.add(cleaned);
            });
        }
        catch (IOException e) {
            System.out.printf("Error reading file '%s'\n", fileName);
        }
        return parsed;
    }
}

