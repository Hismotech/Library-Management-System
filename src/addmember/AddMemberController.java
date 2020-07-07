package addmember;

import com.jfoenix.controls.JFXTextField;
import database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import listmember.ListMemberController.Member;
import notification.AlertMaker;

import java.net.URL;
import java.util.ResourceBundle;


public class AddMemberController implements Initializable {
  DatabaseHandler handler;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private JFXTextField studentField;
    @FXML
    private JFXTextField matricField;
    @FXML
    private JFXTextField departmentField;
    @FXML
    private JFXTextField yearField;

    boolean isItInEditMode = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = DatabaseHandler.getInstance();
    }

    @FXML
    void addMember(ActionEvent event) {
        String studentName = studentField.getText();
        String matricNo = matricField.getText();
        String department = departmentField.getText();
        String year = yearField.getText();
        if(studentName.isEmpty()||matricNo.isEmpty()||department.isEmpty()||year.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Input All Fields");
            alert.showAndWait();
        }
        if (isItInEditMode){
            handleMemberUpdate();
            return;
        }
        String action =
                "Insert into Member values (" + "'" +studentName+"'," + "'"+matricNo+"',"
                +"'"+department+"'," +"'"+year+"'" +")";
        System.out.println(action);
        if (handler.executeAction(action)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Student Added Successfully");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Error in Adding Student");
            alert.showAndWait();
        }
    }


    @FXML
    void cancel(ActionEvent event) {
        Stage stage =  (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }

    public void inflateMemberUI(Member member)
    {
        studentField.setText(member.getStudents());
        matricField.setText(member.getMatric());
        departmentField.setText(member.getDepartment());
        yearField.setText(member.getYear());
        matricField.setEditable(false);
        isItInEditMode = true;
    }
    private void handleMemberUpdate() {
        Member member = new Member(studentField.getText(),matricField.getText(),departmentField.getText(),yearField.getText());
        if (handler.updateMember(member)) {
            AlertMaker.showInformation(null,"Member Updated Successfully");
        }
        else{
            AlertMaker.showSimpleErrorMessage(null, "Error in Updating Member");
        }

    }


}

