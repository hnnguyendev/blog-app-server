package dev.hnnguyen.blog.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

public interface IStorageService {

    String uploadFile(String keyName, MultipartFile file, boolean isPublic);

    String uploadFile(String keyName, byte[] bytes, String contentType);

    String getPreSignedUrl(String keyName);

    ByteArrayOutputStream download(String keyName);
}
