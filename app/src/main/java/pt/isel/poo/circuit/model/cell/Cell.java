package pt.isel.poo.circuit.model.cell;

import pt.isel.poo.circuit.model.Dir;

public abstract class Cell {

    protected int color;
    public Dir nextDir;
    public Dir prevDir;

    public int getColor() {
        return color;
    }

    public boolean isTerminal() {
        return this instanceof Terminal;
    }

    /**
     * @param type - char that represents a type of a cell
     * @return Cell created based on the type
     */
    public static Cell newInstance(char type) {
        switch (type) {
            case '.':
                return new Free();
            case '-':
                return new Line();
            case '|':
                return new Line();
            case '0':
                return new Block();
            default:
                if (type >= 'A' && type <= 'F' || type == 'T') return new Terminal();
        }
        return null;
    }

    /**
     * Can be specialized for each type of cell
     *
     * @param word - String with the additional information about the cell
     * @return True if the cell known
     */

    public boolean fromString(String word) {
        char type = word.charAt(0);
        return ((type >= 'A' && type <= 'Z') || type == '.' || type == '-' || type == '|');
    }

    /**
     * @return True if the cell has color
     */
    public boolean hasColor() {
        return color != -1;
    }

    /**
     * Changes the color of the cell to the parameter color
     *
     * @param color - Color we want to change
     */
    public void changeColor(int color) {
        this.color = color;
    }

    /**
     * See if the cell can link to the other cell or if a cell can link to it
     * Can be specialized for each type of cell
     *
     * @param dir   - direction of the movement
     * @param color - color of the destination cell
     * @param from  - if the cell have the position from
     * @return True can link or can be linked
     **/
    public abstract boolean canLink(Dir dir, int color, boolean from);

    /**
     * @return True if can unlink
     */

    public boolean canUnlink() {
        return hasColor();
    }

    /**
     * Do the unlink process
     * Can be specialized for each type of cell
     */

    public void unlink() {
        prevDir = null;
        nextDir = null;
        color = -1;
    }

    /**
     * Do the unlink process of the previous cell or next cell
     * Can be specialized for each type of cell
     *
     * @param dir - direction to unlink of the cell
     */
    public void unLink(Dir dir) {
        if (dir == nextDir) {
            nextDir = null;
            if (prevDir == null)
                color = -1;
        } else {
            prevDir = null;
            if (nextDir == null)
                color = -1;
        }
    }

    /**
     * Check if cell feels completed
     */
    public abstract boolean isComplete();
}
