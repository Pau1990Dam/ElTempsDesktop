package DomParser;

import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by 14270729b on 26/11/15.
 */
public class Ciudad {
    private static TreeMap<String, Integer> ciudades = new TreeMap<String , Integer>();
    Ciudad(){
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

}


/*
{"_id":2513917,"name":"Merida","country":"ES","coord":{"lon":-6.34366,"lat":38.916111}}
{"_id":6359304,"name":"Madrid","country":"ES","coord":{"lon":-3.68275,"lat":40.489349}}
{"_id":2510911,"name":"Sevilla","country":"ES","coord":{"lon":-5.97613,"lat":37.382408}}
{"_id":3104324,"name":"Zaragoza","country":"ES","coord":{"lon":-0.87734,"lat":41.656059}}

{"_id":2509954,"name":"Valencia","country":"ES","coord":{"lon":-0.37739,"lat":39.469749}}

{"_id":6357346,"name":"Santiago de Compostela","country":"ES","coord":{"lon":-8.54736,"lat":42.880241}}
{"_id":6362308,"name":"Valladolid","country":"ES","coord":{"lon":-4.72854,"lat":41.652302}}

{"_id":6361828,"name":"Toledo","country":"ES","coord":{"lon":-4.00988,"lat":39.867649}}

{"_id":6355234,"name":"Murcia","country":"ES","coord":{"lon":-1.14146,"lat":37.986622}}

{"_id":6355286,"name":"Vitoria-Gasteiz","country":"ES","coord":{"lon":-2.66976,"lat":42.859081}}
{"_id":6359947,"name":"Oviedo","country":"ES","coord":{"lon":-5.87339,"lat":43.35796}}

{"_id":6360638,"name":"Santa Cruz de Tenerife","country":"ES","coord":{"lon":-16.20302,"lat":28.53924}}

{"_id":6360720,"name":"Santander","country":"ES","coord":{"lon":-3.81994,"lat":43.472252}}

{"_id":6359749,"name":"Pamplona","country":"ES","coord":{"lon":-1.64432,"lat":42.812759}}
{"_id":2519582,"name":"Ceuta","country":"ES","coord":{"lon":-5.3075,"lat":35.890282}}
{"_id":6362988,"name":"Melilla","country":"ES","coord":{"lon":-2.94434,"lat":35.292149}}
{"_id":6359078,"name":"Logroño","country":"ES","coord":{"lon":-2.44541,"lat":42.467121}}
 */