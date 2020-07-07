package utilpackage;

import database.DatabaseHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class LibraryUtil {
    private static final String screenIcon = "/resources/icon.png";

    public static void setStageIcon (Stage stage){
        Image image = new Image(screenIcon);
        stage.getIcons().add(image);
    }

    public static void loadWindow(URL location, String title) {
        try {
            Parent parent = FXMLLoader.load(location);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
            setStageIcon(stage);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        catch (NullPointerException npe) {
            System.err.println(npe.getMessage());
        }
    }
}

