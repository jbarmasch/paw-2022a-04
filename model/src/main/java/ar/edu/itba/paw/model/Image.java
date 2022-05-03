package ar.edu.itba.paw.model;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Image {
    private long id;
    private byte[] image;

    public Image(long id, byte[] image) {
        this.id = id;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public byte[] getImage() {
        return image;
    }
}
