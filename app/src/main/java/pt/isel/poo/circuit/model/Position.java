package pt.isel.poo.circuit.model;

public class Position {
    private int lin;
    private int col;

    public Position(int line, int col) {
        lin = line;
        this.col = col;
    }

    public int getLine() {
        return lin;
    }

    public int getCol() {
        return col;
    }

    public boolean equals(Position p) {
        return lin == p.lin && col == p.col;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Position && equals((Position) obj);
    }

    /**
     * @param to - position of the destination cell of the movement
     * @return Opposite direction of the movement
     */

    Dir getDirTo(Position to) {
        if (lin == to.getLine())
            return getCol() < to.getCol() ? Dir.RIGHT : Dir.LEFT;
        if (col == to.getCol())
            return getLine() < to.getLine() ? Dir.DOWN : Dir.UP;

        return null;
    }
}
