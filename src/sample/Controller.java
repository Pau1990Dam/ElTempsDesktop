package sample;

import DomParser.Ciudad;
import DomParser.ForecastParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import javax.swing.*;


public class Controller {

    //Pruebas
    private int limitPrevisio;
    private ForecastParser prediccio=new ForecastParser();
    private ImageView imagen=new ImageView();
    private Ciudad c=new Ciudad();
    //public Text ciudad;
    //private ObservableList<String> ciudades =

    //public ComboBox

    public static final ObservableList data =
            FXCollections.observableArrayList();

    public ListView ForecastResults = new ListView(data);


    public void initialize(){

        limitPrevisio=prediccio.getTotalPrevisiones();
        data.add(prediccio.getPrediccion(limitPrevisio,"Barcelona"));
        ForecastResults.setItems(data);
        Image icon= new Image("/icons/broken_clouds.png");
        //ciudad.setText(prediccio.getCiudad());
    }


}

//http://api.openweathermap.org/data/2.5/forecast/city?id=3128760&units=imperial&APPID=059046f3861b6d1faeba2ab024a1cf31&mode=XML
//City id list http://openweathermap.org/help/city_list.txt