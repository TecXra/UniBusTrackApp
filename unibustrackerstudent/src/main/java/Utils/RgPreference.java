package Utils;

/**
 * Created by Ali on 5/25/2016.
 */
public class RgPreference {


    public static final String Bus_Id ="";



    public static final String host = "https://univbustrack.azurewebsites.net";

    public static final String busListUrl = "/UnivBusBrows/GetBusesList";

    public static final String busLatLng="/api/UnivBus/{id}";


    public static final String GoogleApiUrlDrive="http://maps.googleapis.com/maps/api/distancematrix/json?origins={OLat},{OLng}&destinations={DLat},{DLng}&mode=driving&language=en-EN&sensor=false";


    public static final String GoogleApiUrlWalk="http://maps.googleapis.com/maps/api/distancematrix/json?origins={OLat},{OLng}&destinations={DLat},{DLng}&mode=walking&language=en-EN&sensor=false";


}
