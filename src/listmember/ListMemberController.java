package listmember;
import addmember.AddMemberController;
import database.DatabaseHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import database.DatabaseHandler;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import notification.AlertMaker;
import utilpackage.LibraryUtil;


import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListMemberController implements Initializable {
    ObservableList<Member> memberList = FXCollections.observableArrayList();
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableView<Member> tableView;
    @FXML
    private TableColumn<Member,String> studentColumn;
    @FXML
    private TableColumn<Member,String> matricColumn;
    @FXML
    private TableColumn<Member,String> deptColumn;
    @FXML
    private TableColumn<Member,String> yearColumn;
    DatabaseHandler handler;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initiateColumn();
      loadMember();
    }
    private void loadMember(){
        memberList.clear();
         handler = DatabaseHandler.getInstance();
        String          query           = "Select * from member";
        ResultSet       rs              = handler.executeQuery(query);
        try {
            while (rs.next()) {
                memberList.add(new Member( rs.getString("students"),rs.getString("matric"),
                        rs.getString("department"), rs.getString("year")));
            }
        }
        catch(SQLException se) {
            System.err.println(se.getMessage());
        }
        tableView.setItems(memberList);

    }

    private void initiateColumn(){
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("students"));
        matricColumn.setCellValueFactory(new PropertyValueFactory<>("matric"));
        deptColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
    }

    public static class Member{
        private final SimpleStringProperty students;
        private final SimpleStringProperty matric;
        private final SimpleStringProperty department;
        private final SimpleStringProperty year;

        public Member(String students, String matric, String department, String year){
             this.students = new SimpleStringProperty(students);
             this.matric = new SimpleStringProperty(matric);
             this.department = new SimpleStringProperty(department);
             this.year = new SimpleStringProperty(year);
        }
        public String getStudents() {
            return students.get();
        }
        public String getMatric() {
            return matric.get();
        }
        public String getDepartment() { return department.get();}
        public String getYear() {
            return year.get();
        }
    }

    @FXML
    void handleDeleteAction(ActionEvent event) {
        Member selectMemberForRemoval = tableView.getSelectionModel().getSelectedItem();

        if (selectMemberForRemoval == null){
            AlertMaker.showInformation("No Member Selected","Select a Member For Removal");
            return;
        }

        if (DatabaseHandler.getInstance().hasMemberAlreadyBorrowedABook(selectMemberForRemoval)) {
            AlertMaker.showSimpleErrorMessage("Can't Be Removed", selectMemberForRemoval.getStudents() +
                    " Borrowed a Book.");
            return;
        }

        Alert alert  = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Do You Want To Remove " + selectMemberForRemoval.getStudents() + " From The List");
        Optional<ButtonType> answer = alert.showAndWait();
        if(answer.get()==ButtonType.OK){

            boolean result = DatabaseHandler.getInstance().deleteMember(selectMemberForRemoval);

         if(result){
             AlertMaker.showInformation(null, selectMemberForRemoval.getStudents() + " Was Removed Successfully");
             memberList.remove(selectMemberForRemoval);
         }
         else{
             AlertMaker.showSimpleErrorMessage(null,"Sorry!! " +selectMemberForRemoval.getStudents()
                     + "  Could Not Be Removed");
         }
        }
        else{
            AlertMaker.showSimpleErrorMessage(null,"Removal Operation Cancelled");
        }
    }

    @FXML
    void handleEditMember(ActionEvent event) {
        Member selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null){
            AlertMaker.showSimpleErrorMessage("No Member Selected","Select the Member to edit" );
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addmember/AddMember.fxml"));
            Parent     parent  = loader.load();
            AddMemberController controller = (AddMemberController)loader.getController();
            controller.inflateMemberUI(selectedForEdit);
            Stage      stage  = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Member");
            stage.setScene(new Scene(parent));
            LibraryUtil.setStageIcon(stage);
            stage.show();
            stage.setOnCloseRequest((e) -> {
                handleRefreshAction(new ActionEvent());
            });

        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void handleRefreshAction(ActionEvent event) {
        loadMember(); }
}
