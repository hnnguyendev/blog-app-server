package dev.hnnguyen.blog.config;

import dev.hnnguyen.blog.config.property.CloudProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsConfiguration {

    @Bean(destroyMethod = "close")
    public S3Client s3Client(CloudProperties config) {
        return S3Client
            .builder()
            .region(Region.of(config.getRegion()))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(config.getCredentials().getAccessKey(), config.getCredentials().getSecretKey())
                )
            )
            .build();
    }

    @Bean(destroyMethod = "close")
    public S3Presigner s3Presigner(CloudProperties config) {
        return S3Presigner
            .builder()
            .region(Region.of(config.getRegion()))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(config.getCredentials().getAccessKey(), config.getCredentials().getSecretKey())
                )
            )
            .build();
    }
}
