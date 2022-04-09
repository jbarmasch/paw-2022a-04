package ar.edu.itba.paw.model;

import java.util.stream.Stream;

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
        return Stream.of(Location.values()).map(Location::getName).toArray(String[]::new);
    }
}
