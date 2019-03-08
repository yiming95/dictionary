/**
 * The system is worker-pool architecture multiple threading TCP socket server.
 * This is the client part. It can connect to the server and have interaction with dictionary
 * to query, add or remove a word.
 *
 * @author Yiming Zhang, ID 889262, 05/09/2018
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

//Interactive client that reads input from the command line and sends it to 
//a server 
public class Client {

 	/**
 	 * sendMessage method processes I/O to interact with server.
 	 */	
	static void sendMessage(Socket socket) throws IOException{
		
			// Get the input/output streams for reading/writing data from/to the socket
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

			Scanner scanner = new Scanner(System.in);
			String inputStr = null;

			//While the user input differs from "exit"
			while (!(inputStr = scanner.nextLine()).equals("exit")) {
				
				// Send the input string to the server by writing to the socket output stream
				out.write(inputStr + "\n");
				out.flush();
			
				// Receive the reply from the server by reading from the socket input stream
				String received = in.readLine(); 
				System.out.println(received);
			}
			scanner.close();
	}
	
	public static void main(String[] args) {
	
		Socket socket = null;
		try {
			// Create a stream socket bounded to any port and connect it to the
			// socket bound to localhost on port 4444
			socket = new Socket("localhost", 4444);
			System.out.println("Welcome to dictionary! Please query, add or remove word!");
			sendMessage(socket);
			System.out.println("Connect to the server successfully!");
			
			// catch error messages and print error messages.
			} catch (UnknownHostException e) {
				//e.printStackTrace();
				System.out.println("Error! A host error occured! ");
			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println("Error! A I/O error occured! ");
			} finally {
				// Close the socket
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						//e.printStackTrace();
						System.out.println("Error! A socket error occured! ");
					}
				}
			}
	}

}
