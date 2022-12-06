public class HumanPlayer extends Player {

    private Deployment deployment;

    public HumanPlayer(int size) {
        boardSize = size;
        opponentBoard = new Gameboard(size);
        deployment = new Deployment(size);
        myBoard = deployment.returnBoard();
    }

    public void shoot() {
    }

}
