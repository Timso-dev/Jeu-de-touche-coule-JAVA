import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Servertexte {

  private int port;
  private List<User> clients;
  private ServerSocket servertexte;

  public static void main(String[] args) throws IOException {
    new Servertexte(12345).run();
  }

  public Servertexte(int port) {
    this.port = port;
    this.clients = new ArrayList<User>();
  }

  public void run() throws IOException {
    servertexte = new ServerSocket(port) {
      protected void finalize() throws IOException {
        this.close();
      }
    };
    System.out.println("Port 12345 is now open.");

    while (true) {
      // accepts a new client
      Socket client = servertexte.accept();

      // get nickname of newUser
      String nickname = (new Scanner ( client.getInputStream() )).nextLine();
      nickname = nickname.replace(",", ""); //  ',' use for serialisation
      nickname = nickname.replace(" ", "_");
      System.out.println("New Client: \"" + nickname + "\"\n\t     Host:" + client.getInetAddress().getHostAddress());

      // create new User
      User newUser = new User(client, nickname);

      // add newUser message to list
      this.clients.add(newUser);

      // Welcome msg
      newUser.getOutStream().println("<b>Bienvenue</b> " + newUser.toString() );

      // create a new thread for newUser incoming messages handling
      new Thread(new UserHandler(this, newUser)).start();
    }
  }

  // delete a user from the list
  public void removeUser(User user){
    this.clients.remove(user);
  }

  // send incoming msg to all Users
  public void broadcastMessages(String msg, User userSender) {
    for (User client : this.clients) {
      client.getOutStream().println(
              userSender.toString() + "<span>: " + msg+"</span>");
    }
  }

  // send list of clients to all Users
  public void broadcastAllUsers(){
    for (User client : this.clients) {
      client.getOutStream().println(this.clients);
    }
  }

  // send message to a User (String)
  public void sendMessageToUser(String msg, User userSender, String user){
    boolean find = false;
    for (User client : this.clients) {
      if (client.getNickname().equals(user) && client != userSender) {
        find = true;
        userSender.getOutStream().println(userSender.toString() + " -> " + client.toString() +": " + msg);
        client.getOutStream().println(
                "(<b>Private</b>)" + userSender.toString() + "<span>: " + msg+"</span>");
      }
    }
    if (!find) {
      userSender.getOutStream().println(userSender.toString() + " -> (<b>no one!</b>): " + msg);
    }
  }
}

class UserHandler implements Runnable {

  private Servertexte servertexte;
  private User user;

  public UserHandler(Servertexte servertexte, User user) {
    this.servertexte = servertexte;
    this.user = user;
    this.servertexte.broadcastAllUsers();
  }

  public void run() {
    String message;

    // when there is a new message, broadcast to all
    Scanner sc = new Scanner(this.user.getInputStream());
    while (sc.hasNextLine()) {
      message = sc.nextLine();

      // smiley
      message = message.replace(":)", "<img src='http://4.bp.blogspot.com/-ZgtYQpXq0Yo/UZEDl_PJLhI/AAAAAAAADnk/2pgkDG-nlGs/s1600/facebook-smiley-face-for-comments.png'>");
      message = message.replace(":D", "<img src='http://2.bp.blogspot.com/-OsnLCK0vg6Y/UZD8pZha0NI/AAAAAAAADnY/sViYKsYof-w/s1600/big-smile-emoticon-for-facebook.png'>");
      message = message.replace(":d", "<img src='http://2.bp.blogspot.com/-OsnLCK0vg6Y/UZD8pZha0NI/AAAAAAAADnY/sViYKsYof-w/s1600/big-smile-emoticon-for-facebook.png'>");
      message = message.replace(":(", "<img src='http://2.bp.blogspot.com/-rnfZUujszZI/UZEFYJ269-I/AAAAAAAADnw/BbB-v_QWo1w/s1600/facebook-frown-emoticon.png'>");
      message = message.replace("-_-", "<img src='http://3.bp.blogspot.com/-wn2wPLAukW8/U1vy7Ol5aEI/AAAAAAAAGq0/f7C6-otIDY0/s1600/squinting-emoticon.png'>");
      message = message.replace(";)", "<img src='http://1.bp.blogspot.com/-lX5leyrnSb4/Tv5TjIVEKfI/AAAAAAAAAi0/GR6QxObL5kM/s400/wink%2Bemoticon.png'>");
      message = message.replace(":P", "<img src='http://4.bp.blogspot.com/-bTF2qiAqvi0/UZCuIO7xbOI/AAAAAAAADnI/GVx0hhhmM40/s1600/facebook-tongue-out-emoticon.png'>");
      message = message.replace(":p", "<img src='http://4.bp.blogspot.com/-bTF2qiAqvi0/UZCuIO7xbOI/AAAAAAAADnI/GVx0hhhmM40/s1600/facebook-tongue-out-emoticon.png'>");
      message = message.replace(":o", "<img src='http://1.bp.blogspot.com/-MB8OSM9zcmM/TvitChHcRRI/AAAAAAAAAiE/kdA6RbnbzFU/s400/surprised%2Bemoticon.png'>");
      message = message.replace(":O", "<img src='http://1.bp.blogspot.com/-MB8OSM9zcmM/TvitChHcRRI/AAAAAAAAAiE/kdA6RbnbzFU/s400/surprised%2Bemoticon.png'>");

      // Gestion des messages private
      if (message.charAt(0) == '@'){
        if(message.contains(" ")){
          System.out.println("private msg : " + message);
          int firstSpace = message.indexOf(" ");
          String userPrivate= message.substring(1, firstSpace);
          servertexte.sendMessageToUser(
                  message.substring(
                          firstSpace+1, message.length()
                  ), user, userPrivate
          );
        }

        // Gestion du changement
      }else if (message.charAt(0) == '#'){
        user.changeColor(message);
        // update color for all other users
        this.servertexte.broadcastAllUsers();
      } else if (message.contains("/smiley")) {
        String msg = "\nSmileys disponible :\n" +
                ":)\n" +
                ":D\n" +
                ":d\n" +
                ":C\n" +
                "-_-\n" +
                ";)\n" +
                ":P\n" +
                ":p\n" +
                ":o\n" +
                ":0";
        servertexte.sendMessageToUser(msg, user, user.getNickname());
      }
      else if (message.contains("/color")) {
        String msg = "\ncouleur disponible :\n" +
                "#3079ab => dark blue\n" +
                "#e15258 => red\n" +
                "#f9845b => orange\n" +
                "#7d669e => purple\n" +
                "#53bbb4 => aqua\n" +
                "#51b46d => green\n" +
                "#e0ab18 => mustard\n" +
                "#f092b0 => pink\n" +
                "#e39e54 => orange\n" +
                "#d64d4d => red\n" +
                "#4d7358 => green";
        servertexte.sendMessageToUser(msg, user, user.getNickname());
      } else if (message.contains("/règles")) {
        String msg = "\n2 joueurs face-à-face doivent placer leurs « navires », horizontalement ou verticalement, \n" +
                "sur leur propre grille qui reste tenue secrète et tenter de toucher les « navires » du joueur adverse.  \n" +
                "Le gagnant est celui qui parvient à couler tous les navires de l'adversaire avant que tous les siens ne le soient.";
        servertexte.sendMessageToUser(msg, user, user.getNickname());
      }
      else{
        // update user list
        servertexte.broadcastMessages(message, user);
      }
    }
    // end of Thread
    servertexte.removeUser(user);
    this.servertexte.broadcastAllUsers();
    sc.close();
  }
}

class User {
  private static int nbUser = 0;
  private int userId;
  private PrintStream streamOut;
  private InputStream streamIn;
  private String nickname;
  private Socket client;
  private String color;

  // constructor
  public User(Socket client, String name) throws IOException {
    this.streamOut = new PrintStream(client.getOutputStream());
    this.streamIn = client.getInputStream();
    this.client = client;
    this.nickname = name;
    this.userId = nbUser;
    this.color = ColorInt.getColor(this.userId);
    nbUser += 1;
  }

  public String getnickname() {
    return this.nickname;
  }

  // change color user
  public void changeColor(String hexColor){
    // check if it's a valid hexColor
    Pattern colorPattern = Pattern.compile("#([0-9a-f]{3}|[0-9a-f]{6}|[0-9a-f]{8})");
    Matcher m = colorPattern.matcher(hexColor);
    if (m.matches()){
      Color c = Color.decode(hexColor);
      // if the Color is too Bright don't change
      double luma = 0.2126 * c.getRed() + 0.7152 * c.getGreen() + 0.0722 * c.getBlue(); // per ITU-R BT.709
      if (luma > 160) {
        this.getOutStream().println("<b>Color Too Bright</b>");
        return;
      }
      this.color = hexColor;
      this.getOutStream().println("<b>Color changed successfully</b> " + this.toString());
      return;
    }
    this.getOutStream().println("<b>Failed to change color</b>");
  }

  // getteur
  public PrintStream getOutStream(){
    return this.streamOut;
  }

  public InputStream getInputStream(){
    return this.streamIn;
  }

  public String getNickname(){
    return this.nickname;
  }

  // print user with his color
  public String toString(){

    return "<u><span style='color:"+ this.color
            +"'>" + this.getNickname() + "</span></u>";

  }

}

class ColorInt {
  public static String[] mColors = {
          "#3079ab", // dark blue
          "#e15258", // red
          "#f9845b", // orange
          "#7d669e", // purple
          "#53bbb4", // aqua
          "#51b46d", // green
          "#e0ab18", // mustard
          "#f092b0", // pink
          "#e39e54", // orange
          "#d64d4d", // red
          "#4d7358", // green
  };

  public static String getColor(int i) {
    return mColors[i % mColors.length];
  }
}


