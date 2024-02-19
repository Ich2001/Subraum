package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {
    
    private ServerSocket serverSocket;
    private List< ClientHandler> clients;
    public ChatServer(int port)
    {
        clients = new CopyOnWriteArrayList<>();
        try {
            serverSocket = new ServerSocket(port);
            System.out.println ("Start Subroom Server  on port " + port );
           while(true){
                System.out.println ("Waiting for new clients...");
                Socket connectionToClient = serverSocket.accept();
                ClientHandler client = new ClientHandler(this,connectionToClient) ;
                clients.add (client);
                System.out.println ("Accepted new client");
           }

        } catch(IOException e)
        {
             
             e.printStackTrace();

        }  finally {
            if(serverSocket !=null){
            try {
                serverSocket.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
        }
    }

    public static void  main (String  [] args ) 
    {
     new ChatServer(3141);
    }

    public void broadcastMessege(String string) {
        System.out.println(string);
        if(string != null){
            for(ClientHandler client :clients) 
            {
                client.sendMessage(string );
            }
       }
      
    }

    public void removeClient(ClientHandler client)
    {
        clients.remove(client);
    }
  
}