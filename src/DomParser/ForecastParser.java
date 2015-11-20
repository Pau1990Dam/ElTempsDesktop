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

/**
 * Created by 14270729b on 13/11/15.
 */
public class ForecastParser {

    private MiniTraductor traductor=new MiniTraductor();
    private String ciudad;
    private String pais;
    private ArrayList<String>tiempo=new ArrayList<>();
    private ArrayList<String>temperaturas=new ArrayList<>();
    private ArrayList<String> direccionViento=new ArrayList<>();
    private ArrayList<String>viento=new ArrayList<>();
    private ArrayList<String> presion=new ArrayList<>();
    private ArrayList<String> humedad =new ArrayList<>();
    private ArrayList<String> nubes =new ArrayList<>();

    public Document CarregarDocXML() throws
            ParserConfigurationException, IOException, SAXException {

        String url="http://api.openweathermap.org/data/2.5/forecast/city?id=3128760&units=metric&lang=es&APPID=059046f3861b6d1faeba2ab024a1cf31&mode=XML";
        DocumentBuilderFactory Factoria
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder carregarXML = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
        Document doc = carregarXML.parse(new URL(url).openStream());
        doc.getDocumentElement().normalize();

        return doc;

    }

    public void parser() throws IOException, SAXException, ParserConfigurationException, ParseException {
        Document doc =this.CarregarDocXML();
        if(doc.hasChildNodes()){
            lectorMetadatos(doc);
            lectorPrevision(doc);
        }
    }

    public void lectorMetadatos(Document doc){
        NodeList nodes=doc.getElementsByTagName("location");

        ciudad=nodes.item(0).getFirstChild().getFirstChild().getNodeValue();
        nodes=doc.getElementsByTagName("country");
        pais=nodes.item(0).getFirstChild().getNodeValue();
    }

    public void lectorPrevision(Document doc) throws ParseException {
        SimpleDateFormat inputFormat= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DecimalFormat formato= new DecimalFormat("#0.00");
        double velocidadViento;
        NodeList nodes = doc.getElementsByTagName("time");

        for(int i=0;i<nodes.getLength();i++){
            Element elemento=(Element)nodes.item(i);
            tiempo.add("Desde "+outputFormat.format(inputFormat.parse(elemento.getAttributes().getNamedItem("from").
                    getNodeValue()))+"\t\tHasta "+outputFormat.format(inputFormat.parse(elemento.getAttributes().
                    getNamedItem("to").getNodeValue())));
            temperaturas.add("media: " + elemento.getElementsByTagName("temperature").item(0).getAttributes().
                    getNamedItem("value")
                    .getNodeValue()+" ºC" + "\tmínima: " + elemento.getElementsByTagName("temperature").item(0).getAttributes().
                    getNamedItem("min")
                    .getNodeValue()+" ºC" + "\tmáxima:" + elemento.getElementsByTagName("temperature").item(0).getAttributes().
                    getNamedItem("max").getNodeValue()+" ºC");
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

    public String toString(int i){
        try {
            parser();
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

    public int getTotalPrevisiones(){
        return temperaturas.size();
    }

}


// http://api.openweathermap.org/data/2.5/forecast/daily?id=3128760&units=metric&lang=es&APPID=059046f3861b6d1faeba2ab024a1cf31&mode=XML&cnt=14