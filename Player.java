public abstract class Player {
    public Gameboard myBoard;
    protected Gameboard opponentBoard;
    protected int boardSize;

    protected void setBoard() {
        myBoard = new Gameboard(boardSize);
        myBoard.setRandom();

    }

    public abstract void shoot();

    public void debug() {
        myBoard.debug();
    }

    protected void uploadResult(Shoot shoot) {
        opponentBoard.uploadResult(shoot);
    }

    public boolean isDead() {
        return myBoard.noMoreShips();
    }

}
