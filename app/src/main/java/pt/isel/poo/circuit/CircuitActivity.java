package pt.isel.poo.circuit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Scanner;

import pt.isel.poo.circuit.model.Circuit;
import pt.isel.poo.circuit.model.Loader;
import pt.isel.poo.circuit.model.Position;
import pt.isel.poo.circuit.model.cell.Cell;
import pt.isel.poo.circuit.view.cell.CellView;
import pt.isel.poo.tile.OnBeatListener;
import pt.isel.poo.tile.OnTileTouchListener;
import pt.isel.poo.tile.Tile;
import pt.isel.poo.tile.TilePanel;

public class CircuitActivity extends Activity implements OnTileTouchListener {

    public static final String FILE_NAME = "currLvl.txt";
    TilePanel view;
    Circuit model;

    TextView level;
    TextView time;

    Button nextLevel;
    private int currLvl = 1;
    private int countTime;
    private boolean levelsRemaining = true;


    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_circuit);

        view = (TilePanel) findViewById(R.id.view);
        view.setListener(this);
        view.setHeartbeatListener(1000, new OnBeatListener() {
            @Override
            public void onBeat(long beat, long time) {
                if (nextLevel.isEnabled() || !levelsRemaining) return;
                updateTime(++countTime);
            }
        });

        nextLevel = (Button) findViewById(R.id.nextLevel);
        nextLevel.setEnabled(false);
        nextLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextLevel();
            }
        });

        if (state == null)     //Only load the level saved in the file when the app starts
            loadProgress();

        level = (TextView) findViewById(R.id.level);
        level.setText(String.format("%s%d", getResources().getString(R.string.level), currLvl));

        time = (TextView) findViewById(R.id.timer);

        Level.clear();      //When we close and reopen the app the list already have levels saved, but the user dont want to save so we clear the list
        canLoadLevel(currLvl);
        loadStatistics();
        view.setSize(model.getWidth(), model.getHeight());
        paint();
    }

    /**
     * Saves in the file the last level played
     */
    private void saveProgress() {
        try {
            OutputStream out = openFileOutput(FILE_NAME, MODE_PRIVATE);
            PrintWriter pw = new PrintWriter(out);
            pw.println(currLvl);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the list with the levels played and scores done in a file
     */
    private void saveStatistics() {
        try (PrintWriter pw = new PrintWriter(openFileOutput(Level.FILE_NAME, MODE_PRIVATE))) {
            Iterator<Level> it = Level.getIterator();
            while (it.hasNext()) {
                Level l = it.next();
                pw.println(l.getNumber());
                l.printAll(pw);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Load the Statistics saved when the player closed the app
     */
    private void loadStatistics() {
        try (Scanner s = new Scanner(openFileInput(Level.FILE_NAME))) {
            do {
                int i = s.nextInt();
                String st;
                Level.addLevel(i);
                s.nextLine();
                while (s.hasNext() && !s.hasNextInt()) {
                    st = s.nextLine();
                    String name = st.substring(0, st.indexOf("-") - 1);
                    String score = st.substring(st.indexOf("-") + 2);
                    Level.getLevel(i).add(Integer.valueOf(score), name);
                }
            }
            while (s.hasNext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the last level played saved in the FILE_NAME
     */
    private void loadProgress() {
        try (Scanner in = new Scanner(openFileInput(FILE_NAME))) {
            currLvl = in.nextInt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Put in TextView responsible to show the timer the current time passed since the player started the level
     *
     * @param clock - time that will be represented in the timer
     */
    private void updateTime(int clock) {
        String seconds = clock % 60 < 10 ? "0" + clock % 60 : Integer.toString(clock % 60);
        String minutes = clock / 60 < 10 ? "0" + clock / 60 : Integer.toString(clock / 60);
        time.setText(minutes + ":" + seconds);
    }

    /**
     * Changes all the variables responsible for the good run of the app in order to be possible to play the next level
     */
    private void onNextLevel() {
        Circuit x = model;
        nextLevel.setEnabled(false);
        updateTime(countTime = 0);
        if (!canLoadLevel(++currLvl)) {
            model = x;
            level.setText(getResources().getString(R.string.noMoreLvl));
            levelsRemaining = false;
            terminate();
            return;
        }
        view.setSize(model.getWidth(), model.getHeight());
        paint();
        level.setText(String.format("%s%d", getResources().getString(R.string.level), currLvl));
    }

    /**
     * Create a AlertDialog asking the user if want to exit the app
     */
    private void terminate() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.closeGame)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveProgress();
                        saveStatistics();
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    /**
     * Draw all cells in the model in the TilePanel
     */
    private void paint() {
        for (int i = 0; i < model.getHeight(); i++) {
            for (int j = 0; j < model.getWidth(); j++)
                view.setTile(j, i, createView(model.getCell(i, j)));
        }
    }

    @Override
    public void onBackPressed() {
        terminate();
    }

    /**
     * Creates a Tile which will be put in the TilePanel
     *
     * @param cell we want to represent in the TilePanel
     * @return Tile
     */
    private Tile createView(Cell cell) {
        try {
            Class cls = Class.forName("pt.isel.poo.circuit.view.cell." + cell.getClass().getSimpleName() + "View");
            return (CellView) cls.getConstructor(Cell.class).newInstance(cell);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Load the current level
     *
     * @param currLvl - level that will be loaded
     * @return True if it's possible to load that level
     */
    private boolean canLoadLevel(int currLvl) {
        try (Scanner in = new Scanner(getResources().openRawResource(R.raw.demo))) {
            model = new Loader(in).load(currLvl);
        } catch (Loader.LevelFormatException e) {
            return false;
        }
        Level.addLevel(currLvl);
        return true;
    }

    @Override
    public boolean onClick(int xTile, int yTile) {
        Position p = new Position(yTile, xTile);
        if (model.unlink(p)) {
            paint();
            return true;
        }
        return false;
    }

    @Override
    public boolean onDrag(int xFrom, int yFrom, int xTo, int yTo) {
        Position from = new Position(yFrom, xFrom);
        Position to = new Position(yTo, xTo);
        if (model.drag(from, to)) {
            view.setTile(xFrom, yFrom, createView(model.getCell(from)));
            view.setTile(xTo, yTo, createView(model.getCell(to)));
            if (model.isOver() && levelsRemaining) {
                nextLevel.setEnabled(true);
                level.setText(String.format("%s%d%s", getResources().getString(R.string.winnerMsg_1), currLvl, getResources().getString(R.string.winnerMsg_2)));
                getPlayerName();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDragEnd(int x, int y) {
    }

    @Override
    public void onDragCancel() {
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if (SelectLevelActivity.isClosed() && StatisticsActivity.isClosed()) {   //Put in the bundle all the variables responsible by the good run of the app
            state.putSerializable("model", model);
            state.putInt("countTime", countTime);
            state.putInt("currLvl", currLvl);
            state.putBoolean("buttonState", nextLevel.isEnabled());
            state.putString("msg", level.getText().toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        if (SelectLevelActivity.isClosed() && StatisticsActivity.isClosed()) {
            model = (Circuit) state.getSerializable("model");
            countTime = state.getInt("countTime");
            updateTime(countTime);
            currLvl = state.getInt("currLvl");
            nextLevel.setEnabled(state.getBoolean("buttonState"));
            level.setText(state.getString("msg"));
            paint();
        }
    }

    public void onSelectLevelClicked(View view) {
        if (!levelsRemaining)
            levelsRemaining = true;        //When the player finish the last level and he wants to play another level we put levelsRemaining=true
        Intent it = new Intent(this, SelectLevelActivity.class);
        startActivityForResult(it, currLvl);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == currLvl && resultCode == RESULT_OK) {
            currLvl = data.getIntExtra("level", currLvl) - 1;     //Will be read the select level - 1 then we call onNextLevel() will be loaded the selected level
            onNextLevel();
        }
    }

    /**
     * Create a AlertDialog which ask the name of the player and save the time he completed the level
     */
    private void getPlayerName() {
        final EditText name = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle(R.string.Congratulations)
                .setMessage(R.string.playerName)
                .setView(name)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String playerName = name.getText().toString();
                        Level.getLevel(currLvl).add(countTime, playerName);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    public void onStatisticsClicked(View view) {
        Intent it = new Intent(this, StatisticsActivity.class);
        startActivity(it);
    }
}
