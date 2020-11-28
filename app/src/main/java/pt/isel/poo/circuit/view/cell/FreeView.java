package pt.isel.poo.circuit.view.cell;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import pt.isel.poo.circuit.model.cell.Cell;

public class FreeView extends CellView {

    public FreeView(Cell cell) {
        super(cell);
    }

    private static Paint freePaint = new Paint();

    static {
        freePaint.setColor(Color.GRAY);
        freePaint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * Draw the standard form of the cellview when doesn't have a color
     *
     * @param canvas To draw the tile
     * @param side   The width of tile in pixels
     */
    @Override
    public void draw(Canvas canvas, int side) {
        freePaint.setStrokeWidth(side / 3);
        canvas.drawPoint(side / 2, side / 2, freePaint);
        if (cell.hasColor()) super.draw(canvas, side);
        paintHighlight(canvas, side);
    }
}
