package a22.sim203;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class controleur pour le Tp2
 * @author Christophe Guerin Adam Lidam
 * @version 1.0
 */
public class CalculatriceApp extends Application {
    /**
     * Lance et crée le Stage à partir du fichier FXML
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        //implement le fichier Fxml et l'instancie entant que root
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(this.getClass().getResourceAsStream("calc.fxml"));

        //créé la scene
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        //definite les dimensions de la scene
        primaryStage.setMinWidth(1050);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}