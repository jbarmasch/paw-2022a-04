package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.util.Locale;

@Entity
@Table(name = "tags")
public class Tag {
    @Transient
    private static Locale locale = Locale.ENGLISH;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tags_tagid_seq")
    @SequenceGenerator(sequenceName = "tags_tagid_seq", name = "tags_tagid_seq", allocationSize = 1)
    @Column(name = "tagid")
    private long id;
    private String name;
    private String name_en;

    public Tag(String name, String name_en) {
        this.name = name;
        this.name_en = name_en;
    }

    public Tag(long id, String name, String name_en) {
        this.id = id;
        this.name = name;
        this.name_en = name_en;
    }

    public Tag() {}

    public static void setLocale(Locale locale) {
        Tag.locale = locale;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        if (locale != null && locale.getLanguage().equals("es"))
            return name;
        return name_en;
    }

    public String getName(String locale) {
        if (locale != null && locale.equals("es"))
            return name;
        return name_en;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }
}
