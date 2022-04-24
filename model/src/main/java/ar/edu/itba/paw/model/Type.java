package ar.edu.itba.paw.model;

public class Type {
    private long id;
    private String name;

    public Type(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

   public String getName() {
        return name;
    }
}
