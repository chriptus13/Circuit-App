package pt.isel.poo.circuit.model.cell;

import pt.isel.poo.circuit.model.Dir;

public class Free extends Cell {

    @Override
    public boolean fromString(String word) {
        color = -1;
        return true;
    }

    @Override
    public boolean canLink(Dir d, int color, boolean from) {
        return from ? hasColor() && (nextDir == null || prevDir == null) : this.color == -1 || color == this.color && (nextDir == null || prevDir == null);
    }

    @Override
    public boolean isComplete() {
        return prevDir != null && nextDir != null;
    }
}
