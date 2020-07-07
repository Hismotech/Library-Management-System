package settings;
import notification.AlertMaker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
        @FXML
        private JFXTextField bookDays;

        @FXML
        private JFXTextField finePerDay;

        @FXML
        private JFXTextField userName;

        @FXML
        private JFXPasswordField passWord;

        @FXML
        void cancelSettings(ActionEvent event) {
                Stage stage =  (Stage) bookDays.getScene().getWindow();
                stage.close();
        }
        @Override
        public void initialize(URL location, ResourceBundle resources) {
                initDefaultValue();

        }

        @FXML
        void saveSettings(ActionEvent event) {
                Preferences preferences = Preferences.getPreferences();
                preferences.setBookDays(Integer.parseInt(bookDays.getText()));
                preferences.setFinePerDay(Float.parseFloat(finePerDay.getText()));
                preferences.setUserName(userName.getText());
                preferences.setPassWord(passWord.getText());
                Preferences.updatePreference(preferences);

        }

        private void initDefaultValue() {
                Preferences preference = Preferences.getPreferences();
                bookDays.setText(String.valueOf(preference.getBookDays()));
                finePerDay.setText(String.valueOf(preference.getFinePerDay()));
                userName.setText(String.valueOf(preference.getUserName()));
                passWord.setText(String.valueOf(preference.getPassWord()));
        }
}


