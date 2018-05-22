import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.util.converter.IntegerStringConverter;

/**
 * Controller for the "Add New Person" form
 * @author Duc Minh Le (s3651764)
 */
public class NewPersonController {

    public TextField nameTextField;
    public TextField photoTextField;
    public TextField statusTextField;

    public ToggleGroup genderToggleGroup;
    public RadioButton maleRadioButton;
    public RadioButton femaleRadioButton;

    public TextField ageTextField;
    public ChoiceBox<String> stateChoiceBox;

    public void initialize() {
        // Populate state data to the 'state' choice box
        stateChoiceBox.setItems(FXCollections.observableArrayList("ACT", "NSW", "NT", "QLD", "SA", "TAS", "VIC", "WA"));

        // Restrict the 'age' text field to integer numbers only
        ageTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
    }

    public void onClickSaveBtn(ActionEvent actionEvent) {
        // Name should not be empty
        String name = nameTextField.getText().trim();
        if (name.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "Please enter name").show();
            return;
        }

        String photo = photoTextField.getText().trim();

        String status = statusTextField.getText().trim();

        // gender should be selected
        boolean gender;
        Toggle selectedToggle = genderToggleGroup.getSelectedToggle();
        if (selectedToggle == maleRadioButton) {
            gender = false;
        }
        else if (selectedToggle == femaleRadioButton) {
            gender = true;
        }
        else {
            new Alert(Alert.AlertType.INFORMATION, "Please select gender").show();
            return;
        }

        // age should be valid
        int age;
        try {
            age = Integer.parseInt(ageTextField.getText());
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.INFORMATION, "Please enter a valid age").show();
            return;
        }

        // state should be selected
        String state = stateChoiceBox.getSelectionModel().getSelectedItem();
        if (state == null) {
            new Alert(Alert.AlertType.INFORMATION, "Please select a state").show();
            return;
        }

        // Validation done, add the person to the network
        try {
            Person newPerson = new Person(name, photo, status, gender, age, state);
            NetManager.addPerson(newPerson);

            new Alert(Alert.AlertType.INFORMATION, "Successfully added a new person").showAndWait();
            nameTextField.getScene().getWindow().hide();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.INFORMATION, e.getMessage()).show();
        }
    }

    public void onClickCancelBtn(ActionEvent actionEvent) {
        nameTextField.getScene().getWindow().hide();
    }


}
