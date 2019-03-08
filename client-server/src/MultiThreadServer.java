/**
 * The system is worker-pool architecture multiple threading TCP socket server.
 * MultiThreadServer implements the functionalities of the dictionary: query, add, remove
 *
 * @author Yiming Zhang, ID 889262, 05/09/2018
 */
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;


public class MultiThreadServer implements Runnable {

    DictionaryServer server=null;
    Socket client=null;
    int id;
    
    MultiThreadServer (Socket client, int count ,DictionaryServer server ) throws IOException {
        
        this.client=client;
        this.server=server;
        this.id=count;
        System.out.println("Connection "+id+"established with client "+client);
        
    }

 	/**
 	 * run method processes I/O with respect to three instructions.
 	 */	 
	@Override
	public void run() {
		try {
			String clientMsg = null;
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
					
				while((clientMsg = in.readLine()) != null) {

					String[] command = clientMsg.split(",");
					if (command[0].equals("query")){
						out.write(query(command[1]));
						out.flush();
					}
					if (command[0].equals("add")){
						out.write(add(command[1]+","+command[2]));
						out.flush();
					}
					if (command[0].equals("remove")){
						out.write(remove(command[1]));
						out.flush();
					}
				 }
			}
			catch(SocketException e) {
				System.out.println("closed...a socket error occured! ");
			}

			catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Error! A I/O error occured! ");
			} 
				try {
					client.close();
				} catch (IOException e) {
				//	e.printStackTrace();
					System.out.println("Error! A I/O error occured! ");
				}
			}


// query the meaning from the dictionary
// format is " query,java "
public static String query(String message){
	JSONParser parser = new JSONParser();

	String output = null;
	try{
	Object obj = parser.parse(new FileReader("dictionary.json"));
	JSONObject jsonObject = (JSONObject) obj;
	
	// check the meaning from dictionary
	if(message != null){
		
	String def = (String) jsonObject.get(message);
	System.out.println("Word to query is: " + message);
	
	if (def == null){
		def = "No result, try again!";
	}
	output = def + "\n";
	}
	
	}  catch (FileNotFoundException e) {
       // e.printStackTrace();
		System.out.println("Error! File can not find! ");
    } catch (IOException e) {
       // e.printStackTrace();
		System.out.println("Error! A I/O error occured! ");
    } catch (ParseException e) {
       // e.printStackTrace();
		System.out.println("Error! A parse error occured! ");
    }
	
	return output;
}

// add message to dictionary
// to add a word like hello, format is " add,hello,Used as a greeting or to begin a telephone conversation. "
@SuppressWarnings("unchecked")
public synchronized String add(String message){
	JSONParser parser = new JSONParser();

	String output = null;
	try{
	Object obj = parser.parse(new FileReader("dictionary.json"));
	JSONObject jsonObject = (JSONObject) obj;
	String[] add_info = message.split(",");
	
	// check the meaning from dictionary
	if((String) jsonObject.get(add_info[0]) == null){
	
	jsonObject.put(add_info[0], add_info[1]);
	System.out.println(jsonObject);
	
    @SuppressWarnings("resource")
	FileWriter file = new FileWriter("dictionary.json", false);
    try {
        file.write(jsonObject.toJSONString());
        file.flush();
    } catch (IOException e) {
       // e.printStackTrace();
		System.out.println("Error! A I/O error occured! ");
    }

	System.out.println("Word adds to the dictionary: " + add_info[0]);
	output = "Adds " +add_info[0] +" to the dictionary successfully!" + "\n";
	}
	
	else if((String) jsonObject.get(add_info[0]) != null){
		System.out.println("Word already exists! ");
		output = "Duplicate! word has already in the dictionary!" + "\n";
	}
	else {
		output = "error!";
	}
	}  catch (FileNotFoundException e) {
       // e.printStackTrace();
		System.out.println("Error! File can not find! ");
    } catch (IOException e) {
       // e.printStackTrace();
		System.out.println("Error! A I/O error occured! ");
    } catch (ParseException e) {
       // e.printStackTrace();
		System.out.println("Error! A parse error occured! ");
    }
	return output;
}


// remove a word from the dictionary
// add message to dictionary
// to add a word like hello, format is " add,hello,Used as a greeting or to begin a telephone conversation. "
public synchronized String remove(String message){
	JSONParser parser = new JSONParser();

	String output = null;
	try{
	Object obj = parser.parse(new FileReader("dictionary.json"));
	JSONObject jsonObject = (JSONObject) obj;
	
	// check the meaning from dictionary
	if((String) jsonObject.get(message) != null){
	
	jsonObject.remove(message);
	System.out.println(jsonObject);
	
    @SuppressWarnings("resource")
	FileWriter file = new FileWriter("dictionary.json", false);
    try {
        file.write(jsonObject.toJSONString());
        file.flush();
    } catch (IOException e) {
       // e.printStackTrace();
		System.out.println("Error! A I/O error occured! ");
    }

	System.out.println("Word deletes successfully! ");
	output = "Deletes from dictionary successfully!" + "\n";
	}
	
	else if((String) jsonObject.get(message) == null){
		System.out.println("Error, No word in the dictionary!");
		output = "Error ! No word in the dictionary!" + "\n";
	}
	else {
		output = "error!";
	}
	}  catch (FileNotFoundException e) {
       // e.printStackTrace();
		System.out.println("Error! File can not find! ");
    } catch (IOException e) {
       // e.printStackTrace();
		System.out.println("Error! A I/O error occured! ");
    } catch (ParseException e) {
       // e.printStackTrace();
		System.out.println("Error! A parse error occured! ");
    }
	return output;
}

}
