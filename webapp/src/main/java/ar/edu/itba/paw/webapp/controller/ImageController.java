package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.service.ImageService;
import ar.edu.itba.paw.webapp.exceptions.ImageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @RequestMapping(value = "/image/{id}", method = { RequestMethod.GET }, produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] getImage(@PathVariable("id") final int imageId) {
        return imageService.getImageById(imageId).orElseThrow(ImageNotFoundException::new).getImage();
    }
}
