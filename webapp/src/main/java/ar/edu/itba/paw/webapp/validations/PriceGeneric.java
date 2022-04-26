package ar.edu.itba.paw.webapp.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
@Constraint(validatedBy = { PriceGenericValidator.class })
public @interface PriceGeneric {
    String message() default "{Price.filterForm}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
