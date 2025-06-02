package dev.hnnguyen.blog.validation.constraint;

import dev.hnnguyen.blog.validation.FileMaxSizeCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER, METHOD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = FileMaxSizeCheckValidator.class)
public @interface FileMaxSizeCheck {

    String message() default "{validation.fileMaxSizeCheck}";

    long max() default 0;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
