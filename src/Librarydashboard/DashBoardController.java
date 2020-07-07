package Librarydashboard;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import notification.AlertMaker;
import utilpackage.LibraryUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


public class DashBoardController implements Initializable {


    @FXML
    private AnchorPane anchorRootPane;
    @FXML
    private JFXButton  renewButton;
    @FXML
    private JFXButton  submissionButton;
    @FXML
    private Tab bookIssueTab;
    @FXML
    private Text             studentNameHolder;
    @FXML
    private Text             studentDeptHolder;
    @FXML
    private Text             studentMatric;
    @FXML
    private Text             bookNameHolder;
    @FXML
    private Text             bookAuthHolder;
    @FXML
    private Text             bookPubHolder;
    @FXML
    private Text             issueDateHolder;
    @FXML
    private Text             noOfDaysHolder;
    @FXML
    private Text             fineHolder;
    @FXML
    private JFXDrawer        drawer;
    @FXML
    private JFXHamburger     hamBurger;
    @FXML
    private HBox             bookInfo;
    @FXML
    private HBox             memberInfo;
    @FXML
    private Text             bookName;
    @FXML
    private Text             authorName;
    @FXML
    private Text             bookStatus;
    @FXML
    private TextField        bookIDInput;
    @FXML
    private TextField        matricNoInput;
    @FXML
    private Text             getStudentName;
    @FXML
    private Text             getDepartment;
    @FXML
    private JFXTextField     bookID;
    @FXML
    private StackPane        rootPane;
    @FXML
    private HBox             issueDataContainer;
    @FXML
    private JFXButton issueButton;
    @FXML
    private StackPane memberInfoContainer;
    @FXML
    private StackPane bookInfoContainer;




