package ar.edu.itba.paw.model;

import javax.persistence.*;

@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "locations_locationid_seq")
    @SequenceGenerator(sequenceName = "locations_locationid_seq", name = "locations_locationid_seq", allocationSize = 1)
    @Column(name = "locationid")
    private long id;
    private String name;

    public Location(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Location(String name) {
        this.name = name;
    }

    public Location() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }
}
