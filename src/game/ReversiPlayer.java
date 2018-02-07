package game;

public interface ReversiPlayer {

    int[] getNextMove(Board board);

    int getColorPreference();

    void setMyColor(int color);

    long getTimeLimitPreference();

    void setTimeLimit(long timeLimit);
}
