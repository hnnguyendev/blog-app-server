package dev.hnnguyen.blog.web.rest;

import dev.hnnguyen.blog.domain.Post;
import dev.hnnguyen.blog.domain.dto.post.*;
import dev.hnnguyen.blog.security.AuthoritiesConstants;
import dev.hnnguyen.blog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/public")
    public ResponseEntity<List<ResponseBlogPost>> getAllBlogPosts(@SortDefault(sort = "p.publishedDate", direction = Sort.Direction.DESC) Pageable pageable) {
        log.debug("REST request to get all Post for blog page");
        final Page<ResponseBlogPost> page = postService.getAllBlogPosts(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/public/{slug}")
    public ResponseEntity<ResponseBlogPostDetails> getBlogPostDetails(@PathVariable("slug") String slug) {
        log.debug("REST request to get Post : {}", slug);
        return ResponseUtil.wrapOrNotFound(java.util.Optional.ofNullable(postService.getBlogPostDetailsBySlug(slug)));
    }

    @PostMapping()
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Post> createPost(@Valid @RequestBody RequestPost requestPost) throws URISyntaxException {
        log.debug("REST request to save Post : {}", requestPost);
        Post newPost = postService.createPost(requestPost);
        return ResponseEntity.created(new URI("/api/posts/" + newPost.getId()))
            .body(newPost);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> updatePost(@PathVariable("id") String id, @Valid @RequestBody RequestPost request) {
        log.debug("REST request to update Post : {}", request);
        postService.updatePost(UUID.fromString(id), request);
        return ResponseEntity.noContent().build(); // return 204
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ResponsePostDetails> getPostDetails(@PathVariable("id") String id) {
        log.debug("REST request to get Post : {}", id);
        return ResponseUtil.wrapOrNotFound(java.util.Optional.ofNullable(postService.getPostDetailsById(UUID.fromString(id))));
    }

    @PostMapping("/search")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<ResponsePost>> getAllPostByFilter(@Valid @RequestBody RequestSearchPost request) {
        log.debug("REST request to get all Post for Admin");
        final Page<ResponsePost> page = postService.getAllPostByFilter(request);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@RequestBody RequestDeletePost request) {
        log.debug("REST request to delete Post");
        postService.deletePosts(request.getIds());
        return ResponseEntity.noContent().build();
    }
}
