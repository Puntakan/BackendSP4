package sit.int221.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = OnCreateUniqueValidator.class)
public @interface ValidUniqueOnCreate {
    String message() default "does not unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
