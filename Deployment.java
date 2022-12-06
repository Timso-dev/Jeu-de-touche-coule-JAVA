import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Deployment {

    private int boardSize;
    private MyButton buttons[][];
    private JLabel labels[][];
    private JButton buttonOK;
    private JButton buttonMenu;
    private Gameboard board;
    private JRadioButton bigShip;
    private JRadioButton mediumShip;
    private JRadioButton smallShip;
    private JRadioButton tinyShip;
    private JComboBox<String> orient;
    private int tinyCount = 0;
    private int smallCount = 0;
    private int mediumCount = 0;
    private int bigCount = 0;
    private JButton randomBoard;

    public Deployment(int size) {
        Start.currentMenu.menuPanel.setVisible(false);
        if (Start.mode == Mode.hotSeat2) {
            Start.firstPanel.setVisible(false);
            Start.secondPanel.setVisible(false);
            Start.thirdPanel.setVisible(false);
            Start.fourthPanel.setVisible(false);
        }
        boardSize = size;
        board = new Gameboard(boardSize);
        setPanels();
        buttons = new MyButton[boardSize][boardSize];
        labels = new JLabel[2][boardSize];
        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j] = new MyButton(" ");
                buttons[i][j].row = i;
                buttons[i][j].column = j;
                buttons[i][j].setBackground(Color.decode("#1d3c45"));
                buttons[i][j].setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
                buttons[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        MyButton tempButton = (MyButton) arg0.getSource();
                        Orientation orientation;
                        if (orient.getSelectedIndex() == 0)
                            orientation = Orientation.vertical;
                        else
                            orientation = Orientation.horizontal;
                        if (bigShip.isSelected()) {
                            if (board.tryAddBigShip(tempButton.row, tempButton.column, orientation)) {
                                bigCount++;
                                randomBoard.setEnabled(false);
                            }
                            if (bigCount == Start.bigShipsCount) {
                                bigShip.setEnabled(false);
                                bigShip.setSelected(false);

                                if (tinyCount != Start.tinyShipsCount)
                                    tinyShip.setSelected(true);
                                if (smallCount != Start.smallShipsCount)
                                    smallShip.setSelected(true);
                                if (mediumCount != Start.mediumShipsCount)
                                    mediumShip.setSelected(true);
                            }
                        } else if (mediumShip.isSelected()) {
                            if (board.tryAddMediumShip(tempButton.row, tempButton.column, orientation)) {
                                mediumCount++;
                                randomBoard.setEnabled(false);
                            }
                            if (mediumCount == Start.mediumShipsCount) {
                                mediumShip.setEnabled(false);
                                mediumShip.setSelected(false);

                                if (tinyCount != Start.tinyShipsCount)
                                    tinyShip.setSelected(true);
                                if (smallCount != Start.smallShipsCount)
                                    smallShip.setSelected(true);
                                if (bigCount != Start.bigShipsCount)
                                    bigShip.setSelected(true);
                            }
                        } else if (smallShip.isSelected()) {
                            if (board.tryAddSmallShip(tempButton.row, tempButton.column, orientation)) {
                                smallCount++;
                                randomBoard.setEnabled(false);
                            }
                            if (smallCount == Start.smallShipsCount) {
                                smallShip.setEnabled(false);
                                smallShip.setSelected(false);

                                if (tinyCount != Start.tinyShipsCount)
                                    tinyShip.setSelected(true);
                                if (mediumCount != Start.mediumShipsCount)
                                    mediumShip.setSelected(true);
                                if (bigCount != Start.bigShipsCount)
                                    bigShip.setSelected(true);
                            }
                        } else if (tinyShip.isSelected()) {
                            if (board.tryAddTinyShip(tempButton.row, tempButton.column,orientation)) {
                                tinyCount++;
                                randomBoard.setEnabled(false);
                            }
                            if (tinyCount == Start.tinyShipsCount) {
                                tinyShip.setEnabled(false);
                                tinyShip.setSelected(false);

                                if (smallCount != Start.smallShipsCount)
                                    smallShip.setSelected(true);
                                if (mediumCount != Start.mediumShipsCount)
                                    mediumShip.setSelected(true);
                                if (bigCount != Start.bigShipsCount)
                                    bigShip.setSelected(true);
                            }
                        }

                        int having = tinyCount + smallCount + mediumCount + bigCount;
                        int required = Start.bigShipsCount + Start.mediumShipsCount + Start.smallShipsCount + Start.tinyShipsCount;
                        if (having == required) {
                            buttonOK.setEnabled(true);
                            for (int i = 0; i < boardSize; i++)
                                for (int j = 0; j < boardSize; j++)
                                    buttons[i][j].setEnabled(false);
                        }
                        paintButtons();
                    }

                });

                char letter = (char) (i + 'a');
                String string = new StringBuilder().append(letter).toString();
                labels[0][i] = new JLabel(string);
                labels[1][i] = new JLabel(new Integer(i + 1).toString());
            }
        paintButtons();
    }

    private void setPanels() {
        Start.firstPanel = new JPanel(new GridBagLayout());
        Start.secondPanel = new JPanel(new GridBagLayout());
        Start.thirdPanel = new JPanel(new GridBagLayout());
        Start.fourthPanel = new JPanel(new GridBagLayout());
        Start.firstPanel.setBackground(Color.decode("#1d3c45"));
        Start.secondPanel.setBackground(Color.decode("#1d3c45"));
        Start.thirdPanel.setBackground(Color.decode("#1d3c45"));
        Start.fourthPanel.setBackground(Color.decode("#1d3c45"));

        Start.firstPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        Start.secondPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        Start.thirdPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        Start.fourthPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        Start.mainFrame.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints frameLayout = new GridBagConstraints();

        frameLayout.gridwidth = 2;
        frameLayout.fill = GridBagConstraints.BOTH;
        frameLayout.weightx = 0.1;
        frameLayout.weighty = 0.1;
        Start.mainFrame.getContentPane().add(Start.firstPanel, frameLayout);
        frameLayout.weightx = 1.0;
        frameLayout.weighty = 1.0;
        frameLayout.gridy = 1;
        frameLayout.gridwidth = 1;
        Start.mainFrame.getContentPane().add(Start.secondPanel, frameLayout);
        frameLayout.gridx = 1;
        Start.mainFrame.getContentPane().add(Start.thirdPanel, frameLayout);
        frameLayout.weightx = 0.1;
        frameLayout.weighty = 0.1;
        frameLayout.gridwidth = 2;
        frameLayout.gridy = 2;
        frameLayout.gridx = 0;
        Start.mainFrame.getContentPane().add(Start.fourthPanel, frameLayout);
        Start.mainFrame.repaint();

        JLabel name = new JLabel("Déploiement");
        name.setForeground(Color.decode("#d2601a"));
        if (Start.mode == Mode.hotSeat1)
            name.setText("Déploiement " + Start.currentMenu.textFieldList[0].getText());
        if (Start.mode == Mode.hotSeat2)
            name.setText("Déploiement " + Start.currentMenu.textFieldList[1].getText());
        buttonOK = new JButton("OK");
        buttonOK.setBackground(Color.decode("#d2601a"));
        buttonOK.setEnabled(false);
        buttonMenu = new JButton("Quitter");
        buttonMenu.setBackground(Color.decode("#d2601a"));
        name.setFont(new Font("SansSerif", Font.PLAIN, 20));
        GridBagConstraints layout = new GridBagConstraints();
        layout.anchor = GridBagConstraints.WEST;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.insets = new Insets(5, 5, 5, 5);
        Start.firstPanel.add(name, layout);
        layout.anchor = GridBagConstraints.EAST;
        Start.fourthPanel.add(buttonOK, layout);
        layout.weightx = 0;
        layout.weighty = 0;
        layout.gridx = 1;
        Start.fourthPanel.add(buttonMenu, layout);

        buttonMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int pick = JOptionPane.showConfirmDialog(
                        null, "Voulez-vous vraiment arrêter?",
                        "Quitter",
                        JOptionPane.YES_NO_OPTION);
                if (pick == JOptionPane.YES_OPTION) {
                    if (Start.mode == Mode.onlineSerwer)
                        Start.serwer.closeSerwerConnection();
                    if (Start.mode == Mode.onlineClient)
                        Start.klient.closeKlientConnection();
                    Start.firstPanel.setVisible(false);
                    Start.secondPanel.setVisible(false);
                    Start.thirdPanel.setVisible(false);
                    Start.fourthPanel.setVisible(false);
                    Start.currentMenu = new MainMenu();
                    Start.mainFrame.repaint();
                }
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Start.mode == Mode.hotSeat1) {
                    Start.mode = Mode.hotSeat2;
                    Start.playerTwo = new HumanPlayer(10);
                } else if (Start.mode == Mode.singleEasy || Start.mode == Mode.singleHard || Start.mode == Mode.singleMedium || Start.mode == Mode.singleUltra)
                    Start.currentGame = new NewGame();
                else if (Start.mode == Mode.hotSeat2)
                    Start.currentGame = (NewGame) new HotSeatView(0, 0);
                else if (Start.mode == Mode.onlineClient) {
                    Start.currentGame = (NewGame) new OnlineGame();
                    OnlineGame temp = (OnlineGame) Start.currentGame;
                    temp.Responder();
                } else if (Start.mode == Mode.onlineSerwer)
                    Start.currentGame = (NewGame) new OnlineGame();
            }
        });

        // Right Panel
        randomBoard = new JButton("Déploiement Aléatoire");
        randomBoard.setBackground(Color.decode("#d2601a"));
        randomBoard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                board.setRandom();
                randomBoard.setEnabled(false);
                bigCount = Start.bigShipsCount;
                mediumCount = Start.mediumShipsCount;
                smallCount = Start.smallShipsCount;
                tinyCount = Start.tinyShipsCount;
                buttonOK.setEnabled(true);
                for (int i = 0; i < boardSize; i++)
                    for (int j = 0; j < boardSize; j++)
                        buttons[i][j].setEnabled(false);

                bigShip.setEnabled(false);
                mediumShip.setEnabled(false);
                smallShip.setEnabled(false);
                tinyShip.setEnabled(false);
                paintButtons();
            }

        });

        ButtonGroup shipChoose = new ButtonGroup();
        bigShip = new JRadioButton("Porte Avion (5 cases) x" + Start.bigShipsCount, true);
        mediumShip = new JRadioButton("Croiseur (4 cases) x" + Start.mediumShipsCount, false);
        smallShip = new JRadioButton("Sous-Marin (3 cases) x" + Start.smallShipsCount, false);
        tinyShip = new JRadioButton("Torpilleur (2 cases) x" + Start.tinyShipsCount, false);
        bigShip.setBackground(Color.decode("#d2601a"));
        mediumShip.setBackground(Color.decode("#d2601a"));
        smallShip.setBackground(Color.decode("#d2601a"));
        tinyShip.setBackground(Color.decode("#d2601a"));
        shipChoose = new ButtonGroup();
        shipChoose.add(tinyShip);
        shipChoose.add(smallShip);
        shipChoose.add(mediumShip);
        shipChoose.add(bigShip);
        layout.gridx = 0;
        layout.gridy = 0;
        layout.weightx = 1;
        layout.weighty = 1;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        Start.thirdPanel.add(bigShip, layout);
        layout.gridy = 1;
        Start.thirdPanel.add(mediumShip, layout);
        layout.gridy = 2;
        Start.thirdPanel.add(smallShip, layout);
        layout.gridy = 3;
        Start.thirdPanel.add(tinyShip, layout);
        String[] names = {"Verticale", "Horizontale"};
        orient = new JComboBox<String>(names);
        orient.setBackground(Color.decode("#d2601a"));
        layout.gridy = 4;
        Start.thirdPanel.add(orient, layout);
        layout.gridy = 5;
        layout.insets = new Insets(0, 0, 200, 0);
        Start.thirdPanel.add(randomBoard, layout);

    }

    private void paintButtons() {
        Start.secondPanel.removeAll();
        Start.secondPanel.repaint();
        GridBagConstraints layout = new GridBagConstraints();
        layout.insets = new Insets(1, 1, 1, 1);
        layout.anchor = GridBagConstraints.CENTER;
        layout.fill = GridBagConstraints.BOTH;

        for (int i = 1; i < boardSize + 1; i++) {
            layout.gridy = i;
            Start.secondPanel.add(labels[1][i - 1], layout);
            labels[1][i - 1].setForeground(Color.decode("#d2601a"));
        }
        layout.gridy = 0;
        for (int i = 1; i < boardSize + 1; i++) {
            layout.gridx = i;
            Start.secondPanel.add(labels[0][i - 1], layout);
            labels[0][i - 1].setForeground(Color.decode("#d2601a"));
        }

        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++) {
                layout.gridx = j + 1;
                layout.gridy = i + 1;
                if (board.getFieldState(i, j) == FieldState.taken)
                    buttons[i][j].setBackground(new Color(0, 255, 0));
                Start.secondPanel.add(buttons[i][j], layout);
            }
    }

    public Gameboard returnBoard() {
        return this.board;
    }

}
