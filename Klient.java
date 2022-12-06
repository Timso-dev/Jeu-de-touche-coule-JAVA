import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class Klient {
    public Socket socket;
    private int port;
    private String ip;

    public Klient() {
        ip = Start.currentMenu.textFieldList[0].getText();
        port = Integer.parseInt(Start.currentMenu.textFieldList[1].getText());
        Start.mode = Mode.onlineClient;
        try {
            socket = new Socket(ip, port);
            JOptionPane.showConfirmDialog(Start.mainFrame, "Connecté avec l'adversaire!",
                                          "Succès", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

            Start.playerOne = (Player) new HumanPlayer(10);
        } catch (IOException e) {
            closeKlientConnection();
        }
    }

    public void closeKlientConnection() {
        JOptionPane.showConfirmDialog(Start.mainFrame, "Impossible de se connecter avec l'adversaire !",
                                      "Erreur", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

        Start.currentMenu = new MainMenu();
        Start.mainFrame.repaint();
    }
}
