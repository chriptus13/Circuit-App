package pt.isel.poo.circuit.model;

import java.io.Serializable;

import pt.isel.poo.circuit.model.cell.Cell;


public class Circuit implements Serializable {
    private final int height;
    private final int width;
    private Cell[][] grid;

    Circuit(int height, int width) {
        this.height = height;
        this.width = width;
        grid = new Cell[height][width];
    }

    /**
     * Also change colors and dirs of the cells
     *
     * @param from - Position of the origin of movement
     * @param to   - Position of the destination of movement
     * @return True it's possible to drag
     */
    public boolean drag(Position from, Position to) {
        Dir dir = from.getDirTo(to);
        Cell cf = getCell(from.getLine(), from.getCol()), ct = getCell(to.getLine(), to.getCol());

        if (cf.canLink(dir, ct.getColor(), true) && ct.canLink(dir, cf.getColor(), false)) {
            if (cf.nextDir == null)
                cf.nextDir = dir;
            else cf.prevDir = dir;
            if (ct.nextDir == null)
                ct.nextDir = Dir.not(dir);
            else ct.prevDir = Dir.not(dir);
            ct.changeColor(cf.getColor());
            return true;
        }
        if (cf.hasColor() && cf.getColor() == ct.getColor() && !cf.isTerminal()
                && ((cf.nextDir == null && cf.prevDir != null) || (cf.nextDir != null && cf.prevDir == null))
                && (dir == cf.nextDir || dir == cf.prevDir)
        ) {
            unlink(from);
            return true;
        }
        return false;
    }

    /**
     * Verify if it's possible to unlink the cell in the position pos to the other cells linked with that cell
     * If it's possible realize that process
     *
     * @param pos - position of the cell to unlink
     * @return True if it's possible to unlink that cell
     */

    public boolean unlink(Position pos) {
        Cell c = getCell(pos);
        if (c.canUnlink()) {
            if (c.nextDir != null) {
                Cell next = getCell(pos.getLine() + c.nextDir.deltaLin, pos.getCol() + c.nextDir.deltaCol);
                if (next.prevDir == Dir.not(c.nextDir))
                    next.unLink(next.prevDir);
                else next.unLink(next.nextDir);
            }
            if (c.prevDir != null) {
                Cell prev = getCell(pos.getLine() + c.prevDir.deltaLin, pos.getCol() + c.prevDir.deltaCol);
                if (prev.prevDir == Dir.not(c.prevDir))
                    prev.unLink(prev.prevDir);
                else prev.unLink(prev.nextDir);
            }
            c.unlink();
            return true;
        }
        return false;
    }

    /**
     * @return True if the level is completed
     */
    public boolean isOver() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!grid[i][j].isComplete()) return false;
            }
        }
        return true;
    }

    /**
     * Put the cell in the array if cells in the index lin and col
     *
     * @param lin  - index of the two-dimensional array
     * @param col  - index of the two-dimensional array
     * @param cell - Cell we want to put in the array
     */
    void putCell(int lin, int col, Cell cell) {
        grid[lin][col] = cell;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    /**
     * @param i - index of the two-dimensional array
     * @param j - index of the two-dimensional array
     * @return the cell in the array of cells with the coordinates i and j
     */
    public Cell getCell(int i, int j) {
        return grid[i][j];
    }

    public Cell getCell(Position pos) {
        return grid[pos.getLine()][pos.getCol()];
    }


}
