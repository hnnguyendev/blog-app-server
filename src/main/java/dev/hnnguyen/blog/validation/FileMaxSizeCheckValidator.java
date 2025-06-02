package dev.hnnguyen.blog.validation;

import dev.hnnguyen.blog.util.FileUtils;
import dev.hnnguyen.blog.validation.constraint.FileMaxSizeCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileMaxSizeCheckValidator implements ConstraintValidator<FileMaxSizeCheck, MultipartFile> {

    private long max;

    @Override
    public void initialize(FileMaxSizeCheck annotation) {
        if (annotation.max() > 0) {
            this.max = annotation.max();
            return;
        }
        max = 10;
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return !FileUtils.checkSize(max, file.getSize());
    }
}
