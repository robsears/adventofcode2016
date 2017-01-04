import java.util.*;

public class State implements Comparable<State> {

    State parent;
    Integer distance = Integer.MAX_VALUE;
    Integer closeness = Integer.MAX_VALUE;
    String representation;
    boolean solution = false;
    private Integer elevatorPos;
    private HashMap<Integer, List<Item>> floors;

    State(State parent, HashMap<Integer, List<Item>> floors, Integer elevatorPos) {
        this.parent = parent;
        this.elevatorPos = elevatorPos;
        this.floors = floors;
        this.representation = stringify();
        this.solution = isSolution();
        this.closeness = getCloseness();
    }

    public int compareTo(State s) {
        int lastCmp = closeness.compareTo(s.closeness);
        return (lastCmp != 0 ? lastCmp : closeness.compareTo(s.closeness));
    }

    // Return a list of all allowable states
    List<State> allowedStates() {

        List<State> allowable = new ArrayList<>();

        // Get a list of possible floors we can travel to:
        List<Integer> possibleFloors = possibleFloors();

        // Get a list of all things we're able to move:
        List<Item> onThisFloor = floors.get(elevatorPos);

        // Get all possible combinations
        HashMap<Integer, List<List<Item>>> movingOptions = getMovingOptions(possibleFloors, onThisFloor);

        // Iterate over the possible combinations and determine which are acceptable:
        for (Integer floor : movingOptions.keySet()) {
            for (List<Item> li : movingOptions.get(floor)) {
                if (legalMove(floor, li)) {

                    // Cool, this move is legal, let's create a state:
                    HashMap<Integer, List<Item>> allowedFloors = new HashMap<>();
                    for (Item i : allItems()) {

                        Integer newFloor = 0;
                        if (li.contains(i)) {
                            newFloor = floor;
                        }
                        else {
                            newFloor = i.floor;
                        }

                        List<Item> thisFloor = new ArrayList<>();
                        if (allowedFloors.keySet().contains(newFloor)) {
                            thisFloor = allowedFloors.get(newFloor);
                        }

                        thisFloor.add(i);
                        allowedFloors.put(newFloor, thisFloor);
                    }

                    State s = new State(this, allowedFloors, floor);
                    if (this.parent != null) {
                        if (!s.representation.equals(this.parent.representation)) {
                            allowable = addIfUniq(allowable, s);
                        }
                    }
                    else {
                        allowable = addIfUniq(allowable, s);
                    }
                }
            }
        }

        Collections.sort(allowable);
        return allowable;

    }

    // Draw the system state
    void draw() {
        for (int f=4; f>=1;f--) {
            System.out.printf("F%d ", f);
            if (f == elevatorPos) {
                System.out.printf("E ");
            }
            else {
                System.out.printf(". ");
            }
            for (Item i : allItems()) {
                if (i.floor == f) {
                    System.out.printf("%s ", i.name);
                }
                else {
                    System.out.printf("... ");
                }
            }
            System.out.printf("\n");
        }
        System.out.printf("\n");

    }

    private Integer getCloseness() {
        Integer closeness = 0;
        for (int i=1;i<=4;i++) {
            if (this.floors.get(i) != null) {
                for (Item item : this.floors.get(i)) {
                    closeness += (4 - i);
                }
            }
        }
        return closeness;
    }

    private List<State> addIfUniq(List<State> current, State state) {
        List<String> reps = new ArrayList<>();
        for (State s : current) {
            reps.add(s.representation);
        }
        if (!reps.contains(state.representation) && !state.representation.equals(this.representation)) {
            current.add(state);
        }
        return current;
    }

    // Return true if move is legal
    private boolean legalMove(Integer destinationFloor, List<Item> itemsBrought) {

        // Figure out everything that will be on the destination floor:
        List<Item> proposedList = new ArrayList<>();
        List<Item> microchips = new ArrayList<>();
        List<Item> generators = new ArrayList<>();
        List<Item> thisFloor = this.floors.get(destinationFloor);

        if (thisFloor != null) {
            for (Item i : thisFloor) {
                if (i.type.equals("G")) {
                    generators.add(i);
                }
                if (i.type.equals("M")) {
                    microchips.add(i);
                }
                proposedList.add(i);
            }
            for (Item i : itemsBrought) {
                if (i.type.equals("G")) {
                    generators.add(i);
                }
                if (i.type.equals("M")) {
                    microchips.add(i);
                }
                proposedList.add(i);
            }

            // Quick heuristic:
            if (generators.size() == 0 || microchips.size() == 0) {
                return true;
            }

            // Remove items that neutralize each other:
            for (Item g : generators) {
                for (Item m : microchips) {
                    if (m.chem.equals(g.chem)) {
                        proposedList.remove(m);
                        proposedList.remove(g);
                    }
                }
            }

            // All items have neutralized each other
            if (proposedList.size() == 0) {
                return true;
            }

            // Check compatibility of remaining items:
            boolean compatible = true;
            for (int i = 0; i < proposedList.size(); i++) {
                for (int j = i; j < proposedList.size(); j++) {
                    if (i != j) {
                        Item one = proposedList.get(i);
                        Item two = proposedList.get(j);
                        compatible = compatible && one.compatibleWith(two);
                    }
                }
            }
            return compatible;
        }
        return true;
    }

    private HashMap<Integer, List<List<Item>>> getMovingOptions(List<Integer> possibleFloors, List<Item> onThisFloor) {
        HashMap<Integer, List<List<Item>>> movingOptions = new HashMap<>();
        for (Integer floor : possibleFloors) {
            List<List<Item>> moving = new ArrayList<>();
            for (int i=0; i<onThisFloor.size(); i++) {
                for (int j=i; j<onThisFloor.size(); j++) {
                    List<Item> toMove = new ArrayList<>();
                    if (i == j) {
                        toMove.add(onThisFloor.get(i));
                    }
                    else {
                        toMove.add(onThisFloor.get(i));
                        toMove.add(onThisFloor.get(j));
                    }
                    moving.add(toMove);
                    movingOptions.put(floor, moving);
                }
            }
        }
        return movingOptions;
    }

    private List<Integer> possibleFloors() {
        List<Integer> possibleFloors = new ArrayList<>();
        if (elevatorPos == 1) {
            possibleFloors.add(2);
        }
        else if (elevatorPos == 4) {
            possibleFloors.add(3);
        }
        else {
            possibleFloors.add(elevatorPos - 1);
            possibleFloors.add(elevatorPos + 1);
        }
        return possibleFloors;
    }

    // Convert the state of the system into a string for easy comparison
    private String stringify() {
        String stringy = "";
        for (int f=1; f<=4;f++) {
            if (f == elevatorPos) {
                stringy += "1";
            }
            else {
                stringy += "0";
            }
            for (Item i : allItems()) {
                if (i.floor == f) {
                    stringy += "1";
                }
                else {
                    stringy += "0";
                }
            }
            //stringy += "\n";
        }
        return stringy;
    }

    // Return a list of all items in the system
    private List<Item> allItems() {
        List<Item> allItems = new ArrayList<>();
        for (Integer f : this.floors.keySet()) {
            for (Item i : this.floors.get(f)) {
                i.floor = f;
                allItems.add(i);
            }
        }
        Collections.sort(allItems);
        return allItems;
    }

    private boolean isSolution() {
        String rep = "";
        for (int i=1;i<=4;i++) {
            for (int j=0; j<allItems().size()+1; j++) {
                if (i < 4) {
                    rep += "0";
                }
                else {
                    rep += "1";
                }
            }
        }
        return rep.equals(this.representation);
    }

}

