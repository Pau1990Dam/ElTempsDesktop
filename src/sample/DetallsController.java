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
    public Text TxTemp;
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
        detallStage.setResizable(false);
        IvIcon.setImage(new Image(prediccio.getIcons(index)));
        TxHumity.setText("Humedad: " + prediccio.getHumity(index));

        TxTemp.setText("Temp: "+prediccio.getTemp(index).replace("mínima:", "").replace(prediccio.getTemp(index).
                substring(prediccio.getTemp(index).indexOf("C") + 1, prediccio.getTemp(index).lastIndexOf(":")+1)," - "));
        TxMoment.setText(prediccio.getTime(index).replace(prediccio.getTime(index).substring(0,10),
                prediccio.getTime(index).substring(0,5)).replace(":00","h"));
        TxSky.setText("Cielo: "+prediccio.getClouds(index));
        TxWind.setText("Viento: "+prediccio.getWind(index).replace("velocidad:",""));
        TxPressure.setText("Presión: "+prediccio.getPresure(index));
        detallStage.show();
    }
}
