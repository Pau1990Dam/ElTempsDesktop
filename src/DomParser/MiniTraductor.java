package DomParser;

/**
 * Created by pau on 20/11/15.
 */
public class MiniTraductor {
    public String vent(String s){
        switch(s){
            case "East-northeast":
                return "Este-nordeste";
            case "NorthEast":
                return "Nordeste";
            case "West":
                return "Oeste";
            case "Northwest":
                return "NordOeste";
            case "South":
                return "Sud";
            case "West-southwest":
                return "Oeste-sudoeste";
            case "West-northwest":
                return "Oeste-nordoeste";
            case "Southwest":
                return "Sudoeste";
            case "South-southwest":
                return "Sud-sudoeste";
            case "East-southeast":
                return "Este-sudeste";
            case "North-northeast":
                return "Norte-nordeste";
            case "East":
                return "Este";
            case "North":
                return "Norte";
            case "SouthEast":
                return "Sud-este";
            case "North-nothwest":
                return "Norte-nordoeste";
            default:
                if(s=="")return "Sin datos";
                return s;
        }
    }

    public String nuvols(String s){
        switch(s){
            case "scattered clouds":
                return "nubes dispersas";
            case "broken clouds":
                return "nubes rotas";
            case "overcast clouds":
                return "nublado";
            case "few clouds":
                return "pocas nubes";
            case "clear sky":
                return "despejado";
            default:
                return s;
        }
    }
}
