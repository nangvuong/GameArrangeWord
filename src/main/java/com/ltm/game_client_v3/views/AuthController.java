package com.ltm.game_client_v3.views;

import com.ltm.game_client_v3.controller.ClientManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

public class AuthController {

    private ClientManager clientManager;

    @FXML private VBox loginPane;
    @FXML private VBox registerPane;

    @FXML private TextField loginUsername;
    @FXML private PasswordField loginPassword;
    @FXML private Label loginError;

    @FXML private TextField regUsername;
    @FXML private TextField regNickname;
    @FXML private PasswordField regPassword;
    @FXML private PasswordField regConfirm;
    @FXML private Label regError;


    @FXML
    public void initialize() {

    }

    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @FXML
    private void onLogin() {
        JSONObject msg = new JSONObject();
        msg.put("action", "LOGIN_REQUEST");
        msg.put("username", loginUsername.getText());
        msg.put("password", loginPassword.getText());
        clientManager.send(msg);
    }

    @FXML
    private void onRegister() {
        if (!regPassword.getText().equals(regConfirm.getText())) {
            showRegisterError("Confirm password do not match!");
            return;
        }
        else if(regPassword.getText().isEmpty() || regUsername.getText().isEmpty()) {
            showRegisterError("Username or password must not be empty");
            return;
        }
        else {
            JSONObject msg = new JSONObject();
            msg.put("action", "REGISTER_REQUEST");
            msg.put("username", regUsername.getText());
            msg.put("password", regPassword.getText());
            msg.put("nickname", regNickname.getText());
            clientManager.send(msg);
        }
    }

    @FXML
    private void showRegister() {
        loginPane.setVisible(false);
        registerPane.setVisible(true);
    }

    @FXML
    public void showLogin() {
        registerPane.setVisible(false);
        loginPane.setVisible(true);
    }

    public void showLoginError(String text) {
        Platform.runLater(() -> {
            loginError.setText(text);
            loginError.setVisible(true);
        });
    }

    public void showNotify(String text) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText(null);
            alert.setContentText(text);
            alert.showAndWait();
        });
    }

    public void showRegisterError(String text) {
        Platform.runLater(() -> {
            regError.setText(text);
            regError.setVisible(true);
        });
    }
}