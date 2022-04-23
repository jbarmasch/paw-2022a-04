package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;

import java.util.Optional;

public interface ImageDao {
    Optional<Image> getImageById(long id);
    int addEventImage(byte[] image);
    Optional<Image> getImgFromEventId(int eventId);
}
