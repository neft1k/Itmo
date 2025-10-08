package models;

import utility.Validatable;

import java.io.Serial;
import java.io.Serializable;

public class Location implements Validatable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int x;
    private int y;
    private String name; //Поле может быть null
    public Location(String name, int x, int y){
        this.name = name;
        this.x = x;
        this.y = y;
    }
    public String toString(){
        return "Location: " + name + "(" + x + ";" + y + ")";
    }

    @Override
    public Boolean validate() {
        if (name == null) return false;
        return true;
    }
}