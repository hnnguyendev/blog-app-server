package dev.hnnguyen.blog.validation.constraint;

import dev.hnnguyen.blog.validation.FileRequireCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE_USE, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = FileRequireCheckValidator.class)
public @interface FileRequireCheck {

    String message() default "{validation.fileRequireCheck}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
