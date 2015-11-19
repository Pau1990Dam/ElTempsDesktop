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
import java.util.ArrayList;

/**
 * Created by 14270729b on 13/11/15.
 */
public class ForecastParser {

    private String ciudad;
    private String pais;
    private ArrayList<String>temperaturas=new ArrayList<>();
    private ArrayList<String> direccionViento=new ArrayList<>();
    private ArrayList<String>viento=new ArrayList<>();;

    public Document CarregarDocXML() throws
            ParserConfigurationException, IOException, SAXException {

        String url="http://api.openweathermap.org/data/2.5/forecast/city?id=3128760&units=metric&APPID=059046f3861b6d1faeba2ab024a1cf31&mode=XML";
        DocumentBuilderFactory Factoria
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder carregarXML = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
        Document doc = carregarXML.parse(new URL(url).openStream());
        doc.getDocumentElement().normalize();

        return doc;

    }

    public void parser() throws IOException, SAXException, ParserConfigurationException {
        Document doc =this.CarregarDocXML();
        if(doc.hasChildNodes()){
            lectorMetadatos(doc);
            lectorPrevision(doc);
        }
    }

    public void lectorMetadatos(Document doc){
        NodeList nodes=doc.getElementsByTagName("name");
        ciudad=nodes.item(0).getNodeValue();
        nodes=doc.getElementsByTagName("country");
        pais=nodes.item(0).getNodeValue();
    }

    public void lectorPrevision(Document doc){
        NodeList nodes = doc.getElementsByTagName("time");
        for(int i=0;i<nodes.getLength();i++){
            Element elemento=(Element)nodes.item(i);
            // temperaturaMin.add(elementos.getElementsByTagName("temperature").item(0).getAttributes().getNamedItem("min").getNodeValue() + "º");
            temperaturas.add("media: "+elemento.getElementsByTagName("temperature").item(0).getAttributes().getNamedItem("value")
                    .getNodeValue()+"\tmínima: "+elemento.getElementsByTagName("temperature").item(0).getAttributes().getNamedItem("min")
                    .getNodeValue()+"\tmáxima:"+elemento.getElementsByTagName("temperature").item(0).getAttributes().
                    getNamedItem("max").getNodeValue());
        }

    }

}
