import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static String passcode = "udskfozm";
    private static HashMap<String, Location> locations = new HashMap<>();

    public static void main(String[] args) {

        initialize();
        runPartOne();
        runPartTwo();

    }

    private static void runPartOne() {
        Location l = bfs(false);
        System.out.printf("Part 1 answer: %s\n", l.path);
    }

    private static void runPartTwo() {
        Location l = bfs(true);
        System.out.printf("Part 2 answer: %s\n", l.distance);
    }

    private static Location bfs(boolean longest) {

        Location initial = locations.get("11");
        Location finalLoc = locations.get("44");

        Queue<Location> queue = new LinkedList<>();
        queue.add(initial);

        while (!queue.isEmpty()) {
            Location current = queue.remove();
            HashMap<String, Location> options = getMoves(current);
            for (String s : options.keySet()) {
                Location l = options.get(s);
                if (l.posX != 4 || l.posY != 4) {
                    queue.add(l);
                }
                else {
                    finalLoc = l;
                    if (!longest) {
                        return finalLoc;
                    }
                }
            }
        }

        return finalLoc;
    }

    private static void initialize() {
        for (int y=1; y<= 4; y++) {
            for (int x=1; x<=4; x++) {
                String key = String.format("%d%d", x, y);
                locations.put(key, new Location(x, y));
            }
        }
    }

    // Return all possible moves
    private static HashMap<String, Location> getMoves(Location location) {
        HashMap<String, Location> moves = new HashMap<>();
        String toHash = passcode + location.path;
        char[] hash = md5(toHash).substring(0,4).toCharArray();
        for (int i=0; i<hash.length; i++) {
            if (isOpen(hash[i])) {
                String key;
                if (location.posY > 1 && i == 0) {
                    // Up
                    key = String.format("%d%d", location.posX, location.posY-1);
                    Location loc = new Location(location.posX, location.posY-1);
                    loc.path = location.path + "U";
                    loc.distance = location.distance+1;
                    moves.put("U", loc);
                }
                if (location.posY < 4 && i == 1) {
                    // Down
                    key = String.format("%d%d", location.posX, location.posY+1);
                    Location loc = new Location(location.posX, location.posY+1);
                    loc.path = location.path + "D";
                    loc.distance = location.distance+1;
                    moves.put("D", loc);
                }
                if (location.posX > 1 && i == 2) {
                    // Left
                    key = String.format("%d%d", location.posX-1, location.posY);
                    Location loc = new Location(location.posX-1, location.posY);
                    loc.path = location.path + "L";
                    loc.distance = location.distance+1;
                    moves.put("L", loc);
                }
                if (location.posX < 4 && i == 3) {
                    // Right
                    key = String.format("%d%d", location.posX, location.posY);
                    Location loc = new Location(location.posX+1, location.posY);
                    loc.path = location.path + "R";
                    loc.distance = location.distance+1;
                    moves.put("R", loc);
                }
            }
        }
        return moves;
    }

    public static String md5(String text) {
        String hashtext = null;
        try
        {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(text.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            hashtext = bigInt.toString(16);
            while(hashtext.length() < 32 ){
                hashtext = "0"+hashtext;
            }
        } catch (Exception e1) {
            System.out.printf("Could not compute hash for %s.\n", text);
        }
        return hashtext;
    }

    private static boolean isOpen(char c) {
        String s = String.valueOf(c);
        Pattern p = Pattern.compile("[b-f]{1}");
        Matcher m = p.matcher(s);
        return m.matches();
    }

}

