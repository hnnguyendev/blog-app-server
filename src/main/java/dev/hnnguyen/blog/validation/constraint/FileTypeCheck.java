package dev.hnnguyen.blog.validation.constraint;

import dev.hnnguyen.blog.domain.enums.FileTypeEnum;
import dev.hnnguyen.blog.validation.FileTypeCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static dev.hnnguyen.blog.domain.enums.FileTypeEnum.*;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE_USE, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = FileTypeCheckValidator.class)
public @interface FileTypeCheck {
    FileTypeEnum[] allowedFileTypes() default {
        PDF, DOC, DOCX, XLS, XLSX, PPT, PPTX, PNG, JPEG, JPG, BMP, MP4, SRT, VTT, HEIF, HEIC, WEBP,
    };

    String message() default "{validation.fileTypeCheck}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
