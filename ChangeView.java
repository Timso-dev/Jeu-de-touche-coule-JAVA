import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeView {
    private JPanel panel;

    public ChangeView(int mySunk, int yourSunk) {

        Start.firstPanel.setVisible(false);
        Start.secondPanel.setVisible(false);
        Start.thirdPanel.setVisible(false);
        Start.fourthPanel.setVisible(false);
        if (Start.mode == Mode.hotSeat1)
            Start.mode = Mode.hotSeat2;
        else
            Start.mode = Mode.hotSeat1;

        JLabel missed = new JLabel(" Vous avez raté ! ");
        missed.setBackground(Color.decode("#1d3c45"));
        missed.setForeground(Color.decode("#d2601a"));
        JLabel text = new JLabel();
        text.setBackground(Color.decode("#1d3c45"));
        text.setForeground(Color.decode("#d2601a"));
        if (Start.mode == Mode.hotSeat2)
            text.setText(" C'est au tour de : " + Start.currentMenu.textFieldList[1].getText() + " " );
        else
            text.setText(" C'est au tour de : " + Start.currentMenu.textFieldList[0].getText() + " ");
        text.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        missed.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints frameLayout = new GridBagConstraints();

        frameLayout.gridwidth = 2;
        frameLayout.fill = GridBagConstraints.BOTH;

        Start.mainFrame.getContentPane().add(panel, frameLayout);

        panel.add(missed, frameLayout);

        frameLayout.gridy = 1;

        panel.add(text, frameLayout);

        frameLayout.gridy = 2;

        JButton button = new JButton("OK");
        button.setBackground(Color.decode("#d2601a"));

        panel.add(button, frameLayout);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                panel.setVisible(false);
                Start.currentGame = (NewGame) new HotSeatView(mySunk, yourSunk);

            }

        });
    }
}
