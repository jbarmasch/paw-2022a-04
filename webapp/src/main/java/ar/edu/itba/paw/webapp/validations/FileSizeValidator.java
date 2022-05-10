package ar.edu.itba.paw.webapp.validations;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {
    private long maxSize;

    @Override
    public void initialize(FileSize fileSize) {
        maxSize = fileSize.maxSize();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        System.out.println("????");
        if (file == null || file.isEmpty())
            return true;

//        try {
        try {
            System.out.println("hola" + file.getSize() + "chau" + file.getBytes().length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(file.getSize());
            return file.getSize() <= maxSize;
//        } catch (DateTimeParseException e) {
//            return true;
//        }
    }
}