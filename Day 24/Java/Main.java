import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Main {

    private static Vertex srcVertex = new Vertex("");
    private static List<Vertex> vertexList = new ArrayList<>();
    private static HashMap<String, Vertex> vertices = new HashMap<>();
    private static HashMap<String, Location> locations = new HashMap<>();
    private static List<Location> milestones = new ArrayList<>();
    private static Integer counter;

    public static void main(String[] args) {

        // Read the maze input, find open locations and waypoints:
        parseInput("./input");

        // Generate graph using BFS to find min distance between all nodes:
        buildGraph();

        runPartOne();

        runPartTwo();
    }

    private static void runPartOne(){
        Integer pathLength = run(false);
        System.out.printf("Part 1 answer: %d\n", pathLength);
    }

    private static void runPartTwo(){
        Integer pathLength = run(true);
        System.out.printf("Part 2 answer: %d\n", pathLength);
    }

    private static Integer run (boolean retrn) {
        List<Integer> intList = new ArrayList<Integer>();
        for (Vertex v : vertexList) {
            if (v != srcVertex) {
                intList.add(Integer.parseInt(v.id));
            }
        }
        List<List<Integer>> myLists = listPermutations(intList);

        Integer bestPath = Integer.MAX_VALUE;
        for (List<Integer> al : myLists) {
            int path = 0;
            Vertex last = srcVertex;
            for (Integer i : al) {
                Vertex v = vertices.get(String.format("%d", i));
                Integer dist = last.getDistance(v);
                path += dist;
                last = v;
            }

            if (retrn) {
                Integer dist = last.getDistance(srcVertex);
                path += dist;
            }

            if (path < bestPath) {
                bestPath = path;
            }
        }
        return bestPath;
    }

    private static List<List<Integer>> listPermutations(List<Integer> list) {

        if (list.size() == 0) {
            List<List<Integer>> result = new ArrayList<List<Integer>>();
            result.add(new ArrayList<>());
            return result;
        }

        List<List<Integer>> returnMe = new ArrayList<List<Integer>>();

        Integer firstElement = list.remove(0);

        List<List<Integer>> recursiveReturn = listPermutations(list);
        for (List<Integer> li : recursiveReturn) {

            for (int index = 0; index <= li.size(); index++) {
                List<Integer> temp = new ArrayList<Integer>(li);
                temp.add(index, firstElement);
                returnMe.add(temp);
            }

        }
        return returnMe;
    }

    private static void buildGraph() {
        for (Location l : milestones) {
            Vertex v = new Vertex(l.id);
            vertices.put(l.id, v);
            vertexList.add(v);
        }

        for (int i=0; i<milestones.size();i++) {
            Location l = milestones.get(i);
            Location init = l;
            Vertex vi = vertices.get(init.id);
            for (int j=0; j<milestones.size(); j++) {
                Location m = milestones.get(j);
                Location goal = m;
                Vertex vg = vertices.get(goal.id);

                if (l != m) {

                    for (String key : locations.keySet()) {
                        Location x = locations.get(key);
                        x.distance = Integer.MAX_VALUE;
                        locations.put(key, x);
                    }

                    init = l;
                    goal = m;
                    vg = vertices.get(goal.id);

                    init.distance = 0;
                    Queue<Location> queue = new LinkedList<>();
                    queue.add(init);

                    while (!queue.isEmpty()) {
                        Location current = queue.remove();
                        if (current == goal) {
                            break;
                        }
                        for (Location loc : getNeighbors(current)) {
                            if (loc.distance == Integer.MAX_VALUE) {
                                loc.distance = current.distance + 1;
                                queue.add(loc);
                            }
                        }
                    }


                }
                else {
                    goal.distance = 0;
                }
                Edge edge = new Edge(goal.distance, vi, vg);
                vi.addEdge(edge);
                //System.out.printf("Distance from %s to %s: %d\n", init.toString(), goal.toString(), goal.distance);
            }
        }

        srcVertex = vertices.get("0");
    }

    private static List<Location> getNeighbors(Location pt) {
        List<Location> neighbors = new ArrayList<>();
        String ptl = String.format("%d,%d", pt.posX-1, pt.posY);
        String ptr = String.format("%d,%d", pt.posX+1, pt.posY);
        String ptu = String.format("%d,%d", pt.posX, pt.posY-1);
        String ptd = String.format("%d,%d", pt.posX, pt.posY+1);

        if (locations.containsKey(ptl)) {
            neighbors.add(locations.get(ptl));
        }
        if (locations.containsKey(ptr)) {
            neighbors.add(locations.get(ptr));
        }
        if (locations.containsKey(ptu)) {
            neighbors.add(locations.get(ptu));
        }
        if (locations.containsKey(ptd)) {
            neighbors.add(locations.get(ptd));
        }

        return neighbors;
    }

    private static void parseInput(String fileName) {
        counter = 0;
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach((line) -> {
                readLine(counter, line);
                counter++;
            });
        } catch (IOException e) {
            System.out.printf("Could not read file '%s'\n", fileName);
        }
    }

    private static void readLine(Integer i, String line) {
        char[] c = line.toCharArray();
        for (int j=0; j<c.length; j++) {
            if (c[j] != '#') {
                String id = "";
                boolean isStart = false;
                boolean isDestination = false;
                if (c[j] == '0') {isStart = true; id = "0";}
                if (c[j] != '0' && c[j] != '.') {
                    isDestination = true;
                    id = String.valueOf(c[j]);
                }
                Location l = new Location(j, i, isStart, isDestination, id);
                String key = String.format("%d,%d", j, i);
                locations.put(key, l);
                if (isDestination || isStart) {
                    milestones.add(l);
                }
            }
        }
    }
}

