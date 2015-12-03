package sample;

import DomParser.Ciudad;
import DomParser.ForecastParser;
import Tabla.TablaModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.Dialog;
import java.io.File;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

public class Controller {

    public RadioButton RbSemana;
    private DetallsController detalls;
    //Tabla
    public TableView<TablaModel>tabla;

    public TableColumn<TablaModel, String>intervalo;
    public TableColumn<TablaModel, String>temperatura;
    public TableColumn<TablaModel, String>viento;
    public TableColumn<TablaModel, String>humedad;
    public TableColumn<TablaModel, String>presion;
    public TableColumn <TablaModel, Image>cielo;//ImageView
    public RadioButton RbDia;
    public RadioButton RbHora;
    public MenuButton MbIntervalo;
    public Button actualizar;
    public TextField EtCiudad;
    public TextField EtPais;
    public LineChart Grafica;
    public Tab tbGrafica;


    //Union tabla datos
    private ObservableList<TablaModel>datos=FXCollections.observableArrayList();
    //Para llenar el MenuButton con el intervalo de fechas
    private ObservableList<MenuItem>tiempoPrediccion=FXCollections.observableArrayList();

/*


TableView<MyType> table = new TableView<>();

//...

table.setRowFactory( tv -> {
    TableRow<MyType> row = new TableRow<>();
    row.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
            MyType rowData = row.getItem();
            System.out.println(rowData);
        }
    });
    return row ;
});


 */

    //
    private RadioButton lastRadio;

    private ForecastParser prediccio=new ForecastParser();
    private Ciudad c=new Ciudad();

