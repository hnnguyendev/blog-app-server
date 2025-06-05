package dev.hnnguyen.blog.service;

import dev.hnnguyen.blog.util.SlugUtils;
import dev.hnnguyen.blog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {

    private final IStorageService storageService;

    public String uploadPostImage(MultipartFile multipartFile) {
        return uploadFile(multipartFile, "PostImages", true);
    }

    public String uploadAvatar(MultipartFile multipartFile) {
        return uploadFile(multipartFile, "Avatars", true);
    }

    public String uploadPostFile(MultipartFile multipartFile) {
        return uploadFile(multipartFile, "PostFiles", true);
    }

    public String uploadFile(MultipartFile multipartFile, String object, boolean isPublic) {
        String fileName = SlugUtils.slugifyFilename(multipartFile.getOriginalFilename());
        String id = UUID.randomUUID().toString();
        String keyName = String.format("%s/%s/%s", object, id, fileName);
        String url = storageService.uploadFile(keyName, multipartFile, isPublic);

        if (StringUtils.isBlank(url)) {
//            throw new BadRequestException(MessageHelper.getMessage(Message.Keys.E0010, "Failed to upload " + fileType.toLowerCase()));
        }
        return url;
    }
}
