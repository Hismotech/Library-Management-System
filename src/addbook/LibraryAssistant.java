package addbook;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class LibraryAssistant extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try {
            Parent root =FXMLLoader.load(getClass().getResource("AddBook.fxml"));
            primaryStage.setTitle("AddBooks");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }

        catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }
}
