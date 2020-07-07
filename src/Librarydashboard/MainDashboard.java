package Librarydashboard;

import database.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilpackage.LibraryUtil;

import java.io.IOException;

public class MainDashboard extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Library Management System");
            Parent root = FXMLLoader.load(getClass().getResource("/login/Login.fxml"));
            primaryStage.setScene(new Scene(root));
            LibraryUtil.setStageIcon(primaryStage);
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


