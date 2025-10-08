package models;

import models.Country;
import models.EyeColor;
import models.HairColor;
import utility.Validatable;

import java.io.Serial;
import java.io.Serializable;

public class Person implements Validatable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name; //Поле не может быть null, Строка не может быть пустой
    private String passportID; //Поле не может быть null
    private EyeColor eyeColor; //Поле не может быть null
    private HairColor hairColor; //Поле может быть null
    private Country nationality; //Поле может быть null
    private Location location; //Поле не может быть null
    public Person(String name, String passportID, EyeColor eyeColor, HairColor hairColor, Country nationality, Location location){
        this.name = name;
        this.passportID = passportID;
        this.hairColor = hairColor;
        this.eyeColor = eyeColor;
        this.nationality = nationality;
        this.location = location;
    }
    @Override
    public String toString(){
        return "name: " + name + "\n" +
                "passportID: " + passportID +  "\n" +
                "Цвет глаз: " + eyeColor + "\n" +
                "Цвет волос: "+ hairColor + "\n" +
                "Национальность: " + nationality + "\n" +
                 location + "\n";
    }

    public String getName() {
        return name;
    }

    public String getPassportID() {
        return passportID;
    }

    public EyeColor getEyeColor() {
        return eyeColor;
    }

    public HairColor getHairColor() {
        return hairColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public Boolean validate() {
        if (name == null || name.isEmpty()) return false;
        if (passportID == null ) return false;
        if (eyeColor == null ) return false;
        if (location == null ) return false;
        return true;
    }
}