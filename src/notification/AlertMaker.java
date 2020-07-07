package notification;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.util.List;

public class AlertMaker {
    public static  void showInformation(String title, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static  void showErrorMessage(Exception ex, String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showSimpleErrorMessage(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showConfirmationMessage(String title, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
    }

    public static void showMaterialDialog(StackPane stackPane, Node node, List<JFXButton> controls,String string1,String string2){
        BoxBlur                 blur            = new BoxBlur(3, 3, 3);
        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        JFXDialog       dialog          = new JFXDialog(stackPane, jfxDialogLayout, JFXDialog.DialogTransition.TOP);
        controls.forEach(controlButton->{
            controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
                dialog.close(); });
        });
        jfxDialogLayout.setHeading(new Label(string1));
        jfxDialogLayout.setActions(controls);
        dialog.show();
        dialog.setOnDialogClosed((JFXDialogEvent dialogEvent) -> {
            node.setEffect(null);
        });
        node.setEffect(blur);
        //JOptionPane.showMessageDialog(null,"Such Book Has Not Been Issued Yet");
    }
}


