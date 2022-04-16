package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.persistence.ImageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageDao imageDao;

    @Autowired
    public ImageServiceImpl(final ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    @Override
    public Optional<Image> getImageById(long id) {
        return imageDao.getImageById(id);
    }

    public String getFormattedImage(byte[] imageArray) {
        byte[] encodeBase64 = Base64.getEncoder().encode(imageArray);
        return new String(encodeBase64, StandardCharsets.UTF_8);
    }

    @Override
    public void addEventImage(byte[] image) {
        imageDao.addEventImage(image);
    }
}
