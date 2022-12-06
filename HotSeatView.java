import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HotSeatView extends NewGame {

    HotSeatView(int mySunk, int yourSunk) {
        this.sunkShips = mySunk;
        setPanels();
        this.boardSize = Start.playerOne.boardSize;
        myButtons = new MyButton[boardSize][boardSize];
        opponentButtons = new MyButton[boardSize][boardSize];
        myLabels = new JLabel[2][boardSize];
        opponentLabels = new JLabel[2][boardSize];

        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++) {
                myButtons[i][j] = new MyButton("  ");
                myButtons[i][j].setSize(myButtons[i][j].getPreferredSize());
                myButtons[i][j].setBackground(Color.decode("#1d3c45"));
                myButtons[i][j].setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
                myButtons[i][j].setEnabled(false);

                opponentButtons[i][j] = new MyButton("  ");
                opponentButtons[i][j].setSize(opponentButtons[i][j].getPreferredSize());
                opponentButtons[i][j].setBackground(Color.decode("#1d3c45"));
                opponentButtons[i][j].setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
                opponentButtons[i][j].row = i;
                opponentButtons[i][j].column = j;

                opponentButtons[i][j].addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent arg0) {
                        MyButton tempButton = (MyButton) arg0.getSource();
                        int row = tempButton.row;
                        int column = tempButton.column;
                        Shoot shoot = null;
                        if (Start.mode == Mode.hotSeat1)
                            if (Start.playerOne.opponentBoard.canIShootThere(row, column)) {

                                shoot = new Shoot(row, column);
                                shoot.setResult(Start.playerTwo.myBoard.resolveShoot(shoot));
                                Start.playerOne.uploadResult(shoot);

                                if (shoot.getResult() != FieldState.missed)
                                    if (Start.playerTwo.isDead()) {
                                        paintBoards();
                                        for (int i = 0; i < boardSize; i++)
                                            for (int j = 0; j < boardSize; j++)
                                                opponentButtons[i][j].setEnabled(false);
                                        JOptionPane.showMessageDialog(null, Start.currentMenu.textFieldList[0].getText() + " A GAGNÉ");
                                    }
                                if (shoot.getResult() == FieldState.sunk)
                                    sunkShips = sunkShips + 1;
                            }
                        if (Start.mode == Mode.hotSeat2)
                            if (Start.playerTwo.opponentBoard.canIShootThere(row, column)) {

                                shoot = new Shoot(row, column);
                                shoot.setResult(Start.playerOne.myBoard.resolveShoot(shoot));
                                Start.playerTwo.uploadResult(shoot);

                                if (shoot.getResult() != FieldState.missed)
                                    if (Start.playerOne.isDead()) {
                                        paintBoards();
                                        for (int i = 0; i < boardSize; i++)
                                            for (int j = 0; j < boardSize; j++)
                                                opponentButtons[i][j].setEnabled(false);
                                        JOptionPane.showMessageDialog(null, Start.currentMenu.textFieldList[1].getText() + " A GAGNÉ");
                                    }
                                if (shoot.getResult() == FieldState.sunk)
                                    sunkShips = sunkShips + 1;
                            }
                        paintBoards();
                        if (shoot != null && shoot.getResult() == FieldState.missed)
                            Start.changer = new ChangeView(yourSunk, sunkShips);

                    }

                });

                char letter = (char) (i + 'a');
                String string = new StringBuilder().append(letter).toString();

                myLabels[0][i] = new JLabel(string);
                myLabels[1][i] = new JLabel(new Integer(i + 1).toString());

                opponentLabels[0][i] = new JLabel(string);
                opponentLabels[1][i] = new JLabel(new Integer(i + 1).toString());
            }
        paintBoards();
    }
}
