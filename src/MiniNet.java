import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Application startup class
 * @author Duc Minh Le (s3651764)
 */
public class MiniNet extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Parent root = loader.load();
        DashboardController controller = loader.getController();

        try {
            NetManager.loadFromFile("people.txt", "relations.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("MiniNet");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
