package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.service.ImageService;
import ar.edu.itba.paw.webapp.exceptions.ImageNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.Min;

//@Controller
//public class ImageController {
//    @Autowired
//    private ImageService imageService;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);
//
//    @RequestMapping(value = "/image/{id}", method = { RequestMethod.GET }, produces = MediaType.IMAGE_PNG_VALUE)
//    @ResponseBody
//    public byte[] getImage(@PathVariable("id") @Min(1) final int imageId) {
//        Image image = imageService.getImageById(imageId).orElse(null);
//        if (image == null) {
//            LOGGER.error("Image not found");
//            throw new ImageNotFoundException();
//        }
//
//        return image.getImage();
//    }
//}
