package dev.hnnguyen.blog.validation;

import dev.hnnguyen.blog.validation.constraint.FileRequireCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileRequireCheckValidator implements ConstraintValidator<FileRequireCheck, MultipartFile> {

    @Override
    public void initialize(final FileRequireCheck annotation) {
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return !value.isEmpty();
    }
}
