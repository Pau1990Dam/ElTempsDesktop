package Tabla;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.Main;

import java.io.File;
import java.net.MalformedURLException;

/**
 * Created by pau on 29/11/15.
 */
public class TablaModel {
    private ImageView img;
    private SimpleStringProperty intervalo;
    private SimpleStringProperty temperatura;
    private SimpleStringProperty viento;
    private SimpleStringProperty humedad;
    private SimpleStringProperty presion;
    private SimpleStringProperty cielo;

    public TablaModel(String intervalo, String temperatura, String viento, String humedad, String presion, String cielo) {//, String cielo
        this.intervalo = new SimpleStringProperty(intervalo);
        this.temperatura = new SimpleStringProperty(temperatura);
        this.viento = new SimpleStringProperty(viento);
        this.humedad = new SimpleStringProperty(humedad);
        this.presion = new SimpleStringProperty(presion);
        this.cielo=new SimpleStringProperty(cielo);
       // Image image=new Image(Main.class.getResourceAsStream("/broken_clouds.png"));
       // img.setImage(image);
    }

    public String getIntervalo() {
        return intervalo.get();
    }

 /*   public ImageView getImg() {
        return img;
    }*/

    public void setIntervalo(String intervalo) {
        this.intervalo.set(intervalo);
    }

    public String getTemperatura() {
        return temperatura.get();
    }

    public void setTemperatura(String temperatura) {
        this.temperatura.set(temperatura);
    }

    public String getViento() {
        return viento.get();
    }

    public void setViento(String viento) {
        this.viento.set(viento);
    }

    public String getHumedad() {
        return humedad.get();
    }

    public void setHumedad(String humedad) {
        this.humedad.set(humedad);
    }

    public String getPresion() {
        return presion.get();
    }

    public void setPresion(String presion) {this.presion.set(presion);}

    public ImageView getCielo(){return new ImageView(cielo.get());}

   // public void setCielo(String cielo){this.cielo.set(cielo);}

}
