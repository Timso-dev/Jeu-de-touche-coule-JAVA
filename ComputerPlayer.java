public abstract class ComputerPlayer extends Player {

    public ComputerPlayer(int size) {
        boardSize = size;
        opponentBoard = new Gameboard(size);
        setBoard();
    }

    public abstract void shoot();

}
