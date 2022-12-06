import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class OnlineGame extends NewGame {

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    public Thread listener;

    public OnlineGame() {
        this.sunkShips = 0;
        if (Start.mode == Mode.onlineClient)
            Start.myTurn = false;
        if (Start.mode == Mode.onlineSerwer)
            Start.myTurn = true;

        if (Start.mode == Mode.onlineClient)
            try {
                outputStream = new ObjectOutputStream(Start.klient.socket.getOutputStream());
                outputStream.flush();
                inputStream = new ObjectInputStream(Start.klient.socket.getInputStream());
            } catch (IOException e) {
                Start.klient.closeKlientConnection();
            }
        else
            try {
                outputStream = new ObjectOutputStream(Start.serwer.socket.getOutputStream());
                outputStream.flush();
                inputStream = new ObjectInputStream(Start.serwer.socket.getInputStream());
            } catch (IOException e) {
                Start.serwer.closeSerwerConnection();
            }
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
                myButtons[i][j].setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
                myButtons[i][j].setEnabled(false);

                opponentButtons[i][j] = new MyButton("  ");
                opponentButtons[i][j].setSize(opponentButtons[i][j].getPreferredSize());
                opponentButtons[i][j].setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
                opponentButtons[i][j].row = i;
                opponentButtons[i][j].column = j;

                opponentButtons[i][j].addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent arg0) {
                        MyButton tempButton = (MyButton) arg0.getSource();
                        int row = tempButton.row;
                        int column = tempButton.column;
                        Shoot shoot;
                        if (Start.playerOne.opponentBoard.canIShootThere(row, column)) {
                            shoot = new Shoot(row, column);
                            try {
                                outputStream.writeObject(shoot);
                                outputStream.flush();
                                shoot = (Shoot) inputStream.readObject();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Start.playerOne.uploadResult(shoot);
                            if (shoot.getResult() != FieldState.missed) {
                                if (shoot.getResult() == FieldState.sunk)
                                    sunkShips = sunkShips + 1;
                                if (sunkShips == 10) {
                                    Start.myTurn = false;
                                    paintBoards();
                                    name.setText("Mode Multijoueur, Tu as Gagné!");
                                    JOptionPane.showMessageDialog(null, "Tu as Gagné");
                                    return;
                                }
                            } else {
                                Start.myTurn = false;
                                Responder();
                            }
                        }
                        paintBoards();
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

    public void Responder() {
        Runnable listening = new Runnable() {
            public void run() {

                Shoot shoot = null;
                while (Start.myTurn == false) {
                    try {
                        shoot = (Shoot) inputStream.readObject();
                        shoot.setResult(Start.playerOne.myBoard.resolveShoot(shoot));
                        paintBoards();
                        Start.mainFrame.repaint();
                        outputStream.writeObject(shoot);
                        outputStream.flush();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (shoot.getResult() != FieldState.missed) {
                        if (Start.playerOne.isDead()) {
                            Start.myTurn = false;
                            paintBoards();
                            name.setText("Mode Multijoueur, Tu as Perdu!");
                            JOptionPane.showMessageDialog(null, "Tu as Perdu");
                            return;
                        }
                    } else {
                        Start.myTurn = true;
                        paintBoards();
                        Start.mainFrame.repaint();
                    }
                }
            }
        };

        OnlineGame temp = (OnlineGame) Start.currentGame;
        temp.listener = new Thread(listening);
        temp.listener.start();

    }
}
