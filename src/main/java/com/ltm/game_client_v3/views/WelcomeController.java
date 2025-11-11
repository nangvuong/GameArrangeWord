package com.ltm.game_client_v3.views;

import com.ltm.game_client_v3.controller.ClientManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WelcomeController {
    private ClientManager clientManager;

    @FXML private Label welcomeLabel;
    @FXML private Button soundButton;

    @FXML
    private ImageView soundIcon;
    private Image soundOnImg;
    private Image soundOffImg;



    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @FXML
    public void initialize() {
            // Load sound icons
            soundIcon = new ImageView(soundOnImg);
            soundIcon.setFitWidth(32);
            soundIcon.setFitHeight(32);

            soundButton.setGraphic(soundIcon);
            soundButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
            
    }


    @FXML
    private void onPlay() {
        clientManager.getViewManager().showHome();
    }

    @FXML
    private void onLogout() {
    }
}