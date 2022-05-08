package ar.edu.itba.paw.webapp.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BookingListValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface BookingList {
    String message() default "REQUIRED";
    String messageNotNull() default "AT LEAST ONE TICKET IS REQUIRED";
    String messagePositive() default "MUST BE POSITIVE";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
