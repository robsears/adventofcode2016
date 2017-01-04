class Location {

    String id;
    Integer posX;
    Integer posY;
    Integer distance = Integer.MAX_VALUE;

    Location(Integer posX, Integer posY, boolean isStart, boolean isDestination, String id) {
        this.posX = posX;
        this.posY = posY;
        if (isStart || isDestination) {
            this.id = id;
        }
    }

}

