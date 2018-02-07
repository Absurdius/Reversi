package game;

public interface ReversiPlayer {

    public int[] getNextMove(Board board);

    public int getColorPreference();

    public void setMyColor(int color);

    public long getTimeLimitPreference();
}
