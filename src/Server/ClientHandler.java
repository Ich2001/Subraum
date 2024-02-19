package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private ChatServer chatServer;
    private Socket connectionToClient;
    private String name;
    private  BufferedReader fromClientReader;
    private PrintWriter  toClientWriter;


    public ClientHandler (ChatServer chatServer,Socket connectionToClient) 
    {
        this.chatServer =  chatServer;
        this.connectionToClient =connectionToClient;
        name = connectionToClient.getInetAddress().getHostAddress();

        new Thread(this).start();
    }
   
    @Override
    public void run()
    {

        try {
            fromClientReader = new BufferedReader(new InputStreamReader(connectionToClient.getInputStream()));
            toClientWriter = new PrintWriter(new OutputStreamWriter(connectionToClient.getOutputStream()));

            chatServer.broadcastMessege(name+ " connected.");
            String message;
            do
            {
             message = fromClientReader.readLine();
            if(!message.isEmpty())
              chatServer.broadcastMessege( message);
            }while(!message.isEmpty());

        } catch (IOException e) 
        {
            // e.printStackTrace();
             
        } finally 
        {
               chatServer.removeClient(this);
               chatServer.broadcastMessege(name + " disconnected.");
            if(fromClientReader !=null){
                try {
                    fromClientReader.close();
                } catch (IOException e) {
                     e.printStackTrace();
                }
           }

            if(toClientWriter !=null){
                 toClientWriter.close();
            }
        }
    }

    public void sendMessage(String string) {
    
        toClientWriter.println(string);
        toClientWriter.flush(); 


    }
}
