package pt.isel.poo.circuit.model;


public enum Dir {
    RIGHT(0, 1), UP(-1, 0), DOWN(1, 0), LEFT(0, -1);

    public final int deltaLin;
    public final int deltaCol;

    Dir(int deltaLin, int deltaCol) {
        this.deltaLin = deltaLin;
        this.deltaCol = deltaCol;
    }

    /**
     * @param dir - Direction of the movement
     * @return Opposite dir of the parameter
     */
    public static Dir not(Dir dir) {
        switch (dir) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return null;
        }
    }
}
