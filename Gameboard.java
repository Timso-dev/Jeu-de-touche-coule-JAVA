import java.util.Random;

// Tiny Ship = 2 field
// Small Ship = 3 fields
// Medium Ship = 4 fields
// Big Ship = 5 fields
public class Gameboard {
    private FieldState[][] board;
    private int boardSize;

    private int howManyLeft;
    public Ship[] shipList;
    public int shipCount;

    public Gameboard(int size) {
        setSize(size);
        int wholeShipCount;
        wholeShipCount = Start.bigShipsCount + Start.mediumShipsCount + Start.smallShipsCount + Start.tinyShipsCount;
        shipList = new Ship[wholeShipCount];
        howManyLeft = 0;
        shipCount = 0;
    }

    public void uploadResult(Shoot shoot) {
        int row = shoot.getRow();
        int column = shoot.getColumn();

        board[row][column] = shoot.getResult();

        boolean repeat, repeatAgain = false;

        if (shoot.getResult() == FieldState.sunk) {

            int lowSearchRow, lowSearchColumn, highSearchRow, highSearchColumn;
            int rowSecondSide = 0, columnSecondSide = 0;
            do {
                repeat = false;
                if (row != 0)
                    lowSearchRow = row - 1;
                else
                    lowSearchRow = 0;
                if (column != 0)
                    lowSearchColumn = column - 1;
                else
                    lowSearchColumn = 0;
                if (row != boardSize)
                    highSearchRow = row + 1;
                else
                    highSearchRow = row;
                if (column != boardSize)
                    highSearchColumn = column + 1;
                else
                    highSearchColumn = column;

                for (int i = lowSearchRow; i <= highSearchRow; i++)
                    for (int j = lowSearchColumn; j <= highSearchColumn; j++) {
                        if (board[i][j] == FieldState.able)
                            board[i][j] = FieldState.missed;
                        if (board[i][j] == FieldState.destroyed) {
                            board[i][j] = FieldState.sunk;
                            if (repeat == false) {
                                repeatAgain = true;
                                rowSecondSide = i;
                                columnSecondSide = j;
                            }
                            repeat = true;
                            row = i;
                            column = j;
                        }

                    }
            } while (repeat);

            while (repeatAgain) {
                repeatAgain = false;
                if (rowSecondSide != 0)
                    lowSearchRow = rowSecondSide - 1;
                else
                    lowSearchRow = 0;
                if (columnSecondSide != 0)
                    lowSearchColumn = columnSecondSide - 1;
                else
                    lowSearchColumn = 0;
                if (rowSecondSide != boardSize)
                    highSearchRow = rowSecondSide + 1;
                else
                    highSearchRow = rowSecondSide;
                if (columnSecondSide != boardSize)
                    highSearchColumn = columnSecondSide + 1;
                else
                    highSearchColumn = columnSecondSide;

                for (int i = lowSearchRow; i <= highSearchRow; i++)
                    for (int j = lowSearchColumn; j <= highSearchColumn; j++) {
                        if (board[i][j] == FieldState.able)
                            board[i][j] = FieldState.missed;
                        if (board[i][j] == FieldState.destroyed) {
                            board[i][j] = FieldState.sunk;
                            repeatAgain = true;
                            rowSecondSide = i;
                            columnSecondSide = j;
                        }

                    }
            }
        }
    }

