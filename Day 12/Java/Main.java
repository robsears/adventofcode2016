import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Main {

    private static int counter = 0;
    private static HashMap<String, Integer> registers = new HashMap<>();

    public static void main(String[] args) {

        Integer[] cvals = {0, 1};

        for (Integer c : cvals) {
            List<String> instructions = parseInput("./input");
            registers.put("c", c);
            counter = 0;
            while (counter < instructions.size()) {
                String op = instructions.get(counter);
                runOp(op);
            }
            System.out.printf("When register 'c' is initialized as %d, the value remaining in register 'a' is: %s.\n", c, registers.get("a"));
            registers.clear();
        }

    }

    private static void runOp(String op) {
        String[] parts = op.split(" ");
        List<String> params = getParams(op);
        switch (parts[0]) {
            case "cpy":
                cpyOp(params);
                break;
            case "inc":
                incOp(params);
                break;
            case "dec":
                decOp(params);
                break;
            case "jnz":
                jnzOp(params);
                break;
        }
        counter++;
    }

    private static void cpyOp(List<String> params) {
        Integer src;
        if (isNumeric(params.get(0))) {
            src = Integer.parseInt(params.get(0));
        }
        else {
            src = registers.get(params.get(0));
        }
        registers.put(params.get(1), src);
    }


    private static void incOp(List<String> params) {
        int a = registers.get(params.get(0));
        a++;
        registers.put(params.get(0), a);
    }

    private static void decOp(List<String> params) {
        int a = registers.get(params.get(0));
        a--;
        registers.put(params.get(0), a);
    }

    private static void jnzOp(List<String> params) {
        int a;
        if (isNumeric(params.get(0))) {
            a = Integer.parseInt(params.get(0));
        }
        else {
            a = registers.get(params.get(0));
        }
        if (a != 0) {
            counter += Integer.parseInt(params.get(1))-1;
        }
    }

    private static List<String> getParams(String op) {
        List<String> params = new ArrayList<>();
        String[] parts = op.split(" ");
        params.add(parts[1]);
        if (parts.length > 2) {
            params.add(parts[2]);
        }
        return params;
    }

    private static List<String> parseInput(String fileName) {
        List<String> parsed = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach( (line) -> {
                initializeRegisters(line);
                parsed.add(line);
            });
        } catch (IOException e) {
            System.out.printf("Could not read file '%s'\n", fileName);
        }
        return parsed;
    }

    private static void initializeRegisters(String line) {
        String[] parts = line.split(" ");
        if (isNumeric(parts[1])) {
            Set<String> registerNames = registers.keySet();
            if (!registerNames.contains(parts[1])) {
                registers.put(parts[1], 0);
            }
        }
    }

    private static boolean isNumeric(String param) {
        try {
            Integer.parseInt(param);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

