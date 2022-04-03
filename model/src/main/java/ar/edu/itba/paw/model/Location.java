package ar.edu.itba.paw.model;

public enum Location {
    ADRO("Adrogué"),
    SANI("San Ishidro Gordo"),
    RECO("Recoletah"),
    BELGRA("Belgranoh"),
    TURDE("Turde"),
    TORCU("Don Torcuato");

    private final String name;

    private Location(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