    private void setSize(int size) {
        boardSize = size - 1;
        board = new FieldState[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                board[i][j] = FieldState.able;
    }

    // Remember row and column value from 0, but displayed from 1
    // means: you have to use this methods with arguments 1 lower


    // Remember row and column value from 0, but displayed from 1
    // means: you have to use this methods with arguments 1 lower
    public boolean tryAddTinyShip(int row, int column, Orientation orientation) {

        int lowRowNotAble, lowColumnNotAble, highRowNotAble, highColumnNotAble;

        if (orientation == Orientation.vertical) {
            if (row + 1 > boardSize || column > boardSize)
                return false;
            if (board[row][column] != FieldState.able)
                return false;
            if (board[row + 1][column] != FieldState.able)
                return false;

            if (row + 1 == boardSize)
                highRowNotAble = row + 1;
            else
                highRowNotAble = row + 2;

            if (row == 0)
                lowRowNotAble = row;
            else
                lowRowNotAble = row - 1;

            if (column == boardSize)
                highColumnNotAble = column;
            else
                highColumnNotAble = column + 1;

            if (column == 0)
                lowColumnNotAble = column;
            else
                lowColumnNotAble = column - 1;

            board[row][column] = FieldState.taken;
            board[row + 1][column] = FieldState.taken;
        } else {

            if (row > boardSize || column + 1 > boardSize)
                return false;
            if (board[row][column] != FieldState.able)
                return false;
            if (board[row][column + 1] != FieldState.able)
                return false;

            if (row == boardSize)
                highRowNotAble = row;
            else
                highRowNotAble = row + 1;

            if (row == 0)
                lowRowNotAble = row;
            else
                lowRowNotAble = row - 1;

            if (column + 1 == boardSize)
                highColumnNotAble = column + 1;
            else
                highColumnNotAble = column + 2;

            if (column == 0)
                lowColumnNotAble = column;
            else
                lowColumnNotAble = column - 1;

            board[row][column] = FieldState.taken;
            board[row][column + 1] = FieldState.taken;
        }

        for (int i = lowRowNotAble; i <= highRowNotAble; i++)
            for (int j = lowColumnNotAble; j <= highColumnNotAble; j++)
                if (board[i][j] != FieldState.taken)
                    board[i][j] = FieldState.notAble;

        shipList[shipCount] = new Ship(new IntPair(row, column), 2, orientation);
        shipCount++;

        howManyLeft += 2;
        return true;
    }

    // Remember row and column value from 0, but displayed from 1
    // means: you have to use this methods with arguments 1 lower
    public boolean tryAddSmallShip(int row, int column, Orientation orientation) {

        int lowRowNotAble, lowColumnNotAble, highRowNotAble, highColumnNotAble;

        if (orientation == Orientation.vertical) {
            if (row + 2 > boardSize || column > boardSize)
                return false;
            if (board[row][column] != FieldState.able)
                return false;
            if (board[row + 1][column] != FieldState.able)
                return false;
            if (board[row + 2][column] != FieldState.able)
                return false;

            if (row + 2 == boardSize)
                highRowNotAble = row + 2;
            else
                highRowNotAble = row + 3;

            if (row == 0)
                lowRowNotAble = row;
            else
                lowRowNotAble = row - 1;

            if (column == boardSize)
                highColumnNotAble = column;
            else
                highColumnNotAble = column + 1;

            if (column == 0)
                lowColumnNotAble = column;
            else
                lowColumnNotAble = column - 1;

            board[row][column] = FieldState.taken;
            board[row + 1][column] = FieldState.taken;
            board[row + 2][column] = FieldState.taken;
        } else {

            if (row > boardSize || column + 2 > boardSize)
                return false;
            if (board[row][column] != FieldState.able)
                return false;
            if (board[row][column + 1] != FieldState.able)
                return false;
            if (board[row][column + 2] != FieldState.able)
                return false;

            if (row == boardSize)
                highRowNotAble = row;
            else
                highRowNotAble = row + 1;

            if (row == 0)
                lowRowNotAble = row;
            else
                lowRowNotAble = row - 1;

            if (column + 2 == boardSize)
                highColumnNotAble = column + 2;
            else
                highColumnNotAble = column + 3;

            if (column == 0)
                lowColumnNotAble = column;
            else
                lowColumnNotAble = column - 1;

            board[row][column] = FieldState.taken;
            board[row][column + 1] = FieldState.taken;
            board[row][column + 2] = FieldState.taken;
        }

        for (int i = lowRowNotAble; i <= highRowNotAble; i++)
            for (int j = lowColumnNotAble; j <= highColumnNotAble; j++)
                if (board[i][j] != FieldState.taken)
                    board[i][j] = FieldState.notAble;

        shipList[shipCount] = new Ship(new IntPair(row, column), 3, orientation);
        shipCount++;
        howManyLeft += 3;
        return true;
    }

    // Remember row and column value from 0, but displayed from 1
    // means: you have to use this methods with arguments 1 lower
    public boolean tryAddMediumShip(int row, int column, Orientation orientation) {

        int lowRowNotAble, lowColumnNotAble, highRowNotAble, highColumnNotAble;

        if (orientation == Orientation.vertical) {
            if (row + 3 > boardSize || column > boardSize)
                return false;
            if (board[row][column] != FieldState.able)
                return false;
            if (board[row + 1][column] != FieldState.able)
                return false;
            if (board[row + 2][column] != FieldState.able)
                return false;
            if (board[row + 3][column] != FieldState.able)
                return false;

            if (row + 3 == boardSize)
                highRowNotAble = row + 3;
            else
                highRowNotAble = row + 4;

            if (row == 0)
                lowRowNotAble = row;
            else
                lowRowNotAble = row - 1;

            if (column == boardSize)
                highColumnNotAble = column;
            else
                highColumnNotAble = column + 1;

            if (column == 0)
                lowColumnNotAble = column;
            else
                lowColumnNotAble = column - 1;

            board[row][column] = FieldState.taken;
            board[row + 1][column] = FieldState.taken;
            board[row + 2][column] = FieldState.taken;
            board[row + 3][column] = FieldState.taken;
        } else {

            if (row > boardSize || column + 3 > boardSize)
                return false;
            if (board[row][column] != FieldState.able)
                return false;
            if (board[row][column + 1] != FieldState.able)
                return false;
            if (board[row][column + 2] != FieldState.able)
                return false;
            if (board[row][column + 3] != FieldState.able)
                return false;

            if (row == boardSize)
                highRowNotAble = row;
            else
                highRowNotAble = row + 1;

            if (row == 0)
                lowRowNotAble = row;
            else
                lowRowNotAble = row - 1;

            if (column + 3 == boardSize)
                highColumnNotAble = column + 3;
            else
                highColumnNotAble = column + 4;

            if (column == 0)
                lowColumnNotAble = column;
            else
                lowColumnNotAble = column - 1;

            board[row][column] = FieldState.taken;
            board[row][column + 1] = FieldState.taken;
            board[row][column + 2] = FieldState.taken;
            board[row][column + 3] = FieldState.taken;
        }

        for (int i = lowRowNotAble; i <= highRowNotAble; i++)
            for (int j = lowColumnNotAble; j <= highColumnNotAble; j++)
                if (board[i][j] != FieldState.taken)
                    board[i][j] = FieldState.notAble;

        shipList[shipCount] = new Ship(new IntPair(row, column), 4, orientation);
        shipCount++;
        howManyLeft += 4;
        return true;
    }
    public boolean tryAddBigShip(int row, int column, Orientation orientation) {

        int lowRowNotAble, lowColumnNotAble, highRowNotAble, highColumnNotAble;

        if (orientation == Orientation.vertical) {
            if (row + 4 > boardSize || column > boardSize)
                return false;
            if (board[row][column] != FieldState.able)
                return false;
            if (board[row + 1][column] != FieldState.able)
                return false;
            if (board[row + 2][column] != FieldState.able)
                return false;
            if (board[row + 3][column] != FieldState.able)
                return false;
            if (board[row + 4][column] != FieldState.able)
                return false;

            if (row + 4 == boardSize)
                highRowNotAble = row + 4;
            else
                highRowNotAble = row + 5;

            if (row == 0)
                lowRowNotAble = row;
            else
                lowRowNotAble = row - 1;

            if (column == boardSize)
                highColumnNotAble = column;
            else
                highColumnNotAble = column + 1;

            if (column == 0)
                lowColumnNotAble = column;
            else
                lowColumnNotAble = column - 1;

            board[row][column] = FieldState.taken;
            board[row + 1][column] = FieldState.taken;
            board[row + 2][column] = FieldState.taken;
            board[row + 3][column] = FieldState.taken;
            board[row + 4][column] = FieldState.taken;
        } else {

            if (row > boardSize || column + 4 > boardSize)
                return false;
            if (board[row][column] != FieldState.able)
                return false;
            if (board[row][column + 1] != FieldState.able)
                return false;
            if (board[row][column + 2] != FieldState.able)
                return false;
            if (board[row][column + 3] != FieldState.able)
                return false;
            if (board[row][column + 4] != FieldState.able)
                return false;

            if (row == boardSize)
                highRowNotAble = row;
            else
                highRowNotAble = row + 1;

            if (row == 0)
                lowRowNotAble = row;
            else
                lowRowNotAble = row - 1;

            if (column + 4 == boardSize)
                highColumnNotAble = column + 4;
            else
                highColumnNotAble = column + 5;

            if (column == 0)
                lowColumnNotAble = column;
            else
                lowColumnNotAble = column - 1;

            board[row][column] = FieldState.taken;
            board[row][column + 1] = FieldState.taken;
            board[row][column + 2] = FieldState.taken;
            board[row][column + 3] = FieldState.taken;
            board[row][column + 4] = FieldState.taken;
        }

        for (int i = lowRowNotAble; i <= highRowNotAble; i++)
            for (int j = lowColumnNotAble; j <= highColumnNotAble; j++)
                if (board[i][j] != FieldState.taken)
                    board[i][j] = FieldState.notAble;

        shipList[shipCount] = new Ship(new IntPair(row, column), 5, orientation);
        shipCount++;
        howManyLeft += 5;
        return true;
    }
    public FieldState resolveShoot(Shoot shoot) {

        int row = shoot.getRow();
        int column = shoot.getColumn();
        FieldState result = FieldState.missed;

        if (board[row][column] == FieldState.taken) {
            board[row][column] = FieldState.destroyed;
            howManyLeft--;

        } else {
            board[row][column] = FieldState.missed;
            return FieldState.missed;
        }

        IntPair pair = new IntPair(row, column);
        for (int i = 0; i < shipCount; i++) {
            shipList[i].shoot(shoot);
            if (shipList[i].onThisShip(pair))
                result = shipList[i].getShipState();
        }
        shoot.setResult(result);
        uploadResult(shoot);
        return result;
    }

    public boolean noMoreShips() {
        if (howManyLeft == 0)
            return true;
        return false;
    }

    public boolean canIShootThere(int row, int column) {
        if (board[row][column] == FieldState.missed || board[row][column] == FieldState.destroyed || board[row][column] == FieldState.sunk)
            return false;
        else
            return true;
    }

    public FieldState getFieldState(int row, int column) {
        return this.board[row][column];
    }

    public void debug() {

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++)
                System.out.print(board[i][j] + "\t");
            System.out.print("\n");
        }
    }

    public void setRandom() {
        int i, row, column;
        Orientation orientation;
        Random generator = new Random();

        for (i = 0; i < Start.bigShipsCount; i++)
            do {
                row = generator.nextInt(boardSize);
                column = generator.nextInt(boardSize);
                orientation = Orientation.values()[generator.nextInt(2)];
            } while (!tryAddBigShip(row, column, orientation));

        for (i = 0; i < Start.mediumShipsCount; i++)
            do {
                row = generator.nextInt(boardSize);
                column = generator.nextInt(boardSize);
                orientation = Orientation.values()[generator.nextInt(2)];
            } while (!tryAddMediumShip(row, column, orientation));

        for (i = 0; i < Start.smallShipsCount; i++)
            do {
                row = generator.nextInt(boardSize);
                column = generator.nextInt(boardSize);
                orientation = Orientation.values()[generator.nextInt(2)];
            } while (!tryAddSmallShip(row, column, orientation));

        for (i = 0; i < Start.tinyShipsCount; i++)
            do {
                row = generator.nextInt(boardSize);
                column = generator.nextInt(boardSize);
                orientation = Orientation.values()[generator.nextInt(2)];
            } while (!tryAddTinyShip(row, column, orientation));

    }

}
