package dev.hnnguyen.blog.web.rest;

import dev.hnnguyen.blog.domain.dto.file.ResponseFileUrl;
import dev.hnnguyen.blog.security.AuthoritiesConstants;
import dev.hnnguyen.blog.service.FileService;
import dev.hnnguyen.blog.validation.constraint.FileMaxSizeCheck;
import dev.hnnguyen.blog.validation.constraint.FileRequireCheck;
import dev.hnnguyen.blog.validation.constraint.FileTypeCheck;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static dev.hnnguyen.blog.domain.enums.FileTypeEnum.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/image", consumes = {"multipart/form-data"})
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ResponseFileUrl> uploadImage(@RequestPart(value = "file") @Valid @FileRequireCheck @FileMaxSizeCheck(max = 5)
                                                       @FileTypeCheck(allowedFileTypes = {JPG, JPEG, PNG, HEIF, HEIC, GIF, SVG, BMP, WEBP, TIF, TIFF, ICO})
                                                       MultipartFile file
    ) {
        String url = fileService.uploadPostImage(file);
        return new ResponseEntity<>(new ResponseFileUrl(url), HttpStatus.OK);
    }

    @PostMapping(value = "/file", consumes = {"multipart/form-data"})
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ResponseFileUrl> uploadFile(@RequestPart(value = "file") @Valid @FileRequireCheck @FileMaxSizeCheck(max = 25) MultipartFile file
    ) {
        String url = fileService.uploadPostFile(file);
        return new ResponseEntity<>(new ResponseFileUrl(url), HttpStatus.OK);
    }
}
