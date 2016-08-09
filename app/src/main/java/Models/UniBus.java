package Models;

/**
 * Created by Ali ( 03154342359 ) on 7/26/2016.
 */
public class UniBus {

    private String id;
    private String name;
    private String CurrentLat;
    private String CurrentLong;

    public  UniBus(String id,String name)
    {
        this.id=id;
        this.name=name;

    }

    public UniBus(String id, String name, String currentLat, String currentLong) {
        this.id = id;
        this.name = name;
        CurrentLat = currentLat;
        CurrentLong = currentLong;
    }

    public String getCurrentLong() {
        return CurrentLong;
    }

    public void setCurrentLong(String currentLong) {
        CurrentLong = currentLong;
    }

    public String getCurrentLat() {
        return CurrentLat;
    }

    public void setCurrentLat(String currentLat) {
        CurrentLat = currentLat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
