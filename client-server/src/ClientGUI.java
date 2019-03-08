/**
 * The system is worker-pool architecture multiple threading TCP socket server.
 * ClientGUI is the GUI for client.
 * Client can use it to query a word definition,
 * add a word definition and remove a word.
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
//import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextArea;
//import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
//import javax.swing.Timer;
import javax.swing.border.Border;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


//Interactive client that reads input from the command line and sends it to 
//a server 
public class ClientGUI extends JFrame 
implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	public static final int NUMBER_OF_CHAR = 100;
	
	private static JTextArea word;
	private static JTextArea meaning;
	static String word_query = null;
	static String def_query = null;
	
	static String word_add = null;
	static String def_add = null;
	
	static String word_remove = null;
	static String def_remove = null;
	
	static String error_message = null;
	static String message = null;
	
	ClientGUI( )
    {
        super("Dictionary");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));
        
        // word panel
        //JPanel wordPanel = new JPanel( ); 
        JTextArea wordPanel = new JTextArea(10, 10); 
        wordPanel.setLayout(new BorderLayout( ));
        wordPanel.setBackground(Color.WHITE); 

        //word = new JTextField(NUMBER_OF_CHAR);
        word = new JTextArea(5,10);
        
        wordPanel.add(word, BorderLayout.SOUTH);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        word.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(5, 5,5, 5)));
        
        JLabel nameLabel = new JLabel("Welcome to dictionary! Please enter word in the box:)");
        wordPanel.add(nameLabel, BorderLayout.PAGE_START);
        add(wordPanel);
        
        JLabel nameLabel1 = new JLabel("Instruction-> Query:word   Add:word,definition   Remove:word");
        wordPanel.add(nameLabel1, BorderLayout.CENTER);
        add(wordPanel);
      
        
        // meaning of the word
        //JPanel meaningPanel = new JPanel( ); 
        JTextArea meaningPanel = new JTextArea(10, 10);
        meaningPanel.setLayout(new BorderLayout( ));
        meaningPanel.setBackground(Color.WHITE); 

        //meaning = new JTextField(NUMBER_OF_CHAR);
        meaning = new JTextArea(5,50);
        meaningPanel.add(meaning, BorderLayout.SOUTH);
        Border border1 = BorderFactory.createLineBorder(Color.BLACK);
        meaning.setBorder(BorderFactory.createCompoundBorder(border1,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        meaningPanel.add(meaning, BorderLayout.SOUTH);
        JLabel meaningLabel = new JLabel("Information From Server:");
        meaningPanel.add(meaningLabel, BorderLayout.CENTER);

        add(meaningPanel);
        
        // query, add and remove buttons
        
        JPanel buttonPanel = new JPanel( );
        buttonPanel.setLayout(new FlowLayout( ));
        //buttonPanel.setBackground(Color.PINK); 
        
        JButton queryButton = new JButton("Query"); 
        queryButton.addActionListener(this);
        buttonPanel.add(queryButton);

        JButton addButton = new JButton("Add"); 
        addButton.addActionListener(this);
        buttonPanel.add(addButton); 
        
        JButton removeButton = new JButton("Remove"); 
        removeButton.addActionListener(this);
        buttonPanel.add(removeButton);
        
        add(buttonPanel);
    }
	
	/**
	 * if the user clicks the button, it will just past message and 
	 * the message will be used in the startClient method.
	 */	
	   @Override
	   public void actionPerformed(ActionEvent e) 
	    {
	        String actionCommand = e.getActionCommand( );

	        if (actionCommand.equals("Query"))
	        	message = "query";        
	        if (actionCommand.equals("Add"))
	        	message = "add";
	        if (actionCommand.equals("Remove"))
	        	message = "remove";
	    } 

	/**
	 * startClient method will process I/O and process the message 
	 * According to the user action(query, add or remove) and then it will
	 * use setText method and return the message to the GUI.
	 */	  
	static void startClient(Socket socket) throws IOException{
	
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			
			String inputWord = null;

			while (word.getText()!= null){
				
				// if not this piece of code, will not keep running....
				if(message == null){
					System.out.println("Connected, waiting for instruction");
				}
				
				// just enter a word that you want to query the meaning
				if (message == "query"){
					
					word_query = word.getText();	
					inputWord= "query"+","+word_query;

					out.write(inputWord + "\n");
					out.flush();
				
					String received = in.readLine(); 
					meaning.setText(received);
				
					message = null;
				}
				
				// to add a definition to new word use word,definition  
				// Example input: book,a word that described ... that can to read.
				
				if (message == "add"){
					
					word_query = word.getText();
					inputWord= "add"+","+word_query;
					
					out.write(inputWord + "\n");
					out.flush();
					
					String received = in.readLine(); 
					meaning.setText(received);
					message = null;
				}
				
				//just enter the word that you want to remove
				if (message == "remove"){
					
					word_query = word.getText();	
					inputWord= "remove"+","+word_query;
					
					out.write(inputWord + "\n");
					out.flush();
					
					String received = in.readLine(); 
					meaning.setText(received);
					message = null;
				}
			}	
		}
	
	public static void main(String[] args) {
		
        ClientGUI gui = new ClientGUI( );
        gui.setVisible(true);
        
        Socket socket = null;
		try {
			socket = new Socket("localhost", 4444);
			System.out.println("Welcome to dictionary! Please query, add or remove word!");
			meaning.setText("Connected to the server successfully! ");
			startClient(socket);	
		} 
		
		
		// catch error messages and print error messages.	
		catch (UnknownHostException e) {
			//e.printStackTrace();
			System.out.println("Error! A host error occured! ");
			error_message = "Error! A host error occured!";
			meaning.setText(error_message);
			
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Error! A I/O error occured! ");
			error_message = "Error! A I/O error occured!";
			meaning.setText(error_message);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					//e.printStackTrace();
					System.out.println("Error! A socket error occured! ");
					error_message = "Error! A socket error occured!";
					meaning.setText(error_message);
				}
			}
		}
	}
}

