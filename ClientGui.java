import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.html.*;

import java.util.ArrayList;
import java.util.Arrays;


public class ClientGui extends Thread{
  public static JFrame mainFrame;
  public static Menu currentMenu;
  final JTextPane jtextFilDiscu = new JTextPane();
  final JTextPane jtextListUsers = new JTextPane();
  final JTextField jtextInputChat = new JTextField();
  private String oldMsg = "";
  private Thread read;
  private String servertexteName;
  private int PORT;
  private String name;
  BufferedReader input;
  PrintWriter output;
  Socket servertexte;

  public ClientGui() {
    this.servertexteName = "localhost";
    this.PORT = 12345;
    this.name = "Pseudo";

    String fontfamily = "Arial, sans-serif";
    Font font = new Font(fontfamily, Font.PLAIN, 15);

    final JFrame jfr = new JFrame("Touché Coulé");
    Image icon = Toolkit.getDefaultToolkit().getImage("homer.jpg");
    jfr.setIconImage(icon);
    jfr.getContentPane().setLayout(null);
    jfr.setSize(700, 500);
    jfr.setResizable(false);
    jfr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Module du fil de discussion
    jtextFilDiscu.setBounds(25, 25, 490, 320);
    jtextFilDiscu.setFont(font);
    jtextFilDiscu.setMargin(new Insets(6, 6, 6, 6));
    jtextFilDiscu.setEditable(false);
    JScrollPane jtextFilDiscuSP = new JScrollPane(jtextFilDiscu);
    jtextFilDiscuSP.setBounds(25, 25, 490, 320);

    jtextFilDiscu.setContentType("text/html");
    jtextFilDiscu.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

    // Module de la liste des utilisateurs
    jtextListUsers.setBounds(520, 25, 156, 320);
    jtextListUsers.setEditable(true);
    jtextListUsers.setFont(font);
    jtextListUsers.setMargin(new Insets(6, 6, 6, 6));
    jtextListUsers.setEditable(false);
    JScrollPane jsplistuser = new JScrollPane(jtextListUsers);
    jsplistuser.setBounds(520, 25, 156, 320);

    jtextListUsers.setContentType("text/html");
    jtextListUsers.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

    // Field message user input
    jtextInputChat.setBounds(0, 350, 400, 50);
    jtextInputChat.setFont(font);
    jtextInputChat.setMargin(new Insets(6, 6, 6, 6));
    final JScrollPane jtextInputChatSP = new JScrollPane(jtextInputChat);
    jtextInputChatSP.setBounds(25, 350, 650, 50);

    // button send
    final JButton jsbtnsend = new JButton("Envoyer");
    jsbtnsend.setFont(font);
    jsbtnsend.setBounds(526, 410, 150, 35);

    // button play solo
    final JButton jsbtnplaygame = new JButton("Jouer Au Jeu");
    jsbtnplaygame.setFont(font);
    jsbtnplaygame.setBounds(273, 410, 150, 35);



    // button Disconnect
    final JButton jsbtndeco = new JButton("Se Déconnecter");
    jsbtndeco.setFont(font);
    jsbtndeco.setBounds(25, 410, 150, 35);

    jtextInputChat.addKeyListener(new KeyAdapter() {
      // send message on Enter
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          sendMessage();
        }

        // Get last message typed
        if (e.getKeyCode() == KeyEvent.VK_UP) {
          String currentMessage = jtextInputChat.getText().trim();
          jtextInputChat.setText(oldMsg);
          oldMsg = currentMessage;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
          String currentMessage = jtextInputChat.getText().trim();
          jtextInputChat.setText(oldMsg);
          oldMsg = currentMessage;
        }
      }
    });

    // Click on send button
    jsbtnsend.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        sendMessage();
      }
    });

    // Click on play solo button
    jsbtnplaygame.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
          Start.mainFrame.setVisible(true);
          Start.currentMenu.menuPanel.setVisible(true);
      }
    });

    // Connection view
    final JTextField jtfName = new JTextField(this.name);
    final JTextField jtfport = new JTextField(Integer.toString(this.PORT));
    final JTextField jtfAddr = new JTextField(this.servertexteName);
    final JButton jcbtn = new JButton("Se Connecter");

    // check if those field are not empty
    jtfName.getDocument().addDocumentListener(new TextListener(jtfName, jtfport, jtfAddr, jcbtn));
    jtfport.getDocument().addDocumentListener(new TextListener(jtfName, jtfport, jtfAddr, jcbtn));
    jtfAddr.getDocument().addDocumentListener(new TextListener(jtfName, jtfport, jtfAddr, jcbtn));

    // position des Modules
    jcbtn.setFont(font);
    jtfAddr.setBounds(25, 380, 135, 40);
    jtfName.setBounds(365, 380, 135, 40);
    jtfport.setBounds(195, 380, 135, 40);
    jcbtn.setBounds(535, 380, 135, 40);

    // couleur par defaut des Modules fil de discussion et liste des utilisateurs
    jfr.getContentPane().setBackground(Color.decode("#1d3c45"));
    jtextFilDiscu.setBackground(Color.decode("#fff1e1"));
    jtextListUsers.setBackground(Color.decode("#fff1e1"));
    jcbtn.setBackground(Color.decode("#d2601a"));
    jcbtn.setForeground(Color.decode("#141414"));
    jtfName.setBackground(Color.decode("#d2601a"));
    jtfName.setForeground(Color.decode("#141414"));
    jtfport.setBackground(Color.decode("#d2601a"));
    jtfport.setForeground(Color.decode("#141414"));
    jtfAddr.setBackground(Color.decode("#d2601a"));
    jtfAddr.setForeground(Color.decode("#141414"));

    // ajout des éléments
    jfr.add(jcbtn);
    jfr.add(jtextFilDiscuSP);
    jfr.add(jsplistuser);
    jfr.add(jtfName);
    jfr.add(jtfport);
    jfr.add(jtfAddr);
    jfr.setVisible(true);


    // info sur le Chat
    appendToPane(jtextFilDiscu, "<h4>Les commandes possibles dans le chat sont:</h4>"
        +"<ul>"
        +"<li><b>@Pseudo</b> pour envoyer un Message privé à l'utilisateur 'Pseudo'</li>"
        +"<li><b>#d3961b</b> pour changer la couleur de son pseudo</li>\n<b>pour en savoir plus : /color</b>"
        +"<li><b>;)</b> quelques smileys sont implémentés</li>\n<b>pour en savoir plus : /smiley</b>"
        +"<li><b>flèche du haut</b> pour reprendre le dernier message tapé</li>"
            +"<li><b>/règles</b> pour lire les règles du jeu Touché Coulé</li>"
        +"</ul><br/>");

    // On connect
    jcbtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        try {
          name = jtfName.getText();
          String port = jtfport.getText();
          servertexteName = jtfAddr.getText();
          PORT = Integer.parseInt(port);

          appendToPane(jtextFilDiscu, "<span>Connecting to " + servertexteName + " on port " + PORT + "...</span>");
          servertexte = new Socket(servertexteName, PORT);

          appendToPane(jtextFilDiscu, "<span>Connected to " +
                  servertexte.getRemoteSocketAddress()+"</span>");

          input = new BufferedReader(new InputStreamReader(servertexte.getInputStream()));
          output = new PrintWriter(servertexte.getOutputStream(), true);

          // send nickname to servertexte
          output.println(name);

          // create new Read Thread
          read = new Read();
          read.start();
          jfr.remove(jtfName);
          jfr.remove(jtfport);
          jfr.remove(jtfAddr);
          jfr.remove(jcbtn);
          jfr.add(jsbtnsend);
          jfr.add(jtextInputChatSP);
          jfr.add(jsbtnplaygame);
          jfr.add(jsbtndeco);
          jfr.revalidate();
          jfr.repaint();
          jfr.getContentPane().setBackground(Color.decode("#1d3c45"));
          jcbtn.setBackground(Color.decode("#d2601a"));
          jcbtn.setForeground(Color.decode("#141414"));
          jtfName.setBackground(Color.decode("#d2601a"));
          jtfName.setForeground(Color.decode("#141414"));
          jtfport.setBackground(Color.decode("#d2601a"));
          jtfport.setForeground(Color.decode("#141414"));
          jtfAddr.setBackground(Color.decode("#d2601a"));
          jtfAddr.setForeground(Color.decode("#141414"));
          jtextInputChat.setBackground(Color.decode("#fff1e1"));
          jsbtnsend.setBackground(Color.decode("#d2601a"));
          jsbtnsend.setForeground(Color.decode("#141414"));
          jsbtndeco.setBackground(Color.decode("#d2601a"));
          jsbtndeco.setForeground(Color.decode("#141414"));
          jsbtnplaygame.setBackground(Color.decode("#d2601a"));
          jsbtnplaygame.setForeground(Color.decode("#141414"));
          jtextFilDiscu.setBackground(Color.decode("#fff1e1"));
          jtextListUsers.setBackground(Color.decode("#fff1e1"));
        } catch (Exception ex) {
          appendToPane(jtextFilDiscu, "<span>Could not connect to servertexte</span>");
          JOptionPane.showMessageDialog(jfr, ex.getMessage());
        }
      }

    });

    // on deco
    jsbtndeco.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent ae) {
        jfr.add(jtfName);
        jfr.add(jtfport);
        jfr.add(jtfAddr);
        jfr.add(jcbtn);
        jfr.remove(jsbtnsend);
        jfr.remove(jtextInputChatSP);
        jfr.remove(jsbtnplaygame);
        jfr.remove(jsbtndeco);
        jfr.revalidate();
        jfr.repaint();
        read.interrupt();
        jtextListUsers.setText(null);
        jfr.getContentPane().setBackground(Color.decode("#1d3c45"));
        jcbtn.setBackground(Color.decode("#d2601a"));
        jcbtn.setForeground(Color.decode("#141414"));
        jtfName.setBackground(Color.decode("#d2601a"));
        jtfName.setForeground(Color.decode("#141414"));
        jtfport.setBackground(Color.decode("#d2601a"));
        jtfport.setForeground(Color.decode("#141414"));
        jtfAddr.setBackground(Color.decode("#d2601a"));
        jtfAddr.setForeground(Color.decode("#141414"));
        jtextInputChat.setBackground(Color.decode("#fff1e1"));
        jsbtnsend.setBackground(Color.decode("#d2601a"));
        jsbtnsend.setForeground(Color.decode("#141414"));
        jsbtndeco.setBackground(Color.decode("#d2601a"));
        jsbtndeco.setForeground(Color.decode("#141414"));
        jsbtnplaygame.setBackground(Color.decode("#d2601a"));
        jsbtnplaygame.setForeground(Color.decode("#141414"));
        jtextFilDiscu.setBackground(Color.decode("#fff1e1"));
        jtextListUsers.setBackground(Color.decode("#fff1e1"));
        appendToPane(jtextFilDiscu, "<span>Connection closed.</span>");
        output.close();
      }
    });

  }

  // check if if all field are not empty
  public class TextListener implements DocumentListener{
    JTextField jtf1;
    JTextField jtf2;
    JTextField jtf3;
    JButton jcbtn;

    public TextListener(JTextField jtf1, JTextField jtf2, JTextField jtf3, JButton jcbtn){
      this.jtf1 = jtf1;
      this.jtf2 = jtf2;
      this.jtf3 = jtf3;
      this.jcbtn = jcbtn;
    }

    public void changedUpdate(DocumentEvent e) {}

    public void removeUpdate(DocumentEvent e) {
      if(jtf1.getText().trim().equals("") ||
          jtf2.getText().trim().equals("") ||
          jtf3.getText().trim().equals("")
          ){
        jcbtn.setEnabled(false);
      }else{
        jcbtn.setEnabled(true);
      }
    }
    public void insertUpdate(DocumentEvent e) {
      if(jtf1.getText().trim().equals("") ||
          jtf2.getText().trim().equals("") ||
          jtf3.getText().trim().equals("")
          ){
        jcbtn.setEnabled(false);
      }else{
        jcbtn.setEnabled(true);
      }
    }

  }

  // envoi des messages
  public void sendMessage() {
    try {
      String message = jtextInputChat.getText().trim();
      if (message.equals("")) {
        return;
      }
      this.oldMsg = message;
      output.println(message);
      jtextInputChat.requestFocus();
      jtextInputChat.setText(null);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage());
      System.exit(0);
    }
  }

  public static void main(String[] args) throws Exception {
    ClientGui client = new ClientGui();
  }

  // read new incoming messages
  class Read extends Thread {
    public void run() {
      String message;
      while(!Thread.currentThread().isInterrupted()){
        try {
          message = input.readLine();
          if(message != null){
            if (message.charAt(0) == '[') {
              message = message.substring(1, message.length()-1);
              ArrayList<String> ListUser = new ArrayList<String>(
                  Arrays.asList(message.split(", "))
                  );
              jtextListUsers.setText(null);
              for (String user : ListUser) {
                appendToPane(jtextListUsers, "@" + user);
              }
            }else{
              appendToPane(jtextFilDiscu, message);
            }
          }
        }
        catch (IOException ex) {
          System.err.println("Failed to parse incoming message");
        }
      }
    }
  }

  // send html to pane
  private void appendToPane(JTextPane tp, String msg){
    HTMLDocument doc = (HTMLDocument)tp.getDocument();
    HTMLEditorKit editorKit = (HTMLEditorKit)tp.getEditorKit();
    try {
      editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
      tp.setCaretPosition(doc.getLength());
    } catch(Exception e){
      e.printStackTrace();
    }
  }
}
