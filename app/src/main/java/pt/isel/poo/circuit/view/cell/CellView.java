package pt.isel.poo.circuit.view.cell;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import pt.isel.poo.circuit.model.cell.Cell;
import pt.isel.poo.tile.Tile;

public abstract class CellView implements Tile {

    private static final int[] COLORS = {
            Color.RED, Color.GREEN, Color.YELLOW,
            Color.BLUE, Color.MAGENTA, Color.WHITE
    };

    protected Cell cell;

    private boolean highlight;

    private static Paint paint = new Paint();

    static {
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public int getColor() {
        return COLORS[cell.getColor()];
    }

    public Cell getCell() {
        return cell;
    }

    protected CellView(Cell cell) {
        this.cell = cell;
    }

    /**
     * Draw the links of the current cellview
     *
     * @param canvas To draw the tile
     * @param side   The width of tile in pixels
     */

    @Override
    public void draw(Canvas canvas, int side) {
        int x = side / 2;
        paint.setColor(getColor());
        paint.setStrokeWidth(side / 3);
        if (cell.nextDir != null)
            canvas.drawLine(x, x, x + cell.nextDir.deltaCol * x, x + x * cell.nextDir.deltaLin, paint);
        if (cell.prevDir != null) {
            canvas.drawLine(x + cell.prevDir.deltaCol * x, x + x * cell.prevDir.deltaLin, x, x, paint);
        }
    }

    @Override
    public boolean setSelect(boolean selected) {
        highlight = selected;
        return true;
    }

    /**
     * Draw the highlight when a cell it's selected
     *
     * @param canvas To draw the tile
     * @param side   The width of tile in pixels
     */
    protected void paintHighlight(Canvas canvas, int side) {
        if (highlight) {
            paint.setColor(Color.GRAY);
            paint.setStrokeWidth(10);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(0, 0, side, side, paint);
        }
    }
}
