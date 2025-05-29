package dev.hnnguyen.blog.repository;

import dev.hnnguyen.blog.domain.Post;
import dev.hnnguyen.blog.domain.dto.post.ResponseBlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the {@link Post} entity.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, UUID>, PostRepositoryCustom {

    boolean existsBySlug(String slug);

    @Query("""
            SELECT new dev.hnnguyen.blog.domain.dto.post.ResponseBlogPost(
                p.id, p.title, p.description, p.heroImage, a.fullName, a.imageUrl, p.publishedDate, p.slug
            )
            FROM Post p
            JOIN p.author a
            WHERE p.status = "PUBLISHED"
        """)
    Page<ResponseBlogPost> getAllBlogPosts(Pageable pageable);

    @Query("""
            SELECT p FROM Post p
            LEFT JOIN FETCH p.author
            LEFT JOIN FETCH p.category
            LEFT JOIN FETCH p.tags
            LEFT JOIN FETCH p.sections
            WHERE p.status = 'PUBLISHED' AND p.slug = :slug
        """)
    Optional<Post> findDetailedPostBySlug(String slug);

    @Query("""
            SELECT p FROM Post p
            LEFT JOIN FETCH p.tags
            LEFT JOIN FETCH p.sections
            WHERE p.id = :id
        """)
    Optional<Post> findDetailedPostById(UUID id);
}
