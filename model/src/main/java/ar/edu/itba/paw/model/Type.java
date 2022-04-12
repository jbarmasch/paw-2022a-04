package ar.edu.itba.paw.model;

import java.util.stream.Stream;

public enum Type {
    AFTER("After"),
    FIESTA( "Fiesta"),
    PREVIA( "Previa");

    private final String name;

    Type(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public static String[] getNames() {
        return Stream.of(Type.values()).map(Type::getName).toArray(String[]::new);
    }
}
