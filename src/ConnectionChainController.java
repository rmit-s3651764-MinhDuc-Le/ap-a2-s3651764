import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;

/**
 * Controller for showing the connection chain window
 * @author Duc Minh Le (s3651764)
 */
public class ConnectionChainController {

    // The table
    public TableView<Person> peopleTableView;
    public TableColumn<Person, String> nameCol;
    public TableColumn<Person, String> statusCol;
    public TableColumn<Person, String> genderCol;
    public TableColumn<Person, String> ageCol;
    public TableColumn<Person, String> stateCol;

    // Chain data
    ArrayList<Person> connectionChain;

    public void initialize() {
        nameCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().name));
        statusCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().status));
        genderCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().gender? "Female" : "Male"));
        ageCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().age)));
        stateCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().state));
    }

    public void postInitialize() {
        peopleTableView.setItems(FXCollections.observableArrayList(connectionChain));
    }

}
