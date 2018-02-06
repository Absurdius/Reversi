package game;

import java.util.ArrayList;

public interface ReversiPlayer {

    public int[] getNextMove(int[][] possibleMoves, ArrayList<int[]> validMoves);

    public int getColorPreference();

    public void setMyColor(int color);

    public long getTimeLimitPreference();
}
