import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    public static String password;
    public static boolean reverse;

    public static void main(String[] args) {

        runPartOne("abcdefgh");
        runPartTwo("fbgdceah");

    }

    public static void runPartOne(String pw) {
        run(false, pw);
        System.out.printf("Part 1 answer: %s\n", password);
    }

    public static void runPartTwo(String pw) {
        run(true, pw);
        System.out.printf("Part 2 answer: %s\n", password);
    }

    public static void run(boolean rev, String pw) {
        reverse = rev;
        password = pw;
        List<String> lines = parseInput("./input");
        if (reverse) {
            Collections.reverse(lines);
        }
        for (String s : lines) {
            runOp(s);
        }
    }

    public static void runSwapByPosition(String line) {
        String newPassword = "";
        char[] oldPass = password.toCharArray();
        String[] params = line.split(" ");
        Integer a = Integer.parseInt(params[2]);
        Integer b = Integer.parseInt(params[5]);

        for (int i=0; i<oldPass.length; i++) {
            if (i == a) {
                newPassword += oldPass[b];
            }
            else if (i == b) {
                newPassword += oldPass[a];
            }
            else {
                newPassword += oldPass[i];
            }
        }
        password = newPassword;
    }

    // swap letter X with letter Y means that the letters X and Y should be
    // swapped (regardless of where they appear in the string).
    public static void runSwapByLetter(String line) {
        String newPassword = "";
        char[] oldPass = password.toCharArray();
        String[] params = line.split(" ");
        Integer a = password.indexOf(params[2]);
        Integer b = password.indexOf(params[5]);
        for (int i=0; i<oldPass.length; i++) {
            if (i == a) {
                newPassword += oldPass[b];
            }
            else if (i == b) {
                newPassword += oldPass[a];
            }
            else {
                newPassword += oldPass[i];
            }
        }
        password = newPassword;
    }

    // swap letter X with letter Y means that the letters X and Y should be
    // swapped (regardless of where they appear in the string).
    public static void runSwapByLetterRev(String line) {
        String newPassword = "";
        char[] oldPass = password.toCharArray();
        String[] params = line.split(" ");
        Integer a = password.indexOf(params[5]);
        Integer b = password.indexOf(params[2]);
        for (int i=0; i<oldPass.length; i++) {
            if (i == a) {
                newPassword += oldPass[b];
            }
            else if (i == b) {
                newPassword += oldPass[a];
            }
            else {
                newPassword += oldPass[i];
            }
        }
        password = newPassword;
    }

    // rotate left/right X steps means that the whole string should be rotated;
    // for example, one right rotation would turn abcd into dabc
    public static void runRotateByDirection(String line) {
        String newPassword = password;
        String[] params = line.split(" ");
        Integer x = Integer.parseInt(params[2]);
        if (params[1].equals("left")) {
            for (int i=0; i<x; i++) {
                newPassword = newPassword.substring(1,newPassword.length()) + newPassword.substring(0,1);
            }
        }
        if (params[1].equals("right")) {
            for (int i=0; i<x; i++) {
                newPassword = newPassword.substring(newPassword.length()-1) + newPassword.substring(0,newPassword.length()-1);
            }
        }
        password = newPassword;
    }

    // rotate left/right X steps means that the whole string should be rotated;
    // for example, one right rotation would turn abcd into dabc
    public static void runRotateByDirectionRev(String line) {
        String newPassword = password;
        String[] params = line.split(" ");
        Integer x = Integer.parseInt(params[2]);
        if (params[1].equals("right")) {
            for (int i=0; i<x; i++) {
                newPassword = newPassword.substring(1,newPassword.length()) + newPassword.substring(0,1);
            }
        }
        if (params[1].equals("left")) {
            for (int i=0; i<x; i++) {
                newPassword = newPassword.substring(newPassword.length()-1) + newPassword.substring(0,newPassword.length()-1);
            }
        }
        password = newPassword;
    }

    // rotate based on position of letter X means that the whole string should be rotated to the right
    // based on the index of letter X (counting from 0) as determined before this instruction does
    // any rotations. Once the index is determined, rotate the string to the right one time, plus a
    // number of times equal to that index, plus one additional time if the index was at least 4
    public static void runRotateByLetter(String line) {
        String newPassword = password;
        String[] params = line.split(" ");
        Integer x = 1 + password.indexOf(params[6]);
        if (password.indexOf(params[6]) >= 4) {
            x++;
        }
        for (int i=0; i<x; i++) {
            newPassword = newPassword.substring(newPassword.length()-1) + newPassword.substring(0,newPassword.length()-1);
        }
        password = newPassword;
    }

    // rotate based on position of letter X means that the whole string should be rotated to the right
    // based on the index of letter X (counting from 0) as determined before this instruction does
    // any rotations. Once the index is determined, rotate the string to the right one time, plus a
    // number of times equal to that index, plus one additional time if the index was at least 4
    public static void runRotateByLetterRev(String line) {
        String newPassword = password;
        String[] params = line.split(" ");
        Integer x = password.indexOf(params[6]);
        Integer y=0;
        switch(x) {
            case 1:
                y = 0;
                break;
            case 3:
                y = 1;
                break;
            case 5:
                y = 2;
                break;
            case 7:
                y = 3;
                break;
            case 2:
                y = 4;
                break;
            case 4:
                y = 5;
                break;
            case 6:
                y = 6;
                break;
            case 0:
                y = 7;
                break;
        }

        if (y > x) {
            // Move right y-x steps
            for (int i=0; i<(y-x); i++) {
                newPassword = newPassword.substring(newPassword.length()-1) + newPassword.substring(0,newPassword.length()-1);
            }
        }
        else {
            // Move left x-y steps
            for (int i=0; i<(x-y); i++) {
                newPassword = newPassword.substring(1,newPassword.length()) + newPassword.substring(0,1);
            }
        }
        password = newPassword;
    }

    // reverse positions X through Y means that the span of letters at indexes X through Y (including the letters
    // at X and Y) should be reversed in order
    public static void runReverse(String line) {
        String newPassword = "";
        String[] params = line.split(" ");
        Integer x = Integer.parseInt(params[2]);
        Integer y = Integer.parseInt(params[4]);
        int max = (y < password.length()-1) ? y+1 : password.length();
        String toReverse = "";
        char[] revPass = password.substring(x, max).toCharArray();
        for (int i=revPass.length-1; i>=0; i--) {
            toReverse += revPass[i];
        }
        newPassword = password.substring(0,x)+toReverse+password.substring(max);
        password = newPassword;
    }

    // move position X to position Y means that the letter which is at index X should be removed from
    // the string, then inserted such that it ends up at index Y.
    public static void runMove(String line) {
        String newPassword = "";
        char[] chrpass = password.toCharArray();
        String[] params = line.split(" ");
        Integer x = Integer.parseInt(params[2]);
        Integer y = Integer.parseInt(params[5]);
        int max = (y+1 < password.length()-1) ? y+1 : password.length()-1;
        char z = password.charAt(x);
        for (int i=0; i<chrpass.length;i++) {
            if (i != x) {
                newPassword += chrpass[i];
            }
        }
        char[] updated = newPassword.toCharArray();
        newPassword = "";
        for (int i=0; i<=updated.length; i++) {
            if (i == y) {
                newPassword += z;
            }
            if (i != updated.length) {
                newPassword += updated[i];
            }
        }
        password = newPassword;
    }

    // move position X to position Y means that the letter which is at index X should be removed from
    // the string, then inserted such that it ends up at index Y.
    public static void runMoveRev(String line) {
        String newPassword = "";
        char[] chrpass = password.toCharArray();
        String[] params = line.split(" ");
        Integer x = Integer.parseInt(params[5]);
        Integer y = Integer.parseInt(params[2]);
        int max = (y+1 < password.length()-1) ? y+1 : password.length()-1;
        char z = password.charAt(x);
        for (int i=0; i<chrpass.length;i++) {
            if (i != x) {
                newPassword += chrpass[i];
            }
        }
        char[] updated = newPassword.toCharArray();
        newPassword = "";
        for (int i=0; i<=updated.length; i++) {
            if (i == y) {
                newPassword += z;
            }
            if (i != updated.length) {
                newPassword += updated[i];
            }
        }
        password = newPassword;
    }

    public static void runOp(String line) {
        String[] parts = line.split(" ");
        switch (parts[0]) {
            case "swap":
                if (parts[1].equals("position")) {
                    if (reverse) {
                        runSwapByPosition(line);
                    }
                    else {
                        runSwapByPosition(line);
                    }
                }
                else {
                    if (reverse) {
                        runSwapByLetterRev(line);
                    }
                    else {
                        runSwapByLetter(line);
                    }
                }
                break;
            case "rotate":
            if (parts[1].equals("left") || parts[1].equals("right")) {
                    if (reverse) {
                        runRotateByDirectionRev(line);
                    }else  {
                        runRotateByDirection(line);
                    }
                }
                else {
                    if (reverse) {
                        runRotateByLetterRev(line);
                    }
                    else {
                        runRotateByLetter(line);
                    }
                }
            break;
            case "reverse":
                if (reverse) {
                    runReverse(line);
                } else {
                    runReverse(line);
                }
                break;
            case "move":
                if (reverse) {
                    runMoveRev(line);
                }
                else {
                    runMove(line);
                }
                break;
        }
    }

    public static List<String> parseInput(String fileName) {
        List<String> parsed = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(parsed::add);
        } catch (IOException e) {
            System.out.printf("Could not read file '%s'\n", fileName);
        }
        return parsed;
    }

}

