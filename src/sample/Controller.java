package sample;

import DomParser.Ciudad;
import DomParser.ForecastParser;
import Tabla.TablaModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Controller {

    //Tabla
    public TableView<TablaModel>tabla;

    public TableColumn<TablaModel, String>intervalo;
    public TableColumn<TablaModel, String>temperatura;
    public TableColumn<TablaModel, String>viento;
    public TableColumn<TablaModel, String>humedad;
    public TableColumn<TablaModel, String>presion;
    public TableColumn <TablaModel, ImageView>cielo;
    public RadioButton RbDia;
    public RadioButton RbHora;
    public MenuButton MbIntervalo;
    public Button actualizar;
    public TextField EtCiudad;
    public TextField EtPais;


    //Union tabla datos
    private ObservableList<TablaModel>datos=FXCollections.observableArrayList();
    //Para llenar el MenuButton con el intervalo de fechas
    private ObservableList<MenuItem>tiempoPrediccion=FXCollections.observableArrayList();



    //
    private RadioButton lastRadio;

    private ForecastParser prediccio=new ForecastParser();
    private ImageView imagen=new ImageView();
    private Ciudad c=new Ciudad();

    public void initialize(){
        RbHora.setSelected(true);
        lastRadio=RbHora;
        setIntervaloOptions("RbHora");
        EtCiudad.setText("Barcelona");
        prediccio.startPrediccion(EtCiudad.getText(), null);
        for(int i=0;i<prediccio.getTotalPrevisiones();i++){
            datos.addAll(new TablaModel(prediccio.getTime(i),prediccio.getTemp(i),prediccio.getWind(i),
                    prediccio.getHumity(i),prediccio.getPresure(i)));
        }

        intervalo.setCellValueFactory(new PropertyValueFactory<TablaModel, String>("Intervalo"));
        temperatura.setCellValueFactory(new PropertyValueFactory<TablaModel, String>("Temperatura"));
        viento.setCellValueFactory(new PropertyValueFactory<TablaModel, String>("Viento"));
        humedad.setCellValueFactory(new PropertyValueFactory<TablaModel, String>("Humedad"));
        presion.setCellValueFactory(new PropertyValueFactory<TablaModel, String>("Presion"));
      //  cielo.setCellValueFactory(new PropertyValueFactory<TablaModel, ImageView>("img"));

        tabla.setItems(datos);
    }


    public void setIntervalo(ActionEvent actionEvent) {
        RadioButton item=(RadioButton) actionEvent.getSource();
        /*
        Tooltip a = new Tooltip();
        a.setText(item.getText());
        a.show(Main.getStage());
        */
        if(lastRadio==null||lastRadio.getText().equals(item.getText())) {
            item.setSelected(true);
            return;
        }
        lastRadio.setSelected(false);
        item.setSelected(true);
        lastRadio=item;
        setIntervaloOptions(item.getId());
    }

    private void setIntervaloOptions(String opcion){
        tiempoPrediccion.clear();
        MbIntervalo.setDisable(false);
        if(opcion.equals("RbDia")){
            String dia=" Dia";
            for(int i=1;i<=14;i++) {
                MenuItem item = new MenuItem(i + dia);
                dia=" Dias";
                tiempoPrediccion.add(item);
                item.setOnAction(event -> {
                    MenuItem item1 = (MenuItem)event.getSource();
                    MbIntervalo.setText(item1.getText());
                });
            }

        }else{
            MbIntervalo.setText(null);
            MbIntervalo.setDisable(true);
        }
        MbIntervalo.getItems().addAll(tiempoPrediccion);
    }

    public void update(ActionEvent actionEvent) {
        Tooltip t= new Tooltip();
        prediccio.clearPrevisiones();
        prediccio.startPrediccion(EtCiudad.getText(), MbIntervalo.getText());
        datos.removeAll(datos);

        for(int i=0;i<prediccio.getTotalPrevisiones();i++){
            datos.addAll(new TablaModel(prediccio.getTime(i),prediccio.getTemp(i),prediccio.getWind(i),
                    prediccio.getHumity(i),prediccio.getPresure(i)));
        }
        tabla.setItems(datos);
        tabla.refresh();
    }

    //new Locale("es_ES")
}

//http://api.openweathermap.org/data/2.5/forecast/city?id=3128760&units=imperial&APPID=059046f3861b6d1faeba2ab024a1cf31&mode=XML
//City id list http://openweathermap.org/help/city_list.txt