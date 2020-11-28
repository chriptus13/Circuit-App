package pt.isel.poo.circuit.view.cell;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import pt.isel.poo.circuit.model.cell.Cell;

public class BlockView extends CellView {
    private static Paint paint = new Paint();

    static {
        paint.setColor(Color.WHITE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public BlockView(Cell cell) {
        super(cell);
    }

    @Override
    public void draw(Canvas canvas, int side) {
        int x = side / 10;
        paint.setStrokeWidth(x);
        canvas.drawLine(x, x, side - x, side - x, paint);
        canvas.drawLine(x, side - x, side - x, x, paint);
        paintHighlight(canvas, side);
    }
}
