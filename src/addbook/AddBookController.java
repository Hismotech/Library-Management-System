package addbook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.sql.ResultSet;

import database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import listbook.BookListController.Book;
import notification.AlertMaker;


import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddBookController implements Initializable {

    @FXML
    private JFXTextField idField;
    @FXML
    private JFXTextField titleField;
    @FXML
    private JFXTextField authorField;
    @FXML
    private JFXTextField publisherField;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private AnchorPane anchorPane;

    private boolean isItEditMode = false;

    DatabaseHandler handler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        handler = DatabaseHandler.getInstance();
        checkBooks();
    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void addBook(ActionEvent event) {
        String bookId = idField.getText();
        String bookTitle = titleField.getText();
        String bookAuthor = authorField.getText();
        String bookPublisher = publisherField.getText();
        if(bookTitle.isEmpty()||bookId.isEmpty()||bookAuthor.isEmpty()||bookPublisher.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Input All Fields!!!");
            alert.showAndWait();
            return;
        }
        if(isItEditMode){
            handleBookUpdate();
            return;
        }
        String action = "insert into Book values(" + "'" + bookId + "'," +
        "'"+ bookTitle +"'," + "'"+ bookAuthor + "'," + "'"+ bookPublisher +"'," + ""+ "true" +""+ ")";
        System.out.println(action);

        if (handler.executeAction(action)) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Book Added Successfully");
            alert.showAndWait();
        }
        else{

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failure in Adding Book");
            alert.showAndWait();

        }
        }

    private void checkBooks() {
        String query = "Select * from book";
        ResultSet rs = handler.executeQuery(query);
        try {
                while (rs.next())
//                  System.out.printf("%s%n", rs.getString("title"));
               System.out.println(rs.getString("id")+ rs.getString("title")+
                  rs.getString("author")+ rs.getString("publisher")+
                       rs.getBoolean("isAvailable"));
            }
            catch (SQLException se) {
                System.err.println(se.getMessage());
            }

        }

     public  void inflateAddBookUi(Book book){
            idField.setText(book.getId());
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            publisherField.setText(book.getPublisher());
            idField.setEditable(false);
            isItEditMode = true;
    }

    private void handleBookUpdate() {
        Book book = new Book(idField.getText(),titleField.getText(),
                authorField.getText(),publisherField.getText(), true);
        if (handler.updateBook(book)){
            AlertMaker.showInformation(null, "Book Updated Sucessfully");
        }
        else
        {
            AlertMaker.showSimpleErrorMessage(null,"Error in updating book");
        }

    }
    }






