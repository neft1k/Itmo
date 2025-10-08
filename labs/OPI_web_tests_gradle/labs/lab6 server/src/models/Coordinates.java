package models;

import java.io.Serial;
import java.io.Serializable;

public class Coordinates implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int x;
    private long y;
    public Coordinates(int x, long y){
        this.x = x;
        this.y = y;
    }
    public String toString(){
        return x + ";" + y;
    }

    public long getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}