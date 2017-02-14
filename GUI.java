
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.net.InetAddress;
import java.net.UnknownHostException;



public class GUI {

    BufferedReader in;
    PrintWriter out;
    JFrame guiFrame = new JFrame("Peer To Peer Chat");
    JTextField text = new JTextField(40);
    JTextArea ChatBox = new JTextArea(8, 40);
    public static String ip;
    public static Socket socket;


    public GUI() {

        //GUI
        text.setEditable(false);
        ChatBox.setEditable(false);
        guiFrame.getContentPane().add(text, "North");
        guiFrame.getContentPane().add(new JScrollPane(ChatBox), "Center");
        guiFrame.pack();


        text.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                out.println(text.getText());
                text.setText("");
            }
        });
    }

    public String getServerAddress() {
        return JOptionPane.showInputDialog(
            guiFrame,
            "Enter An IP Address: ");
  }

    public String getName() {
        return JOptionPane.showInputDialog(
            guiFrame,
            "Enter A User Name");
    }

    public void run() throws IOException {
        if(socket == null)
        System.out.println("Socket is open");
        String serverAddress = getServerAddress();
        socket = new Socket(serverAddress, 6969);
        if(socket != null){
        System.out.println("Socket is closed");

        }
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            String line = in.readLine();
            if (line.startsWith("nameinput")) {
                out.println(getName());
            } else if (line.startsWith("acceptname")) {
                text.setEditable(true);
            } else if (line.startsWith("message")) {
                CheckSystemMessage(line);
                ChatBox.append(line.substring(8) + "\n");
            }
        }
    }

    public int CheckSystemMessage(String line){
      char c = 'z';
      int i = 8;
      while(c != ':'){
          c = line.charAt(i);
          //System.out.println(c);
          i++;
      }
      i+=1;
        //System.out.println(line.charAt(i));
      if(line.charAt(i) == '/'){
          //System.out.println("Escape Message Detected");
          //System.out.println(line.substring(i));
        if(line.substring(i+1).startsWith("leave"))
            //System.out.println("Leave detected");
          System.exit(0);
        else
          ChatBox.append("Escape message not valid \n");
        }
        //System.out.println(i);
        return 1;
    }

    public static void main(String[] args) throws Exception {
        GUI client = new GUI();
          try {
              ip = InetAddress.getLocalHost().getHostAddress();
              System.out.println("Your current IP address : " + ip);


          } catch (UnknownHostException e) {

              e.printStackTrace();
          }
        client.guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.guiFrame.setVisible(true);
        client.run();
    }
}
