package dev.hnnguyen.blog.domain.dto.file;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

public class ResponseFileUrl implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String url;

    public ResponseFileUrl(String url) {
        this.url = url;
    }

    @JsonProperty("url")
    private String getUrl() {
        return url;
    }

    private void setUrl(String url) {
        this.url = url;
    }
}
