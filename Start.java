import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public class Start {

    public static JPanel firstPanel;
    public static JPanel secondPanel;
    public static JPanel thirdPanel;
    public static JPanel fourthPanel;
    public static JFrame mainFrame;
    public static Menu currentMenu;
    public static int tinyShipsCount = 1;
    public static int smallShipsCount = 2;
    public static int mediumShipsCount = 1;
    public static int bigShipsCount = 1;
    public static Player playerOne;
    public static Player playerTwo;
    public static Mode mode;
    public static NewGame currentGame;
    public static ChangeView changer;
    public static boolean myTurn;
    public static Server serwer;
    public static Klient klient;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainFrame = new JFrame("Touché Coulé");
                Image icon = Toolkit.getDefaultToolkit().getImage("homer.jpg");
                mainFrame.setIconImage(icon);
                mainFrame.getContentPane().setBackground(Color.decode("#1d3c45"));
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setMinimumSize(new Dimension(1024, 600));
                mainFrame.setResizable(true);
                mainFrame.setVisible(false);
                currentMenu = new MainMenu();
                Start.currentMenu.menuPanel.setVisible(false);
                ClientGui clientGui=new ClientGui();
            }
        });

    }

}