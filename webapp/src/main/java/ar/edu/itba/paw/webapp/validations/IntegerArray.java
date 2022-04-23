package ar.edu.itba.paw.webapp.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = IntegerArrayValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface IntegerArray {
//    String message() default "{Future.eventForm.date}";
    String message() default "TIENE QUE SER INTEGER[]";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
