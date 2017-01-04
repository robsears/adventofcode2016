import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;


class ButtonPad {

    private HashMap<String, Button> pad = new HashMap<>();
    Button current;

    ButtonPad(String keypad) {
        List<String> lines = parseInput(keypad);
        int x=0;
        int y=0;
        for (String line : lines) {
            for (char c : line.toCharArray()) {
                if (c != ' ') {
                    Button b = new Button(x, y, String.format("%s", c));
                    pad.put(String.format("%d%d", x, y), b);
                }
                x++;
            }
            x=0;
            y++;
        }
        this.current = getButtonByValue("5");
    }

    private Button getButtonByValue(String val) {
        Button b = null;
        for (String key : pad.keySet()) {
            b = pad.get(key);
            if (b.value.equals(val)) {
                break;
            }
        }
        return b;
    }

    private void setCurrent(Button b) {
        this.current = b;
    }

    void move(char c) {

        Integer x = current.posX;
        Integer y = current.posY;

        switch (c) {
            case 'U':
                y--;
                break;
            case 'D':
                y++;
                break;
            case 'L':
                x--;
                break;
            case 'R':
                x++;
                break;
        }

        String newPos = String.format("%d%d", x, y);
        if (pad.containsKey(newPos)) {
            setCurrent(pad.get(newPos));
        }

    }

    private static List<String> parseInput(String fileName) {
        List<String> parsed = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(parsed::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsed;
    }
}

