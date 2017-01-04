import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {

    private static HashMap<Integer, Bot> bots = new HashMap<>();
    private static HashMap<Integer, Output> outputs = new HashMap<>();

    public static void main(String[] args) {

        populateArrays();
        boolean partOne = false;
        boolean partTwo = false;

        while (!(partOne && partTwo)) {
            for (int i : bots.keySet()) {
                Bot b = bots.get(i);
                if (b.chips.size() == 2) {
                    if (b.chips.contains(17) && b.chips.contains(61)) {
                        System.out.printf("Part 1 answer: %d\n", b.id);
                        partOne = true;
                    }

                    if (b.lowToBot >= 0) {
                        Bot target = bots.get(b.lowToBot);
                        b.giveLowToBot(target);
                    }

                    if (b.highToBot >= 0) {
                        Bot target = bots.get(b.highToBot);
                        b.giveHighToBot(target);
                    }

                    if (b.lowToOutput >= 0) {
                        Output target = outputs.get(b.lowToOutput);
                        b.giveLowToOutput(target);
                    }

                    if (b.highToOutput >= 0) {
                        Output target = outputs.get(b.highToOutput);
                        b.giveHighToOutput(target);
                    }

                }

                Output o1 = outputs.get(0);
                Output o2 = outputs.get(1);
                Output o3 = outputs.get(2);

                if (o1.chip > 0 && o2.chip > 0 && o3.chip > 0) {
                    Integer part2 = o1.chip * o2.chip * o3.chip;
                    System.out.printf("Part 2 answer: %d\n", part2);
                    partTwo = true;
                }
                if (partOne && partTwo) {break;}
            }
        }
    }

    private static List<String> parseInput(String fileName) {
        List<String> parsed = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach( (line) -> {
                if (runInstruction(line)) {
                    parsed.add(line);
                }
            });
        } catch (IOException e) {
            System.out.printf("Failed to read file '%s'\n", fileName);
        }
        return parsed;
    }

    private static boolean runInstruction(String line) {
        Pattern p = Pattern.compile("^value ([0-9]{1,}) (goes to bot) ([0-9]{1,})");
        Matcher m = p.matcher(line);
        if (m.find()) {
            Bot b;
            int bid = Integer.parseInt(m.group(3));
            int value = Integer.parseInt(m.group(1));
            if (!bots.containsKey(bid)) {
                b = new Bot(bid, value);
                b.directInput = true;
            }
            else {
                b = bots.get(bid);
                b.addChip(value);
                b.directInput = true;
            }
            bots.put(bid, b);
            return false;
        }

        Pattern pp = Pattern.compile(".*?((bot|output) ([0-9]{1,})).*?");
        Matcher mm = pp.matcher(line);
        while (mm.find()) {
            int id = Integer.parseInt(mm.group(3));
            if (mm.group(2).equals("bot")) {
                if (!bots.containsKey(id)) {
                    Bot b = new Bot(id);
                    bots.put(id, b);
                }
            }
            else {
                if (!outputs.containsKey(id)) {
                    Output o = new Output(id);
                    outputs.put(id, o);
                }
            }
        }
        return true;
    }

    private static void populateArrays() {
        List<String> data = parseInput("./input");

        for (String s : data) {
            Pattern p = Pattern.compile("^bot ([0-9]{1,}) gives low to ([a-z]{1,}) ([0-9]{1,}) and high to ([a-z]{1,}) ([0-9]{1,})");
            Matcher m = p.matcher(s);
            if (m.matches()) {
                Bot sourceBot = bots.get(Integer.parseInt(m.group(1)));

                if (m.group(2).equals("bot")) {
                    Bot lowTarget = bots.get(Integer.parseInt(m.group(3)));
                    sourceBot.lowToBot = lowTarget.id;
                    lowTarget.receiveFrom.add(sourceBot.id);
                }
                else {
                    Output lowTarget = outputs.get(Integer.parseInt(m.group(3)));
                    sourceBot.lowToOutput = lowTarget.id;
                    lowTarget.receiveFrom.add(sourceBot.id);
                }
                if (m.group(4).equals("bot")) {
                    Bot highTarget = bots.get(Integer.parseInt(m.group(5)));
                    sourceBot.highToBot = highTarget.id;
                    highTarget.receiveFrom.add(sourceBot.id);
                }
                else {
                    Output highTarget = outputs.get(Integer.parseInt(m.group(5)));
                    sourceBot.highToOutput = highTarget.id;
                    highTarget.receiveFrom.add(sourceBot.id);
                }
            }
        }
    }
}

