package org.example.enums;

public enum City {
    LONDON("London"),
    LAS_VEGAS("Las Vegas");

    private String name;

    City(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
