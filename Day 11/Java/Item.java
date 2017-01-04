public class Item implements Comparable<Item> {

    int floor;
    String chem;
    String name;
    String type;

    Item(int floor, String chem, String type) {
        this.floor = floor;
        this.chem = chem;
        this.type = type; //"G" or "M"
        this.name = chem.substring(0,2).toUpperCase() + type;
    }

    public int compareTo(Item i) {
        int lastCmp = name.compareTo(i.name);
        return (lastCmp != 0 ? lastCmp : name.compareTo(i.name));
    }

    boolean compatibleWith(Item i) {
        return this.type.equals(i.type) || this.chem.equals(i.chem);
    }

}

