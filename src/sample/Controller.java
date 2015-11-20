package sample;

import DomParser.ForecastParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;

public class Controller {

    //Pruebas
    private int limitPrevisio;
    private ForecastParser prediccio=new ForecastParser();

    public static final ObservableList data =
            FXCollections.observableArrayList();

    public ListView ForecastResults = new ListView(data);


    public void initialize(){

        for (int i = 0; i < 18; i++) {
            data.add("anonym");
        }
        limitPrevisio=prediccio.getTotalPrevisiones();
        data.add(prediccio.toString(limitPrevisio));
        ForecastResults.setItems(data);

    }


}

//http://api.openweathermap.org/data/2.5/forecast/city?id=3128760&units=imperial&APPID=059046f3861b6d1faeba2ab024a1cf31&mode=XML
//City id list http://openweathermap.org/help/city_list.txt