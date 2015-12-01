package sample;

import DomParser.ForecastParser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by pau on 1/12/15.
 */
public class DetallsController {

    public ImageView IvIcon;
    public Text TxHumity;
    public Text Txtemp;
    public Text TxMoment;
    public Text TxSky;
    public Text TxWind;
    public Text TxPressure;
    private Stage detallStage;

    public void setDetallsStage(Stage detallStage) {
        this.detallStage = detallStage;
    }

    public void showDetalls(int index, ForecastParser prediccio){
        detallStage.setTitle(prediccio.getCiudad().toUpperCase());
        IvIcon.setImage(new Image(prediccio.getIcons(index)));
        TxHumity.setText(prediccio.getHumity(index));
        Txtemp.setText(prediccio.getTemp(index));
        TxMoment.setText(prediccio.getTime(index));
        TxSky.setText(prediccio.getClouds(index));
        TxWind.setText(prediccio.getWind(index));
        TxPressure.setText(prediccio.getPresure(index));
        detallStage.show();
    }
}
