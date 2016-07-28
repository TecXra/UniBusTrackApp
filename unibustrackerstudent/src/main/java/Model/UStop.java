package Model;

import java.io.Serializable;

/**
 * Created by Ali ( 03154342359 ) on 7/27/2016.
 */
public class UStop implements Serializable {

    String Id ,Name ,StopLat,StopLong;

    public UStop(String id, String name, String stopLat, String stopLong) {
        Id = id;
        Name = name;
        StopLat = stopLat;
        StopLong = stopLong;
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

    public String getStopLat() {
        return StopLat;
    }

    public void setStopLat(String stopLat) {
        StopLat = stopLat;
    }

    public String getStopLong() {
        return StopLong;
    }

    public void setStopLong(String stopLong) {
        StopLong = stopLong;
    }
}



