package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage=primaryStage;
        FXMLLoader loader=new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("EL TEMPS");
        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.show();


        FXMLLoader detalls = new FXMLLoader(getClass().getResource("detalls.fxml"));
        Parent page = detalls.load();
        Stage detallStg = new Stage();
        detallStg.initModality(Modality.WINDOW_MODAL);
        detallStg.initOwner(primaryStage);
        detallStg.setScene(new Scene(page));

        // Guardem el dialogStage al controlador per poder accedir-hi i mostrar ña finestra
        ((DetallsController)detalls.getController()).setDetallsStage(detallStg);

        // Guardem el controlador del diàleg al controlador principal per poder cridar el mètode que
        // el mostra quan triem la opció al menú.
        ((Controller)loader.getController()).setDetallsControler(detalls.getController());

    }

    public static Stage getStage(){
        return stage;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
