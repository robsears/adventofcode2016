import java.util.ArrayList;
import java.util.List;

public class Main {

    private static List<Disc> discs = new ArrayList<>();

    public static void main(String[] args) {

        runPartOne();
        runPartTwo();

    }

    private static void runPartOne() {

        // Create the discs:
        Disc disc1 = new Disc(17, 5);
        Disc disc2 = new Disc(19, 8);
        Disc disc3 = new Disc(7,  1);
        Disc disc4 = new Disc(13, 7);
        Disc disc5 = new Disc(5,  1);
        Disc disc6 = new Disc(3,  0);

        // Add to an array:
        discs.add(disc1);
        discs.add(disc2);
        discs.add(disc3);
        discs.add(disc4);
        discs.add(disc5);
        discs.add(disc6);

        int answer = run();
        System.out.printf("Part 1 answer: %d\n", answer);
    }

    private static void runPartTwo() {
        // Part 1 discs are still in array...
        // Disc for part 2:
        Disc disc7 = new Disc(11, 0);
        discs.add(disc7);

        int answer = run();
        System.out.printf("Part 2 answer: %d\n", answer);
    }

    private static int run() {
        int t=0;
        while (true) {
            int c=0;
            for (int i=0; i<discs.size(); i++) {
                Disc d = discs.get(i);
                c += (t+(i+1)+d.initialPosition) % d.positions;
            }
            if (c == 0) {
                break;
            }
            t++;
        }
        return t;
    }

}

