package dev.hnnguyen.blog.service;

import dev.hnnguyen.blog.config.property.CloudProperties;
import dev.hnnguyen.blog.exception.AmazonClientException;
import dev.hnnguyen.blog.exception.AmazonDownloadException;
import dev.hnnguyen.blog.exception.AmazonServiceException;
import dev.hnnguyen.blog.exception.AmazonUploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.exception.SdkServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Objects;

@Primary
@Service
@Slf4j
@RequiredArgsConstructor
public class S3StorageService implements IStorageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final CloudProperties config;

    /**
     * Upload MultipartFile
     *
     * @param keyName  String
     * @param file     MultipartFile
     * @param isPublic boolean
     * @return String
     */
    public String uploadFile(String keyName, MultipartFile file, boolean isPublic) {
        try {
            return uploadToS3(keyName, file.getInputStream(), file.getSize(), file.getContentType(), isPublic);
        } catch (IOException e) {
            log.error("Error reading MultipartFile", e);
            throw new AmazonUploadException("Error while reading file for upload", e);
        }
    }

    /**
     * Upload byte[]
     *
     * @param keyName String
     * @param bytes   byte[]
     * @return String
     */
    public String uploadFile(String keyName, byte[] bytes, String contentType) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        return uploadToS3(keyName, inputStream, bytes.length, "application/pdf", true);
    }

    private String uploadToS3(String keyName, InputStream inputStream, long contentLength, String contentType, boolean isPublic) {
        String bucketName = config.getBucket().getName();
        try {
            uploadObjectToS3(bucketName, keyName, inputStream, contentLength, contentType, isPublic);
            return getUrlFromS3Client(keyName);
        } catch (SdkServiceException e) {
            log.error("AWS service error while uploading to bucket [{}] with key [{}]: {}", bucketName, keyName, e.getMessage(), e);
            throw new AmazonServiceException("AWS service error during upload: " + e.getMessage(), e);
        } catch (SdkClientException e) {
            log.error("AWS client error while uploading to bucket [{}] with key [{}]: {}", bucketName, keyName, e.getMessage(), e);
            throw new AmazonClientException("AWS client error during upload: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while uploading to S3 for bucket [{}] with key [{}]", bucketName, keyName, e);
            throw new RuntimeException("Unexpected error during S3 upload", e);
        }
    }

    /**
     * Upload object to S3
     *
     * @param bucketName    String
     * @param keyName       String
     * @param inputStream   InputStream
     * @param contentLength long
     * @param contentType   String
     * @param isPublic      boolean
     */
    private void uploadObjectToS3(String bucketName, String keyName, InputStream inputStream, long contentLength, String contentType, boolean isPublic) {
        if (Objects.isNull(inputStream) || contentLength <= 0) {
            throw new IllegalArgumentException("Input stream must not be null and content length must be greater than 0");
        }
        PutObjectRequest.Builder requestBuilder = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(keyName)
            .contentType(contentType);
        if (isPublic) {
            requestBuilder.acl(ObjectCannedACL.PUBLIC_READ);
        }

        PutObjectRequest putObjectRequest = requestBuilder.build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));
            log.info("Successfully uploaded object to S3. Bucket: {}, Key: {}", bucketName, keyName);
        } catch (SdkException e) {
            log.error("Failed to upload object to S3. Bucket: {}, Key: {}", bucketName, keyName, e);
            throw e; // Rethrow or wrap depending on how you want to handle it upstream
        }
    }

    public String getUrlFromS3Client(String keyName) {
        final URL reportUrl = s3Client
            .utilities()
            .getUrl(GetUrlRequest.builder().bucket(config.getBucket().getName()).key(keyName).build());
        return reportUrl.toString();
    }

    /**
     * Generate url download file
     *
     * @param keyName String
     * @return String
     */
    public String getPreSignedUrl(String keyName) {
        String bucketName = config.getBucket().getName();
        long duration = config.getPreSigned().getUrlExpiration();

        boolean fileExists = checkKeyExists(keyName);
        if (!fileExists) {
            log.error("File not found");
//            throw new NotFoundException(MessageHelper.getMessage(Message.Keys.E0010, "File"));
        }

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofSeconds(duration))
            .getObjectRequest(request -> request
                .bucket(bucketName)
                .key(keyName))
            .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
        return presignedGetObjectRequest.url().toString();
    }

    private boolean checkKeyExists(String keyName) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(config.getBucket().getName())
                .key(keyName)
                .build();
            s3Client.headObject(headObjectRequest);
            return true;
        } catch (S3Exception e) {
            log.error(e.awsErrorDetails().errorMessage());
            return false;
        }
    }

    @Override
    public ByteArrayOutputStream download(String keyName) {
        try {
            byte[] bytes = getObject(keyName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
            baos.writeBytes(bytes);
            return baos;
        } catch (AmazonServiceException e) {
            log.error(e.toString());
            throw new AmazonDownloadException("Error while downloading object from Amazon service", e);
        } catch (AmazonClientException e) {
            log.error(e.toString());
            throw new AmazonDownloadException("Error while downloading object from Amazon client", e);
        }
    }

    public byte[] getObject(String keyName) {
        try {
            log.info("Retrieving file from S3 for key: {}/{}", config.getBucket().getName(), keyName);
            ResponseBytes<GetObjectResponse> s3Object = s3Client.getObject(
                GetObjectRequest.builder().bucket(config.getBucket().getName()).key(keyName).build(),
                ResponseTransformer.toBytes()
            );
            return s3Object.asByteArray();
        } catch (SdkClientException e) {
            log.error(e.toString());
            throw new AmazonClientException(e.getLocalizedMessage(), e);
        } catch (SdkServiceException e) {
            log.error(e.toString());
            throw new AmazonServiceException(e.getLocalizedMessage(), e);
        }
    }
}
