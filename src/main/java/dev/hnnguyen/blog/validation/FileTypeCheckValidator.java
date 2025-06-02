package dev.hnnguyen.blog.validation;

import dev.hnnguyen.blog.util.FileUtils;
import dev.hnnguyen.blog.validation.constraint.FileTypeCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTypeCheckValidator implements ConstraintValidator<FileTypeCheck, MultipartFile> {

    private List<String> validFileTypes;

    @Override
    public void initialize(FileTypeCheck fileType) {
        validFileTypes = new ArrayList<>();
        Arrays.stream(fileType.allowedFileTypes()).forEach(type -> validFileTypes.add(type.getValue()));
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        String extension = FileUtils.getExtensionName(value.getOriginalFilename()).toUpperCase();
        return validFileTypes.contains(extension);
    }
}
