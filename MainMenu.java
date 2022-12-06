import java.awt.*;
import java.awt.event.ActionEvent;

public class MainMenu extends Menu {

    private static String[] buttons = {"Mode Solo", "Mode Multijoueur ", "Quitter"};
    private static String[] textFields = {};
    public MainMenu() {
        super(buttons, textFields);

        EventHandler menuHandler = new MenuEventHandler();
        for (int i = 0; i < 3; i++)
            menuButtonList[i].addActionListener(menuHandler);

    }

    class MenuEventHandler extends Menu.EventHandler {

        public void actionPerformed(ActionEvent event) {
            Start.currentMenu.menuPanel.setVisible(false);
            if (event.getSource() == Menu.menuButtonList[0])
                Start.currentMenu = new SingleplayerMenu();
            else if (event.getSource() == Menu.menuButtonList[1])
                Start.currentMenu = new MultiplayerMenu();
            else if (event.getSource() == Menu.menuButtonList[2])
                if (Start.mainFrame.isDisplayable())
                    Start.mainFrame.setVisible(false);
            Start.mainFrame.invalidate();
            Start.mainFrame.validate();
        }
    }

}
