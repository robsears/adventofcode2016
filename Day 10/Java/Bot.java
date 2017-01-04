import java.util.ArrayList;
import java.util.List;

public class Bot {

    int id;
    boolean directInput;
    int lowToBot     = -1;
    int highToBot    = -1;
    int lowToOutput  = -1;
    int highToOutput = -1;

    List<Integer> receiveFrom = new ArrayList<>();
    List<Integer> chips       = new ArrayList<>();

    Bot(int id) {
        this.id = id;
    }

    Bot(int id, Integer chip) {
        this.id = id;
        this.addChip(chip);
    }

    void addChip(Integer value) {
        if (!this.chips.contains(value)) {
            this.chips.add(value);
        }
    }

    void giveLowToBot(Bot target) {
        Integer low = getLow();
        target.chips.add(low);
        removeChip(low);
    }

    void giveHighToBot(Bot target) {
        Integer high = getHigh();
        target.chips.add(high);
        removeChip(high);
    }

    void giveLowToOutput(Output target) {
        Integer low = getLow();
        target.chip = low;
        removeChip(low);
    }

    void giveHighToOutput(Output target) {
        Integer high = getHigh();
        target.chip = high;
        removeChip(high);
    }

    @Override
    public String toString() {
        String chipVals = "[";
        for (Integer i : chips) {
            chipVals += "" + i + " ";
        }
        chipVals += "]";
        String recd = "";
        recd += "It received chips from bots ";
        for (Integer i : receiveFrom) {
            recd += "" + i + ", ";
        }
        String sent = String.format("It sent chips to bots %d and %d", lowToBot, highToBot);
        return String.format("Bot %d has %d chips: %s. %s. %s", id, chips.size(), chipVals, sent, recd);
    }


    private void removeChip(Integer value) {
        List<Integer> newChips = new ArrayList<>();
        for (Integer i : chips) {
            if (i != value) {
                newChips.add(i);
            }
        }
        this.chips = newChips;
    }

    private Integer getHigh() {
        if (chips.size() > 1) {
            return ((chips.get(0) > chips.get(1)) ? chips.get(0) : chips.get(1));
        }
        return chips.get(0);
    }

    private Integer getLow() {
        if (chips.size() > 1) {
            return ((chips.get(0) < chips.get(1)) ? chips.get(0) : chips.get(1));
        }
        return chips.get(0);
    }

}

