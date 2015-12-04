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
    private int weeks=0;
    private MiniTraductor traductor=new MiniTraductor();
    private String ciudad;
   // private int ciudadId=3128760;
    private String url;
    private Ciudad city=new Ciudad();
    private String pais;
    private ArrayList<String>tiempo=new ArrayList<>();
    private ArrayList<String>tempMin=new ArrayList<>();
    private ArrayList<String>tempMax=new ArrayList<>();
    private ArrayList<String>direccionViento=new ArrayList<>();
    private ArrayList<String>viento=new ArrayList<>();
    private ArrayList<String>presion=new ArrayList<>();
    private ArrayList<String>humedad =new ArrayList<>();
    private ArrayList<String>nubes =new ArrayList<>();
    private ArrayList<String>iconos=new ArrayList<>();

    public void startPrediccion(String city, String periodo){
        try {
            if(periodo!=null&&periodo.length()<8){
                periodo=periodo.substring(0, periodo.lastIndexOf(" "));
            }else if(periodo!=null&&periodo.length()>=8){
                weeks=Integer.parseInt(periodo.substring(0, periodo.lastIndexOf(" ")));
                periodo=String.valueOf(weeks*7);
            }
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
            tempMin.add(elemento.getElementsByTagName("temperature").item(0).getAttributes().
                    getNamedItem("min")
                    .getNodeValue());
            tempMax.add(elemento.getElementsByTagName("temperature").item(0).getAttributes().
                    getNamedItem("max")
                    .getNodeValue());
            direccionViento.add(traductor.vent(elemento.getElementsByTagName("windDirection").item(0).getAttributes().
                    getNamedItem("name").getNodeValue()));
            velocidadViento=1.60934*Double.parseDouble(elemento.getElementsByTagName("windSpeed").item(0).getAttributes().
                    getNamedItem("mps").getNodeValue())*3.6;
            viento.add(formato.format(velocidadViento));
            presion.add(elemento.getElementsByTagName("pressure").item(0).getAttributes().
                    getNamedItem("value").getNodeValue());
            humedad.add(elemento.getElementsByTagName("humidity").item(0).getAttributes().
                    getNamedItem("value").getNodeValue());
            nubes.add(traductor.nuvols(elemento.getElementsByTagName("clouds").item(0).getAttributes().
                    getNamedItem("value").getNodeValue()));
            iconos.add("http://openweathermap.org/img/w/"+elemento.getElementsByTagName("symbol").item(0).getAttributes().
                    getNamedItem("var").getNodeValue()+".png");
        }

        if(weeks>0){
            calcularMediasSemanales(weeks);
            weeks=0;
        }
    }


    public String getTempMin(int i){return tempMin.get(i);}

    public  String getTempMax(int i){return tempMax.get(i);}

    public String getTime(int i){return tiempo.get(i);}

    public String getWindDirection(int i){return direccionViento.get(i);}

    public String getWindVelocity(int i){return viento.get(i);}

    public String getPresure(int i){return presion.get(i);}

    public String getHumity(int i){return humedad.get(i);}

    public String getClouds(int i){return nubes.get(i);}

    public String getIcons(int i){return iconos.get(i);}

    public String getCiudad(){
        return ciudad;
    }

    public String getPais(){
        return pais;
    }

    public int getTotalPrevisiones(){return tempMin.size();}

    public void clearPrevisiones(){
        tiempo.clear();
        tempMin.clear();
        presion.clear();
        nubes.clear();
        humedad.clear();
        viento.clear();
        direccionViento.clear();
        iconos.clear();
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
        DocumentBuilderFactory Factoria
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder carregarXML = Factoria
                .newDocumentBuilder();
        Document doc = carregarXML.parse(String.valueOf(Url));
        doc.getDocumentElement().normalize();

        return doc;

    }

    private void calcularMediasSemanales(int weeks){
        DecimalFormat formato= new DecimalFormat("#0.00");
        int i=0;
        tiempo.add("\t\t\tSemana 1\n" + "\t" + tiempo.get(0) + " - " + tiempo.get(6));
        while(i<weeks){
            float calculadorMediaMin=0;
            float calculadorMediaMax=0;
            float calculadorMediaHumedad=0;
            float calculadorMediaPresion=0;
            float calculadorMediaViento=0;

         for(int j=7*i;j<7*(i+1);j++){
             calculadorMediaMin+=Float.parseFloat(tempMin.get(j));
             calculadorMediaMax+=Float.parseFloat(tempMax.get(j));
             calculadorMediaPresion+=Float.parseFloat(presion.get(j));
             calculadorMediaHumedad+=Float.parseFloat(humedad.get(j));
             calculadorMediaViento+=Float.parseFloat((viento.get(j)).replace(",","."));
         }
            tempMin.add((formato.format(calculadorMediaMin / 7)));
            tempMax.add((formato.format(calculadorMediaMax/7)));
            presion.add((formato.format(calculadorMediaPresion/7)).replace(",","."));
            humedad.add(formato.format(calculadorMediaHumedad/7));
            viento.add(formato.format(calculadorMediaViento/7));
            iconos.add("https://cdn1.iconfinder.com/data/icons/weather-2-colored/512/na_moon-48.png");
            nubes.add("");
            direccionViento.add("");
            if(i==1) { tiempo.add("\t\t\tSemana 2\n" +"\t"+ tiempo.get(7) + " - " + tiempo.get(13));}
            i++;
        }
        int lim=tempMin.size()-weeks;
        for(int x=0;x<lim;x++){
            tiempo.remove(0);
            tempMin.remove(0);
            tempMax.remove(0);
            presion.remove(0);
            viento.remove(0);
            humedad.remove(0);
            iconos.remove(0);
            nubes.remove(0);
            direccionViento.remove(0);
        }
    }

}


// "http://api.openweathermap.org/data/2.5/forecast/city?id=3128760&units=metric&lang=es&APPID=059046f3861b6d1faeba2ab024a1cf31&mode=XML";

// http://api.openweathermap.org/data/2.5/forecast/daily?id=3128760&units=metric&lang=es&APPID=059046f3861b6d1faeba2ab024a1cf31&mode=XML&cnt=14