package settings;


import database.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsLoader extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Settings");
            Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            new Thread(()->{
                DatabaseHandler.getInstance();
            }).start();

        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }


}

