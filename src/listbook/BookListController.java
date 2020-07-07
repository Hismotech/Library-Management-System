package listbook;
import addbook.AddBookController;
import com.sun.javafx.css.converters.BooleanConverter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import notification.AlertMaker;
import database.DatabaseHandler;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.Initializable;
import utilpackage.LibraryUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class BookListController implements Initializable {
    ObservableList<Book> bookList = FXCollections.observableArrayList();
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<Book> tableView;

    @FXML
    private TableColumn<Book, String> idColumn;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> publisherColumn;

    @FXML
    private TableColumn<Book, Boolean> availabilityColumn;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        intitiateColumn();
        loadBooks();
    }

    private void loadBooks(){
        bookList.clear();
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String query = "Select * from Book";
        ResultSet rs = handler.executeQuery(query);
        try {
            while (rs.next()) {
               bookList.add(new Book( rs.getString("id"),rs.getString("title"),
                       rs.getString("author"), rs.getString("publisher"),
                       rs.getBoolean("isAvailable")));
            }
        }
        catch(SQLException se) {
            System.err.println(se.getMessage());
        }
        tableView.setItems(bookList);

    }
    private void intitiateColumn(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availabilityColumn.setCellValueFactory((new PropertyValueFactory<>("availability")));
    }

    public static class Book {
        private final SimpleStringProperty  id;
        private final SimpleStringProperty  title;
        private final SimpleStringProperty  author;
        private final SimpleStringProperty  publisher;
        private final SimpleBooleanProperty availability;


        public Book(String id, String title, String author, String publisher, boolean availability) {
            this.id           = new SimpleStringProperty(id);
            this.title        = new SimpleStringProperty(title);
            this.author       = new SimpleStringProperty(author);
            this.publisher    = new SimpleStringProperty(publisher);
            this.availability = new SimpleBooleanProperty(availability);
        }

        public String getId() {
            return id.get();
        }

        public String getTitle() {
            return title.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public String getPublisher() {
            return publisher.get();
        }

        public boolean isAvailability() {
            return availability.get();
        }

    }

    @FXML
    void handleBookDelete(ActionEvent event) {
        Book selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
        if(selectedForDeletion == null)
        {
            AlertMaker.showSimpleErrorMessage("No Book Selected", "Select a Book For Removal");
            return;
        }

        if( DatabaseHandler.getInstance().isBookAlreadyIssued(selectedForDeletion))
        {
            AlertMaker.showInformation("Can't be Removed", " Sorry!!! " +selectedForDeletion.getTitle()+
                    " Has Been Issued Already");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Do You Want To Remove " + selectedForDeletion.getTitle() + " From The List");

        Optional<ButtonType>answer = alert.showAndWait();

        if (answer.get() == ButtonType.OK){
            boolean response =DatabaseHandler.getInstance().deleteBook(selectedForDeletion);
         if (response)
       {
           AlertMaker.showInformation(null, selectedForDeletion.getTitle() + " Was Removed Successfully");
           bookList.remove(selectedForDeletion);
       }

       else{
          AlertMaker.showSimpleErrorMessage(null,"Sorry!!!! "+ selectedForDeletion.getTitle() +
                  " Could Not Be Removed ");
       }
      }
        else
      {
          AlertMaker.showSimpleErrorMessage(null,"Removal Operation Cancelled");
      }
    }

    @FXML
    void handleBookEdit(ActionEvent event) {
     Book selectedForEdit = tableView.getSelectionModel().getSelectedItem();
     if(selectedForEdit == null){
         AlertMaker.showInformation("No book selected","Please select a book");
         return;
     }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addbook/AddBook.fxml"));
            Parent parent = loader.load();
            AddBookController controller = (AddBookController)loader.getController();
            controller.inflateAddBookUi(selectedForEdit);
            Stage  stage  = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Book");
            stage.setScene(new Scene(parent));
            LibraryUtil.setStageIcon(stage);
            stage.show();
            stage.setOnCloseRequest((e) -> {
                handleBookRefresh(new ActionEvent());
            });
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    @FXML
    void handleBookRefresh(ActionEvent event) {
    loadBooks();
    }

}