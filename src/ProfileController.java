import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.AbstractMap.SimpleEntry;

/**
 * Controller for showing the profile window
 * @author Duc Minh Le (s3651764)
 */
public class ProfileController {

    public ImageView profilePicImgView;
    public Label nameLabel;
    public Label statusLabel;
    public Label genderLabel;
    public Label ageLabel;
    public Label stateLabel;

    public TableView<SimpleEntry<Person, RelationType>> peopleTableView;
    public TableColumn<SimpleEntry<Person, RelationType>, String> nameCol;
    public TableColumn<SimpleEntry<Person, RelationType>, String> genderCol;
    public TableColumn<SimpleEntry<Person, RelationType>, String> ageCol;
    public TableColumn<SimpleEntry<Person, RelationType>, String> stateCol;
    public TableColumn<SimpleEntry<Person, RelationType>, String> typeCol;

    public void initialize() {
        nameCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getKey().name));
        genderCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getKey().getGenderText()));
        ageCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getKey().age)));
        stateCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getKey().state));
        typeCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().name()));

        peopleTableView.setRowFactory(tableView -> {
            TableRow<SimpleEntry<Person, RelationType>> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    setPerson(row.getItem().getKey());
                }
            });

            return row;
        });
    }

    public void setPerson(Person p) {
        try {
            File imageFile = new File(p.photo);
            profilePicImgView.setImage(new Image(imageFile.toURI().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        nameLabel.setText(p.name);
        statusLabel.setText(p.status);
        genderLabel.setText(p.getGenderText());
        ageLabel.setText(String.valueOf(p.age));
        stateLabel.setText(p.state);

        peopleTableView.setItems(NetManager.getRelatedPeople2(p.name));
    }

}
