package dev.hnnguyen.blog.exception;

public class AmazonServiceException extends RuntimeException {

    public AmazonServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
