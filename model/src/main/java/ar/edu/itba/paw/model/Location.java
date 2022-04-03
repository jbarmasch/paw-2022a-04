package ar.edu.itba.paw.model;

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
}
