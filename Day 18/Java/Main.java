public class Main {

    private static String input = "^^.^..^.....^..^..^^...^^.^....^^^.^.^^....^.^^^...^^^^.^^^^.^..^^^^.^^.^.^.^.^.^^...^^..^^^..^.^^^^";

    public static void main(String[] args) {

        runPartOne();
        runPartTwo();

    }

    private static void runPartOne() {
        int answer = run(40);
        System.out.printf("Part 1 answer: %d\n", answer);
    }

    private static void runPartTwo() {
        int answer = run(40000);
        System.out.printf("Part 2 answer: %d\n", answer);
    }

    private static int run(int rows) {
        String thisLine = input;
        int safeTiles = countInitialSafeTiles();
        for (int j=0; j<(rows-1); j++) {
            String nextLine = "";
            char[] c = thisLine.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (checkTrap(i, thisLine)) {
                    nextLine += "^";
                }
                else {
                    nextLine += ".";
                    safeTiles++;
                }
            }
            thisLine = nextLine;
        }
        return safeTiles;
    }


    private static int countInitialSafeTiles() {
        int safe = 0;
        for (char c : input.toCharArray()) {
            if (c == '.') {
                safe++;
            }
        }
        return safe;
    }


//    A new tile is a trap only in one of the following situations:
//
//    1. Its left and center tiles are traps, but its right tile is not.
//    2. Its center and right tiles are traps, but its left tile is not.
//    3. Only its left tile is a trap.
//    4. Only its right tile is a trap.

    private static boolean checkTrap(int i, String thisLine) {

        String tile = ".";

        String left = "";
        boolean leftSafe = true;

        String right = "";
        boolean rightSafe = true;

        String center = thisLine.substring(i,i+1);
        boolean centerSafe = (center.equals("."));

        if (i == 0) {
            leftSafe = true;
            right = thisLine.substring(i+1, i+2);
            rightSafe = (right.equals("."));
        }
        if (i >= thisLine.length()-1) {
            rightSafe = true;
            left = thisLine.substring(i-1, i);
            leftSafe = (left.equals("."));
        }
        if (i > 0 && i < thisLine.length()-1) {
            left = thisLine.substring(i-1, i);
            right = thisLine.substring(i+1, i+2);

            leftSafe = (left.equals("."));
            rightSafe = (right.equals("."));
        }

        // Rule 1:
        if (!leftSafe && !centerSafe && rightSafe) {
            tile = "^";
        }
        // Rule 2:
        else if (leftSafe && !centerSafe && !rightSafe) {
            tile = "^";
        }

        // Rule 3:
        else if (!leftSafe && centerSafe && rightSafe) {
            tile = "^";
        }

        // Rule 4:
        else if (leftSafe && centerSafe && !rightSafe) {
            tile = "^";
        }

        return tile.equals("^");
    }
}

