package DomParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by 14270729b on 13/11/15.
 */
public class ForecastParser {

    private MiniTraductor traductor=new MiniTraductor();
    private String ciudad;
    private int ciudadId=3128760;
    private String url;
    private Ciudad city=new Ciudad();
    private String pais;
    private ArrayList<String>tiempo=new ArrayList<>();
    private ArrayList<String>temperaturas=new ArrayList<>();
    private ArrayList<String>direccionViento=new ArrayList<>();
    private ArrayList<String>viento=new ArrayList<>();
    private ArrayList<String>presion=new ArrayList<>();
    private ArrayList<String>humedad =new ArrayList<>();
    private ArrayList<String>nubes =new ArrayList<>();

    public void startPrediccion(String city, String periodo){
        try {
            if(periodo!=null)periodo=periodo.substring(0, periodo.lastIndexOf(" "));
            parser(city, periodo);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void parser(String city, String periodo) throws IOException, SAXException, ParserConfigurationException, ParseException {
        url=urlConstructor(city, periodo);
        Document doc =this.CarregarDocXML(url);
        if(doc.hasChildNodes()){
            lectorMetadatos(doc);
            lectorPrevision(doc, periodo);
        }
    }

    public void lectorMetadatos(Document doc){
        NodeList nodes=doc.getElementsByTagName("location");
        ciudad=nodes.item(0).getFirstChild().getFirstChild().getNodeValue();
        nodes=doc.getElementsByTagName("country");
        pais=nodes.item(0).getFirstChild().getNodeValue();
    }

    public void lectorPrevision(Document doc, String periodo) throws ParseException {
        SimpleDateFormat inputFormat= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat= new SimpleDateFormat("dd/MM/yyyy  HH:mm");
        if(periodo!=null){
            inputFormat=new SimpleDateFormat("yyyy-MM-dd");
            outputFormat=new SimpleDateFormat("EEE dd MMM yyyy", new Locale("ES"));
        }

        DecimalFormat formato= new DecimalFormat("#0.00");
        double velocidadViento;
        NodeList nodes = doc.getElementsByTagName("time");

        for(int i=0;i<nodes.getLength();i++){
            Element elemento=(Element)nodes.item(i);
            if(periodo==null) {
                tiempo.add(outputFormat.format(inputFormat.parse(elemento.getAttributes().getNamedItem("from").
                        getNodeValue())) + "  - " + outputFormat.format(inputFormat.parse(elemento.getAttributes().
                        getNamedItem("to").getNodeValue())).substring(10));
            }else{
                tiempo.add(outputFormat.format(inputFormat.parse(elemento.getAttributes().getNamedItem("day").
                        getNodeValue())).toUpperCase());
            }
            temperaturas.add("mínima: " + elemento.getElementsByTagName("temperature").item(0).getAttributes().
                    getNamedItem("min")
                    .getNodeValue() + " ºC" + "\tmáxima: " + elemento.getElementsByTagName("temperature").item(0).getAttributes().
                    getNamedItem("max").getNodeValue() + " ºC");
            direccionViento.add(traductor.vent(elemento.getElementsByTagName("windDirection").item(0).getAttributes().
                    getNamedItem("name").getNodeValue()));
            velocidadViento=1.60934*Double.parseDouble(elemento.getElementsByTagName("windSpeed").item(0).getAttributes().
                    getNamedItem("mps").getNodeValue())*3.6;
            viento.add("velocidad: "+formato.format(velocidadViento)+" Km/h");
            presion.add(elemento.getElementsByTagName("pressure").item(0).getAttributes().
                    getNamedItem("value").getNodeValue()+" hPa");
            humedad.add(elemento.getElementsByTagName("humidity").item(0).getAttributes().
                    getNamedItem("value").getNodeValue()+" %");
            nubes.add(traductor.nuvols(elemento.getElementsByTagName("clouds").item(0).getAttributes().
                    getNamedItem("value").getNodeValue()));
        }
    }

    public String getPrediccion(int i, String city, String periodo){
        try {
            parser(city, periodo);
            return tiempo.get(i)+"\n\t"+
                    "Cielo :"+nubes.get(i)+"\n\t"+
                    "Temperatura :"+temperaturas.get(i)+"\n\t"+
                    "Dirección del viento: "+direccionViento.get(i)+"\t"+viento.get(i)+"\n\t"+
                    "Presión :"+presion.get(i)+"\n\t"+
                    "Humedad: "+humedad.get(i);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "FAIL";
    }

    public String getTemp(int i){return temperaturas.get(i);}

    public String getTime(int i){return tiempo.get(i);}

    public String getWind(int i){return direccionViento.get(i)+"\t"+viento.get(i);}

    public String getPresure(int i){return presion.get(i);}

    public String getHumity(int i){return humedad.get(i);}

    public String getClouds(int i){return nubes.get(i);}

    public String getCiudad(){
        return ciudad;
    }

    public String getPais(){
        return pais;
    }

    public int getTotalPrevisiones(){return temperaturas.size();}

    public void clearPrevisiones(){
        tiempo.clear();
        temperaturas.clear();
        presion.clear();
        nubes.clear();
        humedad.clear();
        viento.clear();
        direccionViento.clear();
    }

    public ArrayList<String> getIntervalos(){
        return tiempo;
    }

    private String urlConstructor(String city, String periodo){
        StringBuilder constructorUrl= new StringBuilder();
        if(periodo!=null)constructorUrl.append("http://api.openweathermap.org/data/2.5/forecast/daily?id=");
        else constructorUrl.append("http://api.openweathermap.org/data/2.5/forecast/city?id=");
        constructorUrl.append(String.valueOf(this.city.getCiudadId(city)));
        constructorUrl.append("&units=metric&lang=es&APPID=059046f3861b6d1faeba2ab024a1cf31&mode=XML");
        if(periodo!=null)constructorUrl.append("&cnt="+periodo);

        return constructorUrl.toString();

    }

    private Document CarregarDocXML(String url) throws
            ParserConfigurationException, IOException, SAXException {

        URL Url=new URL(url);
        //String url="http://api.openweathermap.org/data/2.5/forecast/city?id=3128760&units=metric&lang=es&APPID=059046f3861b6d1faeba2ab024a1cf31&mode=XML";
        DocumentBuilderFactory Factoria
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder carregarXML = Factoria
                .newDocumentBuilder();
        //Document doc = carregarXML.parse(new URL(url).openStream());
        Document doc = carregarXML.parse(String.valueOf(Url));
        doc.getDocumentElement().normalize();

        return doc;

    }

}


// "http://api.openweathermap.org/data/2.5/forecast/city?id=3128760&units=metric&lang=es&APPID=059046f3861b6d1faeba2ab024a1cf31&mode=XML";

// http://api.openweathermap.org/data/2.5/forecast/daily?id=3128760&units=metric&lang=es&APPID=059046f3861b6d1faeba2ab024a1cf31&mode=XML&cnt=14