package pt.isel.poo.circuit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Scanner;

import pt.isel.poo.circuit.model.Loader;

public class SelectLevelActivity extends Activity {
    private LinearLayout list;
    private static boolean closed;

    public static boolean isClosed() {
        return closed;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        closed = false;
        list = new LinearLayout(this);
        list.setBackgroundColor(Color.BLACK);
        list.setOrientation(LinearLayout.VERTICAL);
        for (int level = 1; canLoad(level); level++) {
            new TxtButton(level);
        }
        setContentView(list);
    }

    /**
     * @param level that we will test if can be loaded
     * @return true if can be loaded
     */
    private boolean canLoad(int level) {
        try (Scanner in = new Scanner(getResources().openRawResource(R.raw.demo))) {
            new Loader(in).load(level);
            return true;
        } catch (Loader.LevelFormatException e) {
            return false;
        }
    }


    public class TxtButton extends Button implements View.OnClickListener {
        private final int level;

        public TxtButton(int level) {
            super(SelectLevelActivity.this);
            this.level = level;
            setText(String.format("%s%d", getResources().getString(R.string.level), level));
            list.addView(this);
            setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("level", level);
            setResult(RESULT_OK, intent);
            closed = true;
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        closed = true;
        super.onBackPressed();
    }
}
