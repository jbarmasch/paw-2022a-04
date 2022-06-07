package ar.edu.itba.paw.webapp.validations;

import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = DateOrderValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface DateOrder {
    String message() default "{Before.rateForm.date}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