    private PieChart bookChart;
    private PieChart memberChart;
    private boolean isReadyForReturn = false;
    private boolean isReadyForIssue = false;
    private DatabaseHandler databaseHandler;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseHandler = DatabaseHandler.getInstance();
        JFXDepthManager.setDepth(bookInfo, 1);
        JFXDepthManager.setDepth(memberInfo, 1);
        initDrawer();
        showPieChart();
    }

    @FXML
    private void loadAddMember(ActionEvent event) {
        LibraryUtil.loadWindow(getClass().getResource("/addmember/AddMember.fxml"), "Add Member");
    }

    @FXML
    private void loadAddBook(ActionEvent event) throws IOException {
        LibraryUtil.loadWindow(getClass().getResource("/addbook/AddBook.fxml"), "Add Book");

    }

    @FXML
    private void loadMemberTable(ActionEvent event) {
        LibraryUtil.loadWindow(getClass().getResource("/listmember/ListMember.fxml"), "Member List");
    }

    @FXML
    private void loadBookTable(ActionEvent event) {
        LibraryUtil.loadWindow(getClass().getResource("/listbook/BookList.fxml"), "Book List");
    }

    @FXML
    private void loadSettings(ActionEvent event) {
        LibraryUtil.loadWindow(getClass().getResource("/settings/Settings.fxml"), "Settings");
    }

    @FXML
    void loadBookInfo(ActionEvent event) {
        clearBookCache();
        enablePieChart(false);
        String    id    = bookIDInput.getText();
        String    query = "select * from book where id = '" + id + "'";
        ResultSet rs    = databaseHandler.executeQuery(query);
        boolean isBookInfoReady = false;
        try {
            while (rs.next()) {
                bookName.setText(rs.getString("title"));
                authorName.setText(rs.getString("author"));
                boolean bStatus = (rs.getBoolean("isAvailable"));
                String  status  = (bStatus) ? "Avaliable" : "Not Available";
                bookStatus.setText(status);
                isBookInfoReady = true;

            }
            if (!isBookInfoReady) {
                bookName.setText("No Such Book Available");
            }
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    @FXML
    void loadMemberInfo(ActionEvent event) {
        clearMemberCache();
        String    matricNo = matricNoInput.getText();
        String    query    = "select * from member where matric = '" + matricNo + "'";
        ResultSet rs       = databaseHandler.executeQuery(query);
        boolean   isMemberInfoReady     = false;
        try {
            while (rs.next()) {
                getStudentName.setText(rs.getString("students"));
                getDepartment.setText(rs.getString("department"));
                isMemberInfoReady = true;
                enableIssueButton(true);
            }
            if (!isMemberInfoReady) {
                getStudentName.setText("Student Not registered Yet");
                enableIssueButton(false);
            }
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    @FXML
    void loadIssueOperation(ActionEvent event) {
        String Id     = bookIDInput.getText();
        String matric = matricNoInput.getText();
        if(Id.isEmpty()||matric.isEmpty()){
            JFXButton okButton = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane,anchorRootPane,Arrays.asList(okButton),"One or More Field is " +
                            "Empty. Please Input All Fields",
                    null);
            return;
        }
        JFXButton       yesButton          = new JFXButton("Yes");
        yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED,(MouseEvent mouseEvent1)->{
            String action1 = "insert into Issue(Id,matric) values(" + "'" + Id + "'," + "'" + matric + "'" +
                    ")";
            String action2 = "update Book set isAvailable  = false where id = '" + Id + "'";

            if (databaseHandler.executeAction(action1) && databaseHandler.executeAction(action2)) {
                JFXButton       button          = new JFXButton("Done");
                AlertMaker.showMaterialDialog(rootPane,anchorRootPane, Arrays.asList(button),"Book Issued Successfully",
                        null);
                isReadyForIssue = true;
                refreshPieChart();
            }
            else
                {
                JFXButton button = new JFXButton("Ok");
                AlertMaker.showMaterialDialog(rootPane,anchorRootPane, Arrays.asList(button),"Issue Operation Failed",
                        null);
                }
            clearIssueData();
    });
        JFXButton noButton = new JFXButton("No");
        noButton.addEventHandler(MouseEvent.MOUSE_CLICKED,(MouseEvent mouseEvent1)-> {
            AlertMaker.showMaterialDialog(rootPane,anchorRootPane, Arrays.asList(noButton),"Issue Operation Cancelled",
                    null);
           clearIssueData();
        });
        JFXButton button = new JFXButton("Ok");
         AlertMaker.showMaterialDialog(rootPane,anchorRootPane,Arrays.asList(yesButton,noButton),"Confirm Issue",
                 "Issue " + bookName.getText() + " to " + getStudentName.getText() +" ?" );
    }

    @FXML
    void loadIssueData(ActionEvent event) {
        clearIssueEntries();
        isReadyForReturn = false;
        String id = bookID.getText();
        String myQuery = "select issue.id,issue.matric,issue.IssueTime,issue.Renewal,\n" +
                "   member.students,member.matric,member.department,\n" +
                "   book.title,book.author,book.publisher\n" +
                "   from issue\n" +
                "   left join member\n" +
                "   on issue.matric = member.matric\n" +
                "   left join book\n" +
                "   on issue.id = book.id\n" +
                "   where issue.id = '" + id + "'";
        ResultSet rs = databaseHandler.executeQuery(myQuery);
        try {
            if (rs.next()) {
                studentNameHolder.setText(rs.getString("students"));
                studentDeptHolder.setText(rs.getString("department"));
                studentMatric.setText(rs.getString("matric"));

                bookNameHolder.setText(rs.getString("title"));
                bookAuthHolder.setText(rs.getString("author"));
                bookPubHolder.setText(rs.getString("publisher"));

                Timestamp issueTime   = rs.getTimestamp("IssueTime");
                Date      dateOfIssue = new Date(issueTime.getTime());
                issueDateHolder.setText(dateOfIssue.toGMTString());
                Long timeElapsed = System.currentTimeMillis() - issueTime.getTime();
                Long daysElapsed = TimeUnit.DAYS.convert(timeElapsed, TimeUnit.MILLISECONDS);
                noOfDaysHolder.setText(daysElapsed.toString());
                fineHolder.setText("Not Supported Yet");
                isReadyForReturn = true;
                enableRenewReturnButton(true);
                issueDataContainer.setOpacity(1);
            } else {
                JFXButton       button          = new JFXButton("Continue");
                AlertMaker.showMaterialDialog(rootPane,anchorRootPane, Arrays.asList(button),"Such Book Has Not Been Issued Yet",
                        null);
            }
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
        catch (RuntimeException re) {
            System.err.println(re.getMessage());
        }
    }

    @FXML
    void loadReturnOperation(ActionEvent event) {
        if (!isReadyForReturn) {
            JFXButton notReturnButton = new JFXButton("Ok");
            AlertMaker.showMaterialDialog(rootPane,anchorRootPane,Arrays.asList(notReturnButton),"Invalid Book ID"
                    + "please Enter " + "valid Book ID",null );
            return;
        }
        String Id     = bookID.getText();
        JFXButton       yesButton          = new JFXButton("Yes");
        yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED,(MouseEvent mouseEvent1)->{
            String action1 = "Delete from Issue where id = '" + Id + "'";
            String action2 = "Update Book Set isAvailable = true where id = '" + Id + "'";

            if (databaseHandler.executeAction(action1) && databaseHandler.executeAction(action2)) {
                JFXButton submitButton = new JFXButton("Ok");
                AlertMaker.showMaterialDialog(rootPane,anchorRootPane,Arrays.asList(submitButton),"Book Returned " +
                        "Sucessfully",null);
                enableRenewReturnButton(false);
                issueDataContainer.setOpacity(0);
                bookID.clear();
            } else {
                JFXButton submitButton = new JFXButton("Check Again!!");
                AlertMaker.showMaterialDialog(rootPane,anchorRootPane,Arrays.asList(submitButton),"Book Not Returned ",null);
            }
        });
        JFXButton noButton = new JFXButton("No");
        noButton.addEventHandler(MouseEvent.MOUSE_CLICKED,(MouseEvent mouseEvent1)-> {
            AlertMaker.showMaterialDialog(rootPane,anchorRootPane, Arrays.asList(noButton),"Return Operation " +
                            "Cancelled",
                    null);
            JFXButton button = new JFXButton("Ok");
        });
        AlertMaker.showMaterialDialog(rootPane,anchorRootPane,Arrays.asList(yesButton,noButton),"Confirm Return",
                "Return " + bookNameHolder.getText() + " ?" );
    }

    @FXML
    void loadRenewOperation(ActionEvent event) {
        if (!isReadyForReturn) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Invalid Book ID, please Enter a valid book ID");
            alert1.showAndWait();
            return;
        }
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setHeaderText(null);
        alert1.setContentText("Are you sure you want to renew the book?");
        Optional<ButtonType> response = alert1.showAndWait();
        if (response.get() == ButtonType.OK) {
            String action1 = "Update Issue set IssueTime = Current_TimeStamp,Renewal = renewal + 1 where id ='" + bookID.getText() + "'";
            if (databaseHandler.executeAction(action1)) {
                Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
                alert2.setHeaderText(null);
                alert2.setContentText("Book Renewal Successful");
                loadIssueData(null);
            } else {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setHeaderText(null);
                alert2.setContentText("Renewal Operation Failed");
            }
        } else {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setHeaderText(null);
            alert2.setContentText("Renewal Operation Cancelled");
        }
    }

    @FXML
    void closeMenu(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleAddBook(ActionEvent event) {
        LibraryUtil.loadWindow(getClass().getResource("/addbook/AddBook.fxml"), "Add Book");
    }

    @FXML
    void handleAddMember(ActionEvent event) {
        LibraryUtil.loadWindow(getClass().getResource("/addmember/AddMember.fxml"), "Add Member");
    }

    @FXML
    void handleViewBook(ActionEvent event) {
        LibraryUtil.loadWindow(getClass().getResource("/listbook/BookList.fxml"), "Book List");
    }

    @FXML
    void handleViewMember(ActionEvent event) {
        LibraryUtil.loadWindow(getClass().getResource("/listmember/ListMember.fxml"), "Member List");
    }

    @FXML
    void handleMenuFullScreen(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setFullScreen(stage.isFullScreen());
    }

    void initDrawer() {
        try {
            VBox toolbar = FXMLLoader.load(getClass().getResource("/toolbar/ToolBar.fxml"));
            drawer.setSidePane(toolbar);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        HamburgerSlideCloseTransition hsct = new HamburgerSlideCloseTransition(hamBurger);
        hsct.setRate(-1);
        hamBurger.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            hsct.setRate(hsct.getRate() * -1);
            hsct.play();
            if (drawer.isClosed()) {
                drawer.open();
            } else {
                drawer.close();
            }
        });
    }

     private void showPieChart(){
        bookChart = new PieChart(databaseHandler.getBookStatistics());
        memberChart = new PieChart(databaseHandler.getMemberStatistics());
        bookInfoContainer.getChildren().add(bookChart);
        memberInfoContainer.getChildren().add(memberChart);
        bookIssueTab.setOnSelectionChanged((Event event) ->{
            clearIssueData();
            if(bookIssueTab.isSelected()) {
                refreshPieChart();
                enableIssueButton(false);
            }
        });

    }

    void enablePieChart(boolean status){
        if (status) {
            bookChart.setOpacity(1);
            memberChart.setOpacity(1);
        }
        else{
            bookChart.setOpacity(0);
            memberChart.setOpacity(0);
        }

    }

    void clearBookCache() {
        bookName.setText(null);
        authorName.setText(null);
        bookStatus.setText(null);
    }

    void clearMemberCache() {
        getStudentName.setText(null);
        getDepartment.setText(null);
    }

    private void enableRenewReturnButton(boolean flag) {
        if (flag) {
            renewButton.setDisable(false);
            submissionButton.setDisable(false);
        }
        else {
            renewButton.setDisable(true);
            submissionButton.setDisable(true);
        }
    }

    private void enableIssueButton(boolean enable){
        if(enable){
            issueButton.setDisable(false);
        }
        else
            {
            issueButton.setDisable(true);
            }

    }

    private void clearIssueEntries() {
        studentNameHolder.setText(null);
        studentDeptHolder.setText(null);
        studentMatric.setText(null);

        bookNameHolder.setText(null);
        bookAuthHolder.setText(null);
        bookPubHolder.setText(null);

        issueDateHolder.setText(null);
        noOfDaysHolder.setText(null);
        fineHolder.setText(null);
       enableRenewReturnButton(false);
        issueDataContainer.setOpacity(0);
    }

    private void clearIssueData() {
        bookIDInput.clear();
        matricNoInput.clear();

        bookName.setText(null);
        authorName.setText(null);
        bookStatus.setText(null);

        getStudentName.setText(null);
        getDepartment.setText(null);
        enablePieChart(true);

    }
    private void refreshPieChart(){
        bookChart.setData(databaseHandler.getBookStatistics());
        memberChart.setData(databaseHandler.getMemberStatistics());
        enableIssueButton(false);
    }


}


