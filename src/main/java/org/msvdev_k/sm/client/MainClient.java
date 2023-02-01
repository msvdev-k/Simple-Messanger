package org.msvdev_k.sm.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainClient extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientUI.fxml"));
        Parent root = loader.load();
        stage.setTitle("Чат");
        stage.setScene(new Scene(root, 400, 400));
        stage.show();
        ChatController chatController = loader.getController();
        stage.setOnCloseRequest(windowEvent -> {
            chatController.close();
            Platform.exit();
            System.exit(0);
        });
    }
}
