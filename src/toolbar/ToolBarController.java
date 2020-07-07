package toolbar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import utilpackage.LibraryUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ToolBarController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

}
