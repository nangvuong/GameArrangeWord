package com.ltm.game_client_v3;

import com.ltm.game_client_v3.controller.ClientManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        System.out.println("✅ Using system fonts");
        
        ClientManager.getInstance().start(primaryStage);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}