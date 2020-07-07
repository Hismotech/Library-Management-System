package addmember;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddMemberLoader extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Students");
            Parent root = FXMLLoader.load(getClass().getResource("AddMember.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

        }
        catch(IOException ie){
            System.err.println(ie.getMessage());
        }
    }
}
