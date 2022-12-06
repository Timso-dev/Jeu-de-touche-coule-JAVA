import java.awt.event.ActionEvent;

public class MultiplayerMenu extends Menu {

    private static String[] buttons = {"Créer Une Partie Online", "Rejoindre Une Partie Online", "Écran Scindé", "Retour"};
    private static String[] textFields = {};

    public MultiplayerMenu() {
        super(buttons, textFields);
        EventHandler menuHandler = new MenuEventHandler();
        for (int i = 0; i < 4; i++)
            menuButtonList[i].addActionListener(menuHandler);
    }

    class MenuEventHandler extends Menu.EventHandler {

        public void actionPerformed(ActionEvent event) {
            Start.currentMenu.menuPanel.setVisible(false);
            if (event.getSource() == Menu.menuButtonList[0])
                Start.currentMenu = new ServerMenu();
            else if (event.getSource() == Menu.menuButtonList[1])
                Start.currentMenu = new ClientMenu();
            else if (event.getSource() == Menu.menuButtonList[2])
                Start.currentMenu = new HotSeatMenu();
            else if (event.getSource() == Menu.menuButtonList[3])
                Start.currentMenu = new MainMenu();

            Start.mainFrame.invalidate();
            Start.mainFrame.validate();
        }
    }

}
