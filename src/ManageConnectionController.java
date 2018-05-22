import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;

import java.util.ArrayList;

/**
 * Controller for showing the "Manage Connection" window
 * @author Duc Minh Le (s3651764)
 */
public class ManageConnectionController {

    public ChoiceBox<RelationType> relationChoiceBox;
    ArrayList<Person> selectedPeople;

    public void initialize() {
        relationChoiceBox.setItems(FXCollections.observableArrayList(RelationType.values()));
    }

    public void onClickSaveBtn(ActionEvent actionEvent) {
        try {
            NetManager.connect(selectedPeople.get(0), selectedPeople.get(1), relationChoiceBox.getSelectionModel().getSelectedItem());
            new Alert(Alert.AlertType.INFORMATION, "Operation completed successfully!").show();
            relationChoiceBox.getScene().getWindow().hide();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.INFORMATION, e.getMessage()).show();
        }
    }

    public void onClickCancelBtn(ActionEvent actionEvent) {
        relationChoiceBox.getScene().getWindow().hide();
    }
}
