import java.util.Random;

public class Hard extends ComputerPlayer {
    private boolean lastWasGood;
    private int[][] probability;
    private int lastRow;
    private int lastColumn;
    private int whatIsNeeded;
    private int modifirer;

    public Hard(int size) {
        super(size);
        Random generator = new Random();
        lastWasGood = false;
        probability = new int[size][size];
        setProbability();
        whatIsNeeded = 2;
        modifirer = generator.nextInt(2);
    }

    private void setProbability() {
        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++)
                probability[i][j] = 0;
    }

    public void shoot() {

        Random generator = new Random();
        int row = 0, column = 0;
        Shoot shoot;
        int deadEndOuter = 0;
        do {
            int deadEnd = 0;
            do {
                if (deadEndOuter == 150 && modifirer == 1)
                    modifirer = 0;
                else if (deadEndOuter == 150 && modifirer == 0)
                    modifirer = 1;
                do {
                    if (deadEnd == 1000)
                        break;
                    row = generator.nextInt(boardSize);
                    column = generator.nextInt(boardSize);
                    deadEnd++;
                } while ((row + column) % whatIsNeeded == modifirer);
                if (deadEnd == 1000) {
                    row = generator.nextInt(boardSize);
                    column = generator.nextInt(boardSize);
                }

                if (lastWasGood) {
                    for (int i = 0; i < boardSize; i++)
                        for (int j = 0; j < boardSize; j++)
                            if (probability[i][j] == 1 && opponentBoard.canIShootThere(i, j)) {
                                row = i;
                                column = j;
                            }
                    for (int i = 0; i < boardSize; i++)
                        for (int j = 0; j < boardSize; j++)
                            if (probability[i][j] == 2 && opponentBoard.canIShootThere(i, j)) {
                                row = i;
                                column = j;
                            }
                }
                deadEndOuter++;
            } while (opponentBoard.canIShootThere(row, column) == false);

            shoot = new Shoot(row, column);

            shoot.setResult(Start.playerOne.myBoard.resolveShoot(shoot));
            uploadResult(shoot);

            if (shoot.getResult() == FieldState.sunk) {
                setProbability();
                lastWasGood = false;
            }
            if (shoot.getResult() == FieldState.destroyed) {
                if (lastWasGood) {

                    if (row == lastRow) {
                        if (column - 1 >= 0 && opponentBoard.canIShootThere(row, column - 1))
                            probability[row][column - 1] = 2;
                        if (column + 1 <= boardSize - 1 && opponentBoard.canIShootThere(row, column + 1))
                            probability[row][column + 1] = 2;
                    }
                    if (column == lastColumn) {
                        if (row - 1 >= 0 && opponentBoard.canIShootThere(row - 1, column))
                            probability[row - 1][column] = 2;
                        if (row + 1 <= boardSize - 1 && opponentBoard.canIShootThere(row + 1, column))
                            probability[row + 1][column] = 2;
                    }
                } else {
                    if (row != 0)
                        probability[row - 1][column] = 1;
                    if (row != boardSize - 1)
                        probability[row + 1][column] = 1;
                    if (column != 0)
                        probability[row][column - 1] = 1;
                    if (column != boardSize - 1)
                        probability[row][column + 1] = 1;
                }
                lastRow = row;
                lastColumn = column;
                lastWasGood = true;
            }
        } while (shoot.getResult() != FieldState.missed && Start.playerOne.isDead() == false);

    }

}
