public class Ship {
    private FieldState shipState;
    private final int shipSize;
    private final IntPair[] shipFields;
    private final FieldState[] shipPartsState;

    public Ship(IntPair begining, int size, Orientation orientation) {
        shipSize = size;
        shipFields = new IntPair[shipSize];
        shipPartsState = new FieldState[shipSize];
        shipState = FieldState.untouched;
        int row = begining.row;
        int column = begining.column;
        if (orientation == Orientation.horizontal)
            for (int i = 0; i < shipSize; i++) {
                shipFields[i] = new IntPair(row, column + i);
                shipPartsState[i] = FieldState.untouched;
            }
        else
            for (int i = 0; i < shipSize; i++) {
                shipFields[i] = new IntPair(row + i, column);
                shipPartsState[i] = FieldState.untouched;
            }

    }

    public FieldState getShipState() {
        return shipState;
    }

    public boolean onThisShip(IntPair field) {
        for (int i = 0; i < shipSize; i++)
            if (shipFields[i].row == field.row && shipFields[i].column == field.column)
                return true;
        return false;
    }

    public IntPair[] getShipFields() {
        return shipFields;
    }

    public int getShipSize() {
        return shipSize;
    }

    public void shoot(Shoot shotToCheck) {
        int rowOfShoot = shotToCheck.getRow();
        int columnOfShoot = shotToCheck.getColumn();
        for (int i = 0; i < shipSize; i++) {
            if (rowOfShoot == shipFields[i].row && columnOfShoot == shipFields[i].column) {
                shipState = FieldState.destroyed;
                shipPartsState[i] = FieldState.destroyed;
            };
        }
        int destroyedParts = 0;

        for (int i = 0; i < shipSize; i++)
            if (shipPartsState[i] == FieldState.destroyed)
                destroyedParts++;

        if (destroyedParts == shipSize)
            shipState = FieldState.sunk;

    }

}
