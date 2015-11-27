
public abstract class Player {
    protected int playerNum;
    
    public Player(int playerNum)
    {
        this.playerNum = playerNum;
    }
    
    public abstract Move getMove();
    public abstract void OpponentMove(Move m);
}
