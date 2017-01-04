import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Traveler {

    public HashMap<String, Integer> location = new HashMap<>();
    public List<String> locations = new ArrayList<>();
    public String direction;
    public boolean secondStop = false;

    public Traveler(String direction) {
        this.location.put("X", 0);
        this.location.put("Y", 0);
        this.direction = direction;
        this.locations.add(getPosition());
    }

    public void move(String op, boolean stopOnRevist) {
        String dir = op.replaceAll("[0-9]", "");
        String pos = op.replaceAll("[A-Z]", "");
        updateDirection(dir);
        updatePosition(pos, stopOnRevist);
    }

    public void updateDirection(String c) {
        switch (direction) {
            case "N":
                direction = (c.equals("R")) ? "E": "W";
                break;
            case "E":
                direction = (c.equals("R")) ? "S": "N";
                break;
            case "S":
                direction = (c.equals("R")) ? "W": "E";
                break;
            case "W":
                direction = (c.equals("R")) ? "N": "S";
                break;
        }
    }

    public void updatePosition(String c, boolean stopOnRevist) {
        Integer distance = Integer.parseInt(String.format("%s", c));
        Integer dir = 1;
        String axis = "";
        switch (direction) {
            case "N":
                axis = "Y";
                dir = 1;
                break;
            case "E":
                axis = "X";
                dir = 1;
                break;
            case "S":
                axis = "Y";
                dir = -1;
                break;
            case "W":
                axis = "X";
                dir = -1;
                break;
        }
        Integer base = location.get(axis);
        int pos = base;
        while (pos != (base + dir * distance)) {
            pos += dir;
            location.put(axis, pos);
            if (beenHere() && stopOnRevist) {
                this.secondStop = true;
                break;
            }
        }
    }

    public String getPosition() {
        return String.format("%d, %d", location.get("X"), location.get("Y"));
    }

    // Figure out how many blocks the traveler is from the starting location of 0,0:
    public int countBlocks() {
        return (int) (Math.abs( (double) location.get("X")) + Math.abs( (double) location.get("Y")));
    }

    // Return true if the traveler has been here before
    public boolean beenHere() {
        String pos = getPosition();
        if (locations.contains(pos)) {
            return true;
        }
        else {
            locations.add(pos);
        }
        return false;
    }
}