    public void initialize(){
        tabla.setRowFactory( tv -> {
        TableRow<TablaModel> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
            if (!row.isEmpty()) {
                TablaModel rowData = row.getItem();
                //funcio
                mostraDetalls(row.getIndex(), prediccio );
            }
        });
        return row;
    }

    );
        RbHora.setSelected(true);
        lastRadio=RbHora;
        setIntervaloOptions("RbHora");
        EtCiudad.setText("Barcelona");

        intervalo.setCellValueFactory(new PropertyValueFactory<TablaModel, String>("Intervalo"));

        temperatura.setCellValueFactory(new PropertyValueFactory<TablaModel, String>("Temperatura"));
        viento.setCellValueFactory(new PropertyValueFactory<TablaModel, String>("Viento"));
        humedad.setCellValueFactory(new PropertyValueFactory<TablaModel, String>("Humedad"));
        presion.setCellValueFactory(new PropertyValueFactory<TablaModel, String>("Presion"));
        cielo.setCellValueFactory(new PropertyValueFactory<TablaModel, Image>("Cielo"));

        actualizar.fire();
    }


    public void setIntervalo(ActionEvent actionEvent) {
        RadioButton item=(RadioButton) actionEvent.getSource();
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
        MbIntervalo.getItems().removeAll(tiempoPrediccion);
        tiempoPrediccion.removeAll(tiempoPrediccion);
        MbIntervalo.setDisable(false);

        if(opcion.equals("RbDia")){
            String dia=" Dia";
            MbIntervalo.setText("Dias");
            for(int i=1;i<=17;i++) {
                MenuItem item = new MenuItem(i + dia);
                dia=" Dias";
                tiempoPrediccion.add(item);
                addListener(item);
            }
        }else if(opcion.equals("RbSemana")){
            MbIntervalo.setText("Semanas");
            tiempoPrediccion.clear();
            MenuItem item = new MenuItem("1 Semana");
            tiempoPrediccion.add(item);
            addListener(item);
            item = new MenuItem("2 Semanas");
            tiempoPrediccion.add(item);
            addListener(item);
        }else{
            MbIntervalo.setText(null);
            MbIntervalo.setDisable(true);
        }
        MbIntervalo.getItems().addAll(tiempoPrediccion);
    }

    private void addListener(MenuItem item){
        item.setOnAction(event -> {
            MenuItem item1 = (MenuItem) event.getSource();
            MbIntervalo.setText(item1.getText());
        });
    }

    public void update(ActionEvent actionEvent) throws ParseException, MalformedURLException {
        prediccio.clearPrevisiones();
        prediccio.startPrediccion(EtCiudad.getText(), MbIntervalo.getText());
        datos.removeAll(datos);
        System.out.println("Total predicciones: " + prediccio.getTotalPrevisiones());
        for(int i=0;i<prediccio.getTotalPrevisiones();i++){
            datos.addAll(new TablaModel(prediccio.getTime(i), prediccio.getTemp(i), prediccio.getWind(i),
                    prediccio.getHumity(i), prediccio.getPresure(i), prediccio.getIcons(i)));//,prediccio.getIcons(i)
        }
        tabla.setItems(datos);
        tabla.refresh();
        if(MbIntervalo.getText()==null)createGrafica("Mes-dia-hora");
        else createGrafica("Dia");
    }

    private void createGrafica(String intervalo) throws ParseException {

        Grafica.getData().clear();
        ArrayList<String>ejeX=new ArrayList<>();
        for(String momento: prediccio.getIntervalos()){
            ejeX.add(dataStringFormater(momento));
        }
        CategoryAxis abscisas = new CategoryAxis();
        NumberAxis ordenadas = new NumberAxis();
        ordenadas.setLabel("Temperatura en ºC");

        Grafica=new LineChart<String,Number>(abscisas,ordenadas);

        XYChart.Series minimas = new XYChart.Series();
        minimas.setName("minimas");
        XYChart.Series maximas = new XYChart.Series();
        maximas.setName("maximas");
        if (MbIntervalo.getText()==null||MbIntervalo.getText().length() < 8) {
            for(int i=0;i<prediccio.getTotalPrevisiones();i++){
                minimas.getData().add(new XYChart.Data(ejeX.get(i), Float.parseFloat(prediccio.getTemp(i).
                        substring(8, prediccio.getTemp(i).indexOf("º")))));
                maximas.getData().add(new XYChart.Data(ejeX.get(i), Float.parseFloat(prediccio.getTemp(i).
                        substring(prediccio.getTemp(i).lastIndexOf(":") + 1, prediccio.getTemp(i).lastIndexOf("º")))));
            }
        }else{
            for(int i=0;i<prediccio.getTotalPrevisiones();i++){
                minimas.getData().add(new XYChart.Data(ejeX.get(i), Float.parseFloat(prediccio.getTemp(i).
                        substring(15, prediccio.getTemp(i).indexOf("º")))));
                maximas.getData().add(new XYChart.Data(ejeX.get(i), Float.parseFloat(prediccio.getTemp(i).
                        substring(prediccio.getTemp(i).lastIndexOf(":") + 1, prediccio.getTemp(i).lastIndexOf("º")))));
            }
        }

        Grafica.getData().addAll(minimas, maximas);
        tbGrafica.setContent(Grafica);

    }

    private String dataStringFormater( String fecha) throws ParseException {
        SimpleDateFormat input;
        SimpleDateFormat ouput;

        if(MbIntervalo.getText()==null){
            input= new SimpleDateFormat("dd/MM/yyyy HH:mm");
            ouput= new SimpleDateFormat("dd/MM HH:mm");

            return ouput.format(input.parse(fecha));
        }else if(MbIntervalo.getText().length()<8){
            Locale local= new Locale("ES");
            input= new SimpleDateFormat("EEE dd MMM yyyy", local);
            ouput= new SimpleDateFormat("dd MMM", local);

            return ouput.format(input.parse(fecha));
        }else{
            return fecha.substring(1,9)+"\n"+fecha.substring(17,24)+" - "+fecha.substring(34,37);
        }
    }

    public void salir(ActionEvent actionEvent) {Platform.exit();}

    public void setDetallsControler(DetallsController detalls) {
        this.detalls = detalls;
    }

    public void mostraDetalls(int index, ForecastParser prediccio) {
        detalls.showDetalls(index, prediccio);
    }

    //new Locale("es_ES")
}

//http://api.openweathermap.org/data/2.5/forecast/city?id=3128760&units=imperial&APPID=059046f3861b6d1faeba2ab024a1cf31&mode=XML
//City id list http://openweathermap.org/help/city_list.txt