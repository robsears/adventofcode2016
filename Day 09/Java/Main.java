import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    private static List<Node> nodes = new ArrayList<>();
    private static String input = "";

    public static void main(String[] args) {

        parseInput("./input");
        runPartOne();
        runPartTwo();

    }

    private static void runPartOne() {
        int score = 0;
        for (Node n : nodes) {
            score += (n.x * n.y);
        }
        System.out.printf("Part 1 answer: %d\n", score);
    }

    private static void runPartTwo() {
        Long score = 0L;
        for (Node n: nodes) {
            score += n.score;
        }
        System.out.printf("Part 2 answer: %d\n", score);
    }

    private static void parseInput(String fileName) {
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach( (line) -> input = line);
            int p = 0;
            while (p < input.length()) {
                if (input.charAt(p) == '(') {
                    int e = p+input.substring(p).indexOf(')');
                    String[] sizes = input.substring(p+1,e).replaceAll("[^x0-9]{1,}", "").split("x");
                    Long x = Long.parseLong(sizes[0]);
                    Long y = Long.parseLong(sizes[1]);
                    Node node = new Node(x, y, input.substring(e+1, (int) (e+1+x)));
                    nodes.add(node);
                    p= (int) (e+x+1);
                } else {
                    p++;
                }
            }
        } catch (IOException e) {
            System.out.printf("Could not open file '%s'\n", fileName);
        }
    }
}

