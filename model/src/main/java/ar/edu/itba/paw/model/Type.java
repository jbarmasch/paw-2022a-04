package ar.edu.itba.paw.model;

import javax.persistence.*;

@Entity
@Table(name = "types")
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "types_typeid_seq")
    @SequenceGenerator(sequenceName = "types_typeid_seq", name = "types_typeid_seq", allocationSize = 1)
    @Column(name = "typeid")
    private long id;
    private String name;

    public Type(String name) {
        this.name = name;
    }

    public Type() {}

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
