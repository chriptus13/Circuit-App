package pt.isel.poo.circuit.model.cell;

import pt.isel.poo.circuit.model.Dir;

public class Block extends Cell {
    @Override
    public boolean fromString(String word) {
        return true;
    }

    @Override
    public boolean canLink(Dir dir, int color, boolean from) {
        return false;
    }

    @Override
    public void unlink() {
    }

    @Override
    public void unLink(Dir dir) {
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public boolean canUnlink() {
        return false;
    }
}
