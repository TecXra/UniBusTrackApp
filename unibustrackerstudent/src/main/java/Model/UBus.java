package Model;

import java.util.ArrayList;

/**
 * Created by Ali ( 03154342359 ) on 7/27/2016.
 */
public class UBus
{

String Id , Name , CurrentLat , CurrentLong ;

    public ArrayList<UStop> StopList = new ArrayList<UStop>();

    public UBus(String id, String name, String currentLat, String currentLong,ArrayList<UStop> stops) {
        Id = id;
        Name = name;
        CurrentLat = currentLat;
        CurrentLong = currentLong;
        StopList = stops;
    }

    public UBus() {
    }

    public UBus(String currentLat, String currentLong) {
        CurrentLat = currentLat;
        CurrentLong = currentLong;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCurrentLat() {
        return CurrentLat;
    }

    public void setCurrentLat(String currentLat) {
        CurrentLat = currentLat;
    }

    public String getCurrentLong() {
        return CurrentLong;
    }

    public void setCurrentLong(String currentLong) {
        CurrentLong = currentLong;
    }
}


