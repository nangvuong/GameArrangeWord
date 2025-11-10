/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package network;

import java.io.IOException;
import model.Player;
import models.User;
import org.json.JSONObject;

/**
 *
 * @author nangvuong
 */
public class PlayerIO  {
    Server server;

    public PlayerIO(Server server) {
        this.server = server;
    }
    
    
    
    public boolean checkLogin (Player player) throws IOException {
        JSONObject request = new JSONObject();
        
        request.put("action", "LOGIN_REQUEST");
        request.put("username", player.getUsername());
        request.put("password", player.getPassword());
        
        server.sendMessage(request);
        
        JSONObject response = server.reciveMessage();
        
        String status = response.getString("status");
        
        if (status.equals("fail")) return false;
        
        JSONObject userJson = response.getJSONObject("user");
        
        User user = new User(userJson.getInt("id"), userJson.getString("username"), "", userJson.getString("nickname"), userJson.getInt("total_matches"), userJson.getInt("total_wins"), userJson.getInt("total_score"));
        player = new Player(user);
        return true;
    }
    
    public boolean register (Player player) throws IOException {
        JSONObject request = new JSONObject();
        
        request.put("action","REGISTER_REQUEST");
        request.put("username", player.getUsername());
        request.put("password", player.getPassword());
        request.put("nickname", player.getFullName());
        
        server.sendMessage(request);
        
        JSONObject response = server.reciveMessage();
        
        String status = response.getString("status");
        
        if (status.equals("fail")) return false;
        
        return true;
    }
    
    public void logout () throws IOException {
        JSONObject request = new JSONObject();
        request.put("action", "LOGOUT_REQUEST");
        server.sendMessage(request);
    }
}
