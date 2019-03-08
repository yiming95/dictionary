/**
 * The system is worker-pool architecture multiple threading TCP socket server.
 * ServertGUI is the GUI for server. 
 * It shows how many clients have connected to the server.
 *
 * @author Yiming Zhang, ID 889262, 05/09/2018
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.Border;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerGUI extends JFrame 
implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 400;
	public static final int HEIGHT = 400;
	public static final int NUMBER_OF_CHAR = 100;
	
	static String error_message = null;
	private static JTextArea word;
	static String message = null;
    static ExecutorService pool = null;
	static int i = 0;
	static int portNumber = 0;
    
    ServerGUI(){
    	 super("Dictionary Server");
         setSize(WIDTH, HEIGHT);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setLayout(new GridLayout(2, 1));
         
         // word panel
         //JPanel wordPanel = new JPanel( ); 
         JTextArea wordPanel = new JTextArea(20, 10); 
         wordPanel.setLayout(new BorderLayout( ));
         wordPanel.setBackground(Color.WHITE); 

         //word = new JTextField(NUMBER_OF_CHAR);
         word = new JTextArea(8,8);
         
         wordPanel.add(word, BorderLayout.SOUTH);
         Border border = BorderFactory.createLineBorder(Color.BLACK);
         word.setBorder(BorderFactory.createCompoundBorder(border,
                 BorderFactory.createEmptyBorder(10,10,10, 10)));
         

         JLabel nameLabel1 = new JLabel("Welcome to Server!");
         wordPanel.add(nameLabel1, BorderLayout.CENTER);
         add(wordPanel);
       

         // query, add and remove buttons
         
         JPanel buttonPanel = new JPanel( );
         //buttonPanel.setLayout(new FlowLayout( ));
         //buttonPanel.setBackground(Color.pink); 
         
         JButton startButton = new JButton("Show Client Number"); 
         startButton.addActionListener(this);
         buttonPanel.add(startButton);  
         
         JButton infoButton = new JButton("Show Details"); 
         infoButton.addActionListener(this);
         buttonPanel.add(infoButton);  
         
         add(buttonPanel);
     }

 	   @Override
 	   public void actionPerformed(ActionEvent e) 
 	    {
 	        String actionCommand = e.getActionCommand( );
 	        if (actionCommand.equals("Show Client Number"))
 	        	word.setText(i + " clients have connected!");
 	        	message = "start"; 
 	        	
 	 	    if (actionCommand.equals("Show Details"))
 	 	    	word.setText( "client with remote port number " + portNumber+" just connected!");
 	    } 
 	   
 	/**
 	 * startServer method will start the server and wait for clients to connect 
 	 * at port 4444. It will just show the information how many clients have
 	 * been connected to the server in the GUI.
 	 */	   
	public static void startServer(DictionaryServer server){
		ServerSocket listeningSocket = null;
		Socket clientSocket = null;
		JsonDictionary.createDic();
		pool = Executors.newFixedThreadPool(5);
		
		try {
			//Create a server socket listening on port 4444
			listeningSocket = new ServerSocket(4444);
			//Listen for incoming connections for ever 
			while (true) {
				System.out.println("Server listening on port 4444 for a connection");
				//Accept an incoming client connection request 
				clientSocket = listeningSocket.accept(); //This method will block until a connection request is received
				
				i++;
				System.out.println("Client conection number " + i + " accepted:");
				portNumber = clientSocket.getPort();
				System.out.println("Remote Port: " + clientSocket.getPort());
				System.out.println("Remote Hostname: " + clientSocket.getInetAddress().getHostName());
				System.out.println("Local Port: " + clientSocket.getLocalPort());
				
	           MultiThreadServer runnable= new MultiThreadServer(clientSocket,i,server);
	           pool.execute(runnable);    
	           
	        	   
			}
			
	    // catch error messages and print error messages.		
		} catch (SocketException ex) {
			//ex.printStackTrace();
			System.out.println("Error! A socket error occured! ");
			error_message = "Error! A socket error occured!";
			word.setText(error_message);
		}catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Error! A I/O error occured! ");
			error_message = "Error! A I/O error occured!";
			word.setText(error_message);
		} 
		finally {
			if(listeningSocket != null) {
				try {
					listeningSocket.close();
				} catch (IOException e) {
					//e.printStackTrace();
					System.out.println("Error! A socket error occured! ");
					error_message = "Error! A socket error occured!";
					word.setText(error_message);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		DictionaryServer server = new DictionaryServer();
		//dic_server.startServer();
		ServerGUI gui = new ServerGUI();
        gui.setVisible(true);
		startServer(server);
	}
	
}


