package pt.isel.poo.circuit.view.cell;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import pt.isel.poo.circuit.model.cell.Cell;

public class TerminalView extends CellView {

    private static Paint terminalPaint = new Paint();

    public TerminalView(Cell cell) {
        super(cell);
    }

    /**
     * Draw the standard form of the cellview when doesn't have a color
     *
     * @param canvas To draw the tile
     * @param side   The width of tile in pixels
     */
    @Override
    public void draw(Canvas canvas, int side) {
        super.draw(canvas, side);
        terminalPaint.setColor(getColor());
        canvas.drawCircle(side / 2, side / 2, side / 2 - side / 10, terminalPaint);
        terminalPaint.setColor(Color.BLACK);
        canvas.drawCircle(side / 2, side / 2, side / 8, terminalPaint);
        paintHighlight(canvas, side);
    }
}