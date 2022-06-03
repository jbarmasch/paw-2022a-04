package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.util.Locale;

@Entity
@Table(name = "types")
public class Type {
    private static Locale locale = Locale.ENGLISH;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "types_typeid_seq")
    @SequenceGenerator(sequenceName = "types_typeid_seq", name = "types_typeid_seq", allocationSize = 1)
    @Column(name = "typeid")
    private long id;
    private String name;
    private String name_en;

    public Type(String name, String name_en) {
        this.name = name;
        this.name_en = name_en;
    }

    public Type() {}

    public static void setLocale(Locale locale) {
        Type.locale = locale;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        if (locale != null && locale.getLanguage().equals("es"))
            return name;
        return name_en;
    }
}
