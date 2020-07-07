package login;


import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;
import settings.Preferences;
import utilpackage.LibraryUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private JFXTextField     userName;
    @FXML
    private JFXPasswordField passWord;

    Preferences preferences;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        preferences =Preferences.getPreferences();
    }


    @FXML
    void handleCancelAction(ActionEvent event) {
        System.exit(0);

    }


    @FXML
    void handleLoginAction(ActionEvent event) {
    String uName = userName.getText();
    String pWord = DigestUtils.sha1Hex(passWord.getText());
     if (uName.equals(preferences.getUserName())&&pWord.equals(preferences.getPassWord())){
         closeStage();
         loadMainWindow();
     }
     else{
         userName.getStyleClass().add("wrong-credentials");
         passWord.getStyleClass().add("wrong-credentials");
     }
    }

    private void closeStage(){
        Stage stage = (Stage) userName.getScene().getWindow();
        stage.close();
    }

    private void loadMainWindow(){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/Librarydashboard/DashBoard.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Library Management System Dashboard");
            LibraryUtil.setStageIcon(stage);
            stage.setScene(new Scene(parent));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}

