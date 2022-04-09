package ar.edu.itba.paw.model;

import java.util.stream.Stream;

public enum Type {
    FIESTA("Fiesta"),
    BEFORE("Before"),
    AFTER("After");

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
