package dev.hnnguyen.blog.web.rest;

import dev.hnnguyen.blog.domain.Tag;
import dev.hnnguyen.blog.domain.dto.tag.RequestTag;
import dev.hnnguyen.blog.domain.dto.tag.ResponseTag;
import dev.hnnguyen.blog.security.AuthoritiesConstants;
import dev.hnnguyen.blog.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    @PostMapping()
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Tag> createTag(@Valid @RequestBody RequestTag requestTag) throws URISyntaxException {
        log.debug("REST request to save Tag : {}", requestTag);
        Tag tag = tagService.createTag(requestTag);
        return ResponseEntity.created(new URI("/api/tags/" + requestTag.getId()))
            .body(tag);
    }

    @GetMapping("/options")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public List<ResponseTag> getTagOptions() {
        log.debug("REST request to get all Tag Options");
        return tagService.getTagOptions();
    }
}
