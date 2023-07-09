package ar.edu.itba.paw.webapp.helper;

import ar.edu.itba.paw.webapp.exceptions.ImageProcessingErrorException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public final class ImageUtils {
    private ImageUtils() {
        throw new UnsupportedOperationException();
    }

    public static byte[] getByteArray(InputStream inputStream) {
        byte[] image;
        try {
            image = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new ImageProcessingErrorException();
        }
        return image;
    }
}
