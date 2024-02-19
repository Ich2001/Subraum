package Client;

import javax.swing.*; 
import java.io.*;
import java.net.Socket;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ChatClient  extends JFrame implements KeyListener
{
    private String uname;
    private String address;
    private int port;
    private Socket connectionToServer;

    public BufferedReader fromServerReader;
    public PrintWriter   toServerWriter;

    public JTextArea outputTextArea;
    public JTextField inputTextField;
    public JScrollPane outputScrollPane;


    private void initGui()
    {
      outputTextArea = new JTextArea();
      outputTextArea.setEditable(false);
      outputTextArea.setBorder(BorderFactory.createTitledBorder("Chat"));
      
      outputScrollPane= new JScrollPane(outputTextArea);

      inputTextField = new JTextField();
      inputTextField.setBorder(BorderFactory.createTitledBorder("Eingabe"));
      inputTextField.addKeyListener(this);

      add(outputScrollPane,BorderLayout.CENTER);
      add(inputTextField,BorderLayout.SOUTH);

      setVisible(true);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setSize(400,300);
      setLocale(null);

    }

    public ChatClient(int port) {
      super("ComCave Subraum Komunikator"); 
      this.port  = port ;
    ;
      address =JOptionPane.showInputDialog("Ip ? ","10.6.1.108");
       // uname =  JOptionPane.showInputDialog("Username:  ",  System.getProperty("user.name"));
      uname = System.getProperty("user.name");               
      if(address !=null)
      {
        receiveMassages(); 
      }
       
    }

    private void receiveMassages() {

     try {
        connectionToServer = new Socket(address,port);
        fromServerReader  =new BufferedReader(new InputStreamReader(connectionToServer.getInputStream()));
        toServerWriter = new PrintWriter(new OutputStreamWriter(connectionToServer.getOutputStream() ));
        initGui();
  
       while(true)
       {
        String imsg = fromServerReader.readLine();
        outputTextArea.append(imsg+ "\n");
        outputScrollPane.getVerticalScrollBar().setValue(outputScrollPane.getVerticalScrollBar().getMaximum());
        //Toolkit.getDefaultToolkit().beep();
       }
    } catch (IOException e) {
          JOptionPane.showMessageDialog(null, "Verbinung zum server \"" + address +"\" fehlgeschlagen..");
         dispose();
      } finally{

      if(fromServerReader != null)
      try {
        fromServerReader.close();
      } catch (IOException e) {
       
        e.printStackTrace();
      }

      if(connectionToServer != null)
      try {
        connectionToServer.close();
      } catch (IOException e) {
       
        e.printStackTrace();
      }

      toServerWriter.close();
    }
       
    }

    public static void main(String[] args) {

         new ChatClient(3141);
    }

    @Override
    public void keyTyped(KeyEvent e) {
     
    }

    @Override
    public void keyPressed(KeyEvent e) {
     if(e.getKeyCode() == KeyEvent.VK_ENTER) {
      String msg = inputTextField.getText();
      if (!msg.isEmpty()) {
       toServerWriter.println( uname +": "+ msg );
       toServerWriter.flush();
       inputTextField.setText("");
      }
     }
      
    }

    @Override
    public void keyReleased(KeyEvent e) {
     
    }
    
}
