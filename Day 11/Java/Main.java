import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {

    // Draw the optimal solutions?
    private static boolean debug = false;

    public static void main(String[] args) {

        String[] parts = {"part1", "part2"};
        for (String part : parts) {
            State initial = parseInput("./" + part);
            State finalState = bfs(initial);
            if (debug) {
                drawSolutions(finalState);
            }
            System.out.printf("Minimum steps for %s: %d\n", part, finalState.distance);
        }

    }

    private static State bfs(State initial) {

        initial.distance = 0;
        int dist = 0;

        List<String> visited = new ArrayList<>();
        PriorityQueue<State> queue = new PriorityQueue<State>(10, (x, y) -> x.closeness - y.closeness);
        queue.add(initial);

        while (!queue.isEmpty()) {
            State current = queue.remove();
            if (!visited.contains(current.representation)) {
                visited.add(current.representation);
                for (State state : current.allowedStates()) {
                    if (state.distance == Integer.MAX_VALUE) {
                        state.distance = current.distance + 1;
                        state.parent = current;
                        queue.add(state);
                    }
                    if (state.solution) {
                        return state;
                    }
                }
                if (current.distance+1 > dist) {
                    dist = current.distance+1;
                }
            }
        }

        // Couldn't find anything...
        return initial;
    }

    private static void drawSolutions(State solution) {

        List<State> path = new ArrayList<>();
        path.add(solution);
        while (solution.parent != null) {
            solution = solution.parent;
            path.add(solution);
        }
        for (int i=path.size()-1; i>=0; i--) {
            path.get(i).draw();
        }
    }

    private static State parseInput(String fileName) {
        List<String> parsed = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(parsed::add);
        } catch(IOException e) {
            System.out.printf("Failed to open file '%s'\n", fileName);
        }
        HashMap<Integer, List<Item>> floors = new HashMap<>();
        int floor = 1;
        for (String s : parsed) {
            floors.put(floor, extract(s, floor));
            floor++;
        }
        return new State(null, floors, 1);
    }

    private static List<Item> extract(String line, Integer floor) {
        List<String> items = new ArrayList<>();
        HashMap<Integer, List<Item>> floors = new HashMap<>();

        line = line.replaceAll(" and ", ", ");
        Pattern p = Pattern.compile(".*?(a ([\\ \\-_a-z]{1,})[\\.,]{0}( and){0,}).*?");
        Matcher m = p.matcher(line);
        while(m.find()) {
            String thing = m.group(2);
            items.add(thing);
        }

        List<Item> floorItems = new ArrayList<>();
        if (!floors.containsKey(floor)) {
            floors.put(floor, floorItems);
        } else {
            floorItems = floors.get(floor);
        }

        for (String s : items) {
            int init = s.length();
            String chem = s.replaceAll("-compatible microchip","");
            if (chem.length() < init) {
                Item mc = new Item(floor, chem, "M");
                floorItems.add(mc);
            }
            init = s.length();
            chem = s.replaceAll(" generator","");
            if (chem.length() < init) {
                Item g = new Item(floor, chem, "G");
                floorItems.add(g);
            }
        }

        return floorItems;
    }
}

