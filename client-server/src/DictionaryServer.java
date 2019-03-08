/** 
 * The system is worker-pool architecture multiple threading TCP socket server.
 * DictionaryServer is the server part.
 * It creates fixed 5 worker pools.
 * Port number is 4444 in this class.
 * 
 * @author Yiming Zhang, ID 889262, 05/09/2018
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DictionaryServer {
	
    ExecutorService pool = null;
	
	public static void main(String[] args) {
		DictionaryServer dic_server = new DictionaryServer();
		dic_server.startServer();
	}
	
	/**
	 * startServer method will run a server and waits for client
	 * at the port 4444, and it creates 5 worker threads.
	 */	
	public void startServer(){
		
		ServerSocket listeningSocket = null;
		Socket clientSocket = null;
		//load the default dictionary in JSON, it has 3 words.
		JsonDictionary.createDic();
		// creates 5 worker pool threads.
		pool = Executors.newFixedThreadPool(5);
		
		try {
			//Create a server socket listening on port 4444
			listeningSocket = new ServerSocket(4444);
			int i = 0; //counter to keep track of the number of clients
			
			//Listen for incoming connections for ever 
			while (true) {
				System.out.println("Server listening on port 4444 for a connection");
				//Accept an incoming client connection request 
				clientSocket = listeningSocket.accept(); //This method will block until a connection request is received
				i++;
				System.out.println("Client conection number " + i + " accepted:");
				System.out.println("Remote Port: " + clientSocket.getPort());
				System.out.println("Remote Hostname: " + clientSocket.getInetAddress().getHostName());
				System.out.println("Local Port: " + clientSocket.getLocalPort());
				
	           MultiThreadServer runnable= new MultiThreadServer(clientSocket,i,this);
	           pool.execute(runnable);
			}
		// catch error messages and print error messages.	
		} catch (SocketException ex) {
			//ex.printStackTrace();
			System.out.println("Error! A host error occured! ");
		}catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Error! A I/O error occured! ");
		} 
		finally {
			if(listeningSocket != null) {
				try {
					listeningSocket.close();
				} catch (IOException e) {
					//e.printStackTrace();
					System.out.println("Error! A socket error occured! ");
				}
			}
		}
	}
}



