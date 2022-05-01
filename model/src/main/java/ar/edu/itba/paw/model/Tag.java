package ar.edu.itba.paw.model;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tag {
    private long id;
    private String name;

    public Tag(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    static public List<Tag> getTags(Array ids, Array names) throws SQLException {
        List<Tag> tags = new ArrayList<>();
        Integer[] idsAux = (Integer[]) ids.getArray();
        String[] namesAux = (String[]) names.getArray();
        if (idsAux[0] != null && namesAux[0] != null) {
            for (int i = 0; i < idsAux.length; i++) {
                tags.add(new Tag(idsAux[i], namesAux[i]));
            }
        }
        return tags;
    }
}
