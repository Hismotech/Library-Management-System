package listbook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BookListLoader extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("List of Books");
            Parent root = FXMLLoader.load(getClass().getResource("BookList.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }
}
