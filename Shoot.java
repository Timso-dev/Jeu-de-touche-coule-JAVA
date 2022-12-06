import java.io.Serializable;

public class Shoot implements Serializable {
    private static final long serialVersionUID = 1L;

    private int row;
    private int column;
    private FieldState result;
    private boolean ready;

    public Shoot(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public void setResult(FieldState result) {
        this.result = result;
    }

    public FieldState getResult() {
        return this.result;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

}
