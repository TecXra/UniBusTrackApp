package Models;

/**
 * Created by Ali ( 03154342359 ) on 7/26/2016.
 */
public class UniBus {

    private String id;
    private String name;

    public  UniBus(String id,String name)
    {
        this.id=id;
        this.name=name;

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
