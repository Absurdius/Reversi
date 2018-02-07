package game;

public interface ReversiPlayer {

    Move getNextMove(Board board);

    int getColorPreference();

    void setMyColor(int color);

    long getTimeLimitPreference();

    void setTimeLimit(long timeLimit);
}
