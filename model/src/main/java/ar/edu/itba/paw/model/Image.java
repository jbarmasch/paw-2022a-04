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

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getFormatted() {
        byte[] encodeBase64 = Base64.getEncoder().encode(image);
        return new String(encodeBase64, StandardCharsets.UTF_8);
    }
}
