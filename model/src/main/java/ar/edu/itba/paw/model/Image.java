package ar.edu.itba.paw.model;

import javax.persistence.*;

@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "images_imageid_seq")
    @SequenceGenerator(sequenceName = "images_imageid_seq", name = "images_imageid_seq", allocationSize = 1)
    @Column(name = "imageid")
    private long id;
    private byte[] image;

    public Image(byte[] image) {
        this.image = image;
    }

    public Image() {}

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
