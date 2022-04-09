package ar.edu.itba.paw.model;

import java.util.Arrays;

public enum Location {
    ADROGUE("Adrogué"),
    SANISIDRO("San Isidro"),
    RECOLETA("Recoleta"),
    BELGRANO("Belgrano"),
    TURDERA("Turdera"),
    TORCUATO("Don Torcuato");

    private final String name;

    Location(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public static String[] getNames() {
        Location[] locations = values();
        String[] names = new String[locations.length];

        for (int i = 0; i < locations.length; i++){
            names[i] = locations[i].getName();
        }

        return names;
    }

}
