import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    public Socket socket;
    private int port;

    public Server() {
        port = Integer.parseInt(Start.currentMenu.textFieldList[0].getText());
        Start.mode = Mode.onlineSerwer;
        initializeServer();
        try {
            if (serverSocket != null) {
                socket = serverSocket.accept();
                JOptionPane.showConfirmDialog(Start.mainFrame, "Connecté avec l'adversaire !",
                                              "Succès", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

                Start.playerOne = (Player) new HumanPlayer(10);

            }
        } catch (NullPointerException | IOException e) {
            closeSerwerConnection();
        }
    }

    private void initializeServer() {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeSerwerConnection() {
        try {
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
        }
        JOptionPane.showConfirmDialog(Start.mainFrame, "Impossible de se connecter avec l'adversaire!",
                                      "Erreur", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

        Start.currentMenu = new MainMenu();
        Start.mainFrame.repaint();
    }
}
