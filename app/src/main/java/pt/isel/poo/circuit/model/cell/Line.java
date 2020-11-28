package pt.isel.poo.circuit.model.cell;

import pt.isel.poo.circuit.model.Dir;

public class Line extends Cell {
    private boolean orient; //True = | ; False = -

    public boolean getOrient() {
        return orient;
    }

    @Override
    public boolean fromString(String word) {
        char type = word.charAt(0);
        orient = (type == '|');
        color = -1;
        return true;
    }

    @Override
    public boolean canLink(Dir d, int color, boolean from) {
        boolean s = from ? hasColor() && (nextDir == null || prevDir == null) : this.color == -1 || color == this.color && (nextDir == null || prevDir == null);
        return s && (orient ? (d == Dir.DOWN || d == Dir.UP) : (d == Dir.LEFT || d == Dir.RIGHT));
    }

    @Override
    public boolean isComplete() {
        return prevDir != null && nextDir != null;
    }
}
