package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Domenico on 19/12/2016.
 */
public class ClientCameriere extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("sample4.fxml"));
        primaryStage.setTitle("Albergo dei Fiori");
        primaryStage.setScene(new Scene(root, 732, 561));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}