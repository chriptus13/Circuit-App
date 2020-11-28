package pt.isel.poo.circuit.model.cell;

import pt.isel.poo.circuit.model.Dir;

public class Terminal extends Cell {

    @Override
    public boolean fromString(String word) {
        char type = word.charAt(0);
        if (type != 'T' && (type < 'A' || type > 'F')) {    //Terminals can be represent with the String "T0" instead of "A"
            return false;
        }
        color = type == 'T' ? word.charAt(1) - '0' : type - 'A';
        return true;
    }

    @Override
    public boolean canLink(Dir dir, int color, boolean from) {
        return nextDir == null && prevDir == null && (color == -1 || color == this.color);
    }

    @Override
    public void unlink() {
        nextDir = null;
        prevDir = null;
    }

    @Override
    public boolean isComplete() {
        return prevDir != null || nextDir != null;
    }

    @Override
    public void unLink(Dir dir) {
        nextDir = null;
        prevDir = null;
    }
}
