package pt.isel.poo.circuit.view.cell;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import pt.isel.poo.circuit.model.cell.Cell;
import pt.isel.poo.circuit.model.cell.Line;

public class LineView extends CellView {
    private static Paint linePaint = new Paint();

    static {
        linePaint.setColor(Color.GRAY);
        linePaint.setStrokeCap(Paint.Cap.BUTT);
    }

    public LineView(Cell cell) {
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
        linePaint.setStrokeWidth(side / 3);
        if (((Line) cell).getOrient())
            canvas.drawLine(side / 2, side / 5, side / 2, side - side / 5, linePaint);
        else canvas.drawLine(side / 5, side / 2, side - side / 5, side / 2, linePaint);
        if (cell.hasColor()) super.draw(canvas, side);
        paintHighlight(canvas, side);
    }
}

