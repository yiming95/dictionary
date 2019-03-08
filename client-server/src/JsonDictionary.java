/**
 * The system is worker-pool architecture multiple threading TCP socket server.
 * This class is used to create the default dictionary in JSON.
 * It contains three words.
 *
 * @author Yiming Zhang, ID 889262, 05/09/2018
 */
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

	public class JsonDictionary {

	    @SuppressWarnings("unchecked")
		public static void createDic() {

	    	// three words in dictionary
	        JSONObject obj = new JSONObject();
	        obj.put("java", "Definition: An island in Indonesia south of Borneo.");
	        obj.put("python", "Definition: (Greek mythology) Dragon killed by Apollo at Delphi.");
	        obj.put("c", "Definition: the 3rd letter of the Roman alphabet.");
	       
	        try (FileWriter file = new FileWriter("dictionary.json")) {

	            file.write(obj.toJSONString());
	            file.flush();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	}
