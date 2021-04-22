import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        View view = new View(stage);
        Scene scene = new Scene(view, 1200, 780);
        stage.setScene(scene);
        stage.setMaxWidth(1200);
        stage.setMaxHeight(780);
        stage.setMinWidth(400);
        stage.setMinHeight(280);
        stage.setTitle("Bezier Curve (b327zhan)");
        stage.show();
    }
}
