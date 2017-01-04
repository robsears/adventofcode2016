import java.util.HashMap;

class Screen {

    boolean debug = false;
    private int length;
    private int height;
    private HashMap<String, Pixel> pixels = new HashMap<>();

    Screen(int length, int height) {
        this.length = length;
        this.height = height;
        for (int y=0; y<height; y++) {
            for (int x=0; x<length; x++) {
                String pos = "" + x + "," + y + "";
                Pixel pixel = new Pixel(false);
                pixels.put(pos, pixel);
            }
        }
    }

    void printScreen() {
        if (this.debug) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < length; x++) {
                    String pos = "" + x + "," + y + "";
                    if (pixels.get(pos).state) {
                        System.out.printf("#");
                    } else {
                        System.out.printf(" ");
                    }
                }
                System.out.printf("\n");
            }
        }
    }

    void performOperation(String op) {
        String[] opParts = identifyOp(op);
        switch (opParts[0]) {
            case "rect":
                performRect(opParts);
                break;
            case "rotate":
                performRotate(opParts);
                break;
        }
    }

    private void performRect(String[] opParts) {
        String[] size = opParts[1].split("x");
        Integer length = Integer.parseInt(size[0]);
        Integer height = Integer.parseInt(size[1]);
        for (int y=0; y<height; y++) {
            for (int x=0; x<length; x++) {
                String pos = "" + x + "," + y + "";
                this.pixels.get(pos).turnOn();
            }
        }
        printScreen();
    }

    private void performRotate(String[] opParts) {
        String[] parts = opParts[2].split("=");
        if (opParts[1].equals("row")) {
            shiftRow(Integer.parseInt(parts[1]), Integer.parseInt(opParts[4]));
        }
        else if (opParts[1].equals("column")) {
            shiftCol(Integer.parseInt(parts[1]), Integer.parseInt(opParts[4]));
        }
    }

    private void shiftRow(Integer row, Integer amt) {
        for (int i=0; i<amt; i++) {
            String bl = getRowBits(row);
            String newBl = bl.substring(bl.length()-1) + bl.substring(0,bl.length()-1);
            setRowBits(row, newBl);
        }
        printScreen();
    }

    private void shiftCol(Integer col, Integer amt) {
        for (int i=0; i<amt; i++) {
            String bl = getColBits(col);
            String newBl = bl.substring(bl.length()-1) + bl.substring(0,bl.length()-1);
            setColBits(col, newBl);
        }
        printScreen();
    }

    private String[] identifyOp(String op) {
        return op.split(" ");
    }


    private String getRowBits(Integer row) {
        String bitlist = "";
        for (int x=0; x<length; x++) {
            Pixel p = pixels.get(""+x+","+row+"");
            bitlist += (p.state) ?  1 : 0;
        }
        return bitlist;
    }

    private void setRowBits(Integer row, String bitList) {
        char[] newBl = bitList.toCharArray();
        for (int i=0; i<newBl.length; i++) {
            Pixel p = pixels.get(""+i+","+row+"");
            if (newBl[i] == '1') {p.turnOn();}
            else {p.turnOff();}
        }
    }

    private String getColBits(Integer col) {
        String bitlist = "";
        for (int y=0; y<height; y++) {
            Pixel p = pixels.get(""+col+","+y+"");
            bitlist += (p.state) ?  1 : 0;
        }
        return bitlist;
    }

    private void setColBits(Integer col, String bitList) {
        char[] newBl = bitList.toCharArray();
        for (int i=0; i<newBl.length; i++) {
            Pixel p = pixels.get(""+col+","+i+"");
            if (newBl[i] == '1') {p.turnOn();}
            else {p.turnOff();}
        }
    }

    int countLit() {
        int count = 0;
        for (int x=0; x<length;x++) {
            for (int y=0;y<height;y++) {
                String pos = "" + x + "," + y + "";
                Pixel p = pixels.get(pos);
                if (p.state) {
                    count++;
                }
            }
        }
        return count;
    }
}

