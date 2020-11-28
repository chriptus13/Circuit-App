package pt.isel.poo.circuit;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Level {
    public static final int MAX_SCORES = 10;        //Number of scores a level can have
    public static final String FILE_NAME = "score.txt";
    private static List<Level> levelList = new ArrayList<>();
    private int number;
    private Player[] scores;


    public Level(int number) {
        this.number = number;
        scores = new Player[MAX_SCORES];
    }

    /**
     * Search in the list of levels if exists a level with the number passes as parameter
     *
     * @param number of the level that will return if exits
     * @return the level in the list with that number or null if doesn't find it
     */
    public static Level getLevel(int number) {
        for (Level l : levelList) {
            if (l.getNumber() == number)
                return l;
        }
        return null;
    }

    /**
     * Add the new score if it's possible in the scoreboard of the level and sort the scoreboard
     *
     * @param score did by the player
     * @param name  of the player
     */
    public void add(int score, String name) {
        int idx = findIndex(score);
        if (idx >= 0) {
            Player[] temp = new Player[scores.length];
            for (int i = 0; i < idx; i++)
                temp[i] = scores[i];
            temp[idx] = new Player(score, name);
            for (idx++; idx < scores.length; idx++) {
                if (scores[idx - 1] == null) break;
                temp[idx] = scores[idx - 1];
            }
            scores = temp;
        }
    }

    /**
     * Search if the score can enter the scoreboard of the level
     *
     * @param score did by the player
     * @return index where can be inserted the score in the array of scores of the level or -1 if the score can't enter the scoreboard
     */
    private int findIndex(int score) {
        for (int i = 0; i < scores.length; i++) {
            Player p = getPlayer(i);
            if (p == null || p.score > score) return i;
        }
        return -1;
    }

    /**
     * Print at pw all scores of the level
     *
     * @param pw
     */
    public void printAll(PrintWriter pw) {
        for (Player p : scores) {
            if (p == null) return;
            pw.println(p);
        }
    }

    public Player getPlayer(int i) {
        return scores[i];
    }

    public static Iterator<Level> getIterator() {
        return levelList.iterator();
    }

    /**
     * Add the level number i in the levelList if doesn´t exist already
     *
     * @param i - number of the level to be added
     */
    public static void addLevel(int i) {
        Level l = getLevel(i);
        if (l == null) levelList.add(new Level(i));
    }

    public static int availableLevels() {
        return levelList.size();
    }

    public int getNumber() {
        return number;
    }

    public static void clear() {
        levelList.clear();
    }

    /**
     * Objects of this class represent a player where will be saved the name and the score
     */
    public class Player {
        private int score;
        private String name;

        public Player(int score, String name) {
            this.score = score;
            this.name = name;
        }

        @Override
        public String toString() {
            return name + " - " + score;
        }
    }
}
