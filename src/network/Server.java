package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.json.JSONObject;

/**
 * Configuration file for server connection settings
 */
public class Server {
    public static final String SERVER_BIND_ADDRESS = "localhost";
    public static final int SERVER_PORT = 8989;
    BufferedReader reader;
    BufferedWriter writer;

    public Server() throws UnknownHostException, IOException {
        Socket socket = new Socket(InetAddress.getByName(SERVER_BIND_ADDRESS), SERVER_PORT);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        
    }
    
    public void sendMessage (JSONObject request) throws IOException {
        writer.write(request.toString());
        writer.newLine();
        writer.flush();
    }
    
    public JSONObject reciveMessage () throws IOException {
        String response = reader.readLine();
        return new JSONObject(response);
    }
    
}
