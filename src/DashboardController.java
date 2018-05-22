import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controller for showing the dashboard (main page)
 * @author Duc Minh Le (s3651764)
 */
public class DashboardController {

    // The table
    public TableView<Person> peopleTableView;
    public TableColumn<Person, Boolean> checkBoxCol;
    public TableColumn<Person, String> nameCol;
    public TableColumn<Person, String> statusCol;
    public TableColumn<Person, String> genderCol;
    public TableColumn<Person, String> ageCol;
    public TableColumn<Person, String> stateCol;

    public void initialize() {
        checkBoxCol.setCellFactory(tableCell -> new CheckBoxTableCell<>());
        checkBoxCol.setCellValueFactory(param -> param.getValue().selected);
        nameCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().name));
        statusCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().status));
        genderCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().gender? "Female" : "Male"));
        ageCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().age)));
        stateCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().state));

        // Double click to show the person profile
        peopleTableView.setRowFactory(tableView -> {
            TableRow<Person> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showPersonDetails(row.getItem());
                }
            });

            return row;
        });

        // Assign data to table
        peopleTableView.setItems(NetManager.getAllPeople());
    }

    void showPersonDetails(Person p) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile.fxml"));
            Parent root = loader.load();
            ProfileController controller = loader.getController();
            controller.setPerson(p);

            Stage stage = new Stage();
            stage.setTitle("Profile");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickAddNewPerson(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewPerson.fxml"));
            Parent root = loader.load();
            NewPersonController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Add New Person");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickViewProfile(ActionEvent actionEvent) {
        // Check if the table has a selection
        Person p = peopleTableView.getSelectionModel().getSelectedItem();

        // If there is no selection, show the message
        if (p == null) {
            new Alert(Alert.AlertType.INFORMATION, "Please select a person").show();
            return;
        }

        // Else show the person profile
        showPersonDetails(p);
    }

    public void onClickDeleteSelected(ActionEvent actionEvent) {
        Person p = peopleTableView.getSelectionModel().getSelectedItem();
        if (p == null) {
            new Alert(Alert.AlertType.INFORMATION, "Please select a person").show();
            return;
        }

        // Confirm before doing the deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete \"" + p.name + "\" and all related data?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();

        // After user press the "Yes" button
        if (alert.getResult() == ButtonType.YES) {
            NetManager.removePersonByName(p.name);
        }
    }

    public void onClickManageRelationship(ActionEvent actionEvent) {
        // Check the number of selected people (ticked rows)
        int selectedCount = NetManager.getSelectedCount();

        // Only perform the action if number of ticks is 2
        if (selectedCount != 2) {
            new Alert(Alert.AlertType.INFORMATION, "Please select two people").show();
            return;
        }

        // Get the connection info between selected 2 people
        Relation relationOfSelected = NetManager.getConnectionBetweenSelectedPeople();

        if (relationOfSelected.type == null) {
            // There is no connection, prompt user to connect them
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageConnection.fxml"));
                Parent root = loader.load();
                ManageConnectionController controller = loader.getController();
                controller.selectedPeople = NetManager.getSelectedPeople();

                Stage stage = new Stage();
                stage.setTitle("Connection Chain");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            // They are connected directly, show the message
            new Alert(Alert.AlertType.INFORMATION,
                    "Connection between selected people is: " + relationOfSelected.type
            ).show();
        }

    }

    public void onClickConnectionChain(ActionEvent actionEvent) {
        int selectedCount = NetManager.getSelectedCount();
        if (selectedCount != 2) {
            new Alert(Alert.AlertType.INFORMATION, "Please select two people").show();
            return;
        }

        // Determine the connection chain
        ArrayList<Person> connectionChain = NetManager.getConnectionChain();

        // Chain size < 2 means they are not connected
        if (connectionChain.size() < 2) {
            new Alert(Alert.AlertType.INFORMATION, "Not Connected").show();
        }
        // Chain size == 2 means they are directly connected
        else if (connectionChain.size() == 2) {
            new Alert(Alert.AlertType.INFORMATION, "The selected two people are connected directly").show();
        }
        // Chain size > 2 means there is a connection chain
        else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ConnectionChain.fxml"));
                Parent root = loader.load();
                ConnectionChainController controller = loader.getController();
                controller.connectionChain = connectionChain;
                controller.postInitialize();

                Stage stage = new Stage();
                stage.setTitle("Connection Chain");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
