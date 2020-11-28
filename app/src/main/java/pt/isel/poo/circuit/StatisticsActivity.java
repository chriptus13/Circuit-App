package pt.isel.poo.circuit;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatisticsActivity extends Activity {
    public static final int DEFAULT_TEXT_SIZE = 25;

    public static boolean closed;
    private LinearLayout scoreBoard;
    private TextView levelNumber;
    private int level = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        closed = false;
        scoreBoard = (LinearLayout) findViewById(R.id.scoreBoard);
        levelNumber = (TextView) findViewById(R.id.levelNumber);
        showScores();
    }

    public static boolean isClosed() {
        return closed;
    }

    public void onNextClicked(View view) {
        if (level + 1 <= Level.availableLevels()) level++;
        scoreBoard.removeAllViews();
        showScores();
    }

    public void onPrevClicked(View view) {
        if (level - 1 > 0) level--;
        scoreBoard.removeAllViews();
        showScores();
    }

    /**
     * Prints all scores of the level to be showed
     */
    private void showScores() {
        levelNumber.setText(String.format("%s%d", getResources().getString(R.string.level), level));
        Level l = Level.getLevel(level);
        for (int i = 0; i < Level.MAX_SCORES; i++) {
            Level.Player p = l.getPlayer(i);
            if (p != null) new ScoreTextView(i + 1, p.toString());
        }
    }

    @Override
    public void onBackPressed() {
        closed = true;
        super.onBackPressed();
    }

    public class ScoreTextView extends TextView {
        public ScoreTextView(int number, String p) {
            super(StatisticsActivity.this);
            setTextColor(Color.WHITE);
            setTextSize(DEFAULT_TEXT_SIZE);
            setGravity(Gravity.CENTER);
            setText(number + "º. " + p);
            scoreBoard.addView(this);
        }
    }
}


