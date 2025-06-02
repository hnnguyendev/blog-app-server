package dev.hnnguyen.blog.exception;

public class AmazonClientException extends RuntimeException {

    public AmazonClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
