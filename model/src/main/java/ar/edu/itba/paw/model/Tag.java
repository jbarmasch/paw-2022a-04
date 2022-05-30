package ar.edu.itba.paw.model;

import javax.persistence.*;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tags_tagid_seq")
    @SequenceGenerator(sequenceName = "tags_tagid_seq", name = "tags_tagid_seq", allocationSize = 1)
    @Column(name = "tagid")
    private long id;
    private String name;

    public Tag(String name) {
        this.name = name;
    }

    public Tag() {}

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
