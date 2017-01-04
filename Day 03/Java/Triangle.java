import java.util.ArrayList;
import java.util.List;

class Triangle {

    boolean isPossible;
    private List<Integer> legs = new ArrayList<>();

    Triangle(String[] sides) {
        this.isPossible = true;
        for (String side : sides) {
            if (!side.isEmpty()) {
                legs.add(Integer.parseInt(side));
            }
        }
        checkLegs();
    }

    private void checkLegs() {
        for (int i=0; i<3; i++) {
            Integer sumOfOtherLegs = 0;
            for (int j=0; j<3; j++) {
                if (i != j) {
                    sumOfOtherLegs += this.legs.get(j);
                }
            }
            if (sumOfOtherLegs <= legs.get(i)) {
                isPossible = false;
            }
        }
    }

}

