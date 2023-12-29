package sit.int221.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = CategoryIdValidator.class)
public @interface ValidCategoryId {
    String message() default "does not exists";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}
