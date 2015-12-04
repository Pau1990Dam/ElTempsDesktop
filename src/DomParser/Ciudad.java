package DomParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by 14270729b on 26/11/15.
 */
public class Ciudad {
    private static TreeMap<String, Integer> ciudades = new TreeMap<String , Integer>();
     public Ciudad(){
        ciudades.put("Barcelona",3128760);
        ciudades.put("Madrid",6359304);
        ciudades.put("Sevilla",2510911);
        ciudades.put("Zaragoza",3104324);
        ciudades.put("Valencia",2509954);
        ciudades.put("Santiago de Compostela",6357346);
        ciudades.put("Valladolid",6362308);
        ciudades.put("Toledo",6361828);
        ciudades.put("Murcia",6355234);
        ciudades.put("Vitoria-Gasteiz",6355286);
        ciudades.put("Oviedo",6359947);
        ciudades.put("Santa Cruz de Tenerife",6360638);
        ciudades.put("Santander",6360720);
        ciudades.put("Pamplona",6359749);
        ciudades.put("Ceuta",2519582);
        ciudades.put("Melilla",6362988);
        ciudades.put("Logroño",6359078);
    }

    public int getCiudadId(String ciudad){
        return ciudades.get(ciudad);
    }



    /*
    public ArrayList<String> getCiudades(){
        ArrayList<String> cities=new ArrayList<>();
        Iterator it = ciudades.keySet().iterator();
        while(it.hasNext()){
            String key = (String) it.next();
            cities.add(key);
        }
        return cities ;
    }
*/
}
