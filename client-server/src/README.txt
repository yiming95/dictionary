This README describes the file for COMP90015 Distributed System 2018S2, Project 1. 

- It contains 6 java files and they implement a worker-pool multi-threading architecture
with TCP socket Dictionary Server. The port number for socket to connection is default set as 4444. The dictionary data uses JSON format to achieve efficiency.

IP address is default set as localhost. There is no need for user to enter port number or IP address to use it.

The default dictionary contains three words : "java","python","c" and "JsonDictionary.java" creates the dictionary. It uses json-simple-1.1.1.jar to help
build JSON object.

- To run it:
     By console: first run "DictionaryServer.java" then run "Client.java"
                 To query a word's definition:  query,java
		 To add a new word: add,html,a language that can build website
		 To remove a word: remove,html

     By GUI: first run "ServerGUI.java" then run "ClientGUI.java"
		notice: by add a new word, input "html,a language..." in the box
 			then click the "add" button
		To query or remove a word, just enter a word firstly in the input box,
		then click the button "query" or "remove" and the info will be shown
 		in the below information box.

    By jar: It has two jar files- "DictionaryServer.jar" and "DictionaryClient.jar"
	    just double clicked the "DictionaryServer.jar" first and then the
	    "DictionaryClient.jar". No need to enter port number,IP etc.