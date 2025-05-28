package dev.hnnguyen.blog.service;

import dev.hnnguyen.blog.domain.*;
import dev.hnnguyen.blog.domain.dto.post.*;
import dev.hnnguyen.blog.mapper.PostMapper;
import dev.hnnguyen.blog.repository.*;
import dev.hnnguyen.blog.security.SecurityUtils;
import dev.hnnguyen.blog.util.PageUtils;
import dev.hnnguyen.blog.util.SlugUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final SectionRepository sectionRepository;
    private final JsonElementService jsonElementService;
    private final SlugService slugService;

    @Transactional(readOnly = true)
    public Page<ResponseBlogPost> getAllBlogPosts(Pageable pageable) {
        return postRepository.getAllBlogPosts(pageable);
    }

    @Transactional(readOnly = true)
    public ResponseBlogPostDetails getBlogPostDetailsBySlug(String slug) {
        return postRepository.findDetailedPostBySlug(slug)
            .map(PostMapper::toResponseBlogPostDetails)
            .orElse(null); // Or throw new NotFoundException(...)
    }

    @Transactional(readOnly = true)
    public Page<ResponsePost> getAllPostByFilter(RequestSearchPost request) {
        String sort = !ObjectUtils.isEmpty(request.getSort()) ? request.getSort() : "p.last_modified_date,desc";
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), PageUtils.createSort(sort));
        HashMap<String, String> sortMap = new HashMap<>();
        sortMap.put("title", "p.title");
        sortMap.put("heroImage", "p.hero_image");
        sortMap.put("category", "c.name");
        sortMap.put("authorName", "u.full_name");
        sortMap.put("lastUpdated", "p.last_modified_date");
        request.setSearchConditions(dev.hnnguyen.blog.util.ObjectUtils.convertSortParams(request.getSearchConditions(), sortMap));
        return postRepository.getAllPostByFilterCondition(request, pageable);
    }

    @Transactional(readOnly = true)
    public ResponsePostDetails getPostDetailsById(UUID id) {
        return postRepository.findDetailedPostById(id)
            .map(PostMapper::toResponsePostDetails)
            .orElse(null); // Or throw new NotFoundException(...)
    }

    @Transactional
    public void deletePosts(List<UUID> ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            return;
        }
        List<Post> posts = postRepository.findAllById(ids);
        postRepository.deleteAll(posts);
    }

    public Post createPost(RequestPost request) {
        User user = getCurrentUser();

        Category category = getCategory(request.getCategory());
        Set<Tag> tags = getTags(request.getTags());

        Post post = Post.builder()
            .title(request.getTitle())
            .status(request.getStatus())
            .description(request.getDescription())
            .category(category)
            .tags(tags)
            .heroImage(request.getHeroImage())
            .sections(new HashSet<>())
            .publishedDate(Instant.now())
            .author(user)
            .build();
        postRepository.save(post);

        saveDataLessonSection(post, request.getSections());

        return post;
    }

    public Post updatePost(UUID postId, RequestPost request) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException("Post not found: " + postId));

        // Update slug only if the title has changed
        String newTitle = request.getTitle();
        if (!Objects.equals(post.getTitle(), newTitle)) {
            String baseSlug = SlugUtils.toSlug(newTitle);
            String uniqueSlug = slugService.generateUniqueSlug(baseSlug, Post.class);
            post.setSlug(uniqueSlug);
        }

        post.setTitle(newTitle);
        post.setStatus(request.getStatus());
        post.setDescription(request.getDescription());
        post.setHeroImage(request.getHeroImage());

        post.setCategory(getCategory(request.getCategory()));
        post.setTags(getTags(request.getTags()));

        saveDataLessonSection(post, request.getSections());

        return post;
    }

    private void saveDataLessonSection(Post post, List<RequestSection> sections) {
        Set<Section> currentSections = post.getSections();
        if (ObjectUtils.isEmpty(sections)) {
            if (!currentSections.isEmpty()) {
                sectionRepository.deleteAll(currentSections);
            }
            return;
        }

        Map<UUID, Section> currentSectionMap = createCurrentSectionMap(currentSections);
        List<Section> sectionsToDelete = new ArrayList<>(currentSections);
        List<Section> updatedSections = new ArrayList<>();

        for (RequestSection requestSection : sections) {
            Section section = updateOrCreateSection(requestSection, currentSectionMap);

            if (Objects.nonNull(section.getId())) {
                sectionsToDelete.remove(section);
            }

            section.setPost(post);
            Section savedSection = sectionRepository.save(section);
            requestSection.setId(savedSection.getId());

            savedSection.setContent(jsonElementService.convertDtoToJsonElement(requestSection));
            updatedSections.add(savedSection);
        }

        sectionRepository.saveAll(updatedSections);

        if (!sectionsToDelete.isEmpty()) {
            sectionRepository.deleteAll(sectionsToDelete);
        }
    }


    private Map<UUID, Section> createCurrentSectionMap(Set<Section> currentSections) {
        if (ObjectUtils.isEmpty(currentSections)) {
            return new HashMap<>();
        }
        return currentSections.stream()
            .collect(Collectors.toMap(Section::getId, Function.identity()));
    }

    private Section updateOrCreateSection(RequestSection requestSection, Map<UUID, Section> currentSectionMap) {
        if (Objects.nonNull(requestSection.getId())) {
            Section section = currentSectionMap.get(requestSection.getId());
            if (Objects.nonNull(section)) {
                section.setId(requestSection.getId());
                section.setSectionType(requestSection.getSectionType());
                section.setPosition(requestSection.getPosition());
            }
            return section;
        }
        return Section.builder()
            .id(requestSection.getId())
            .sectionType(requestSection.getSectionType())
            .position(requestSection.getPosition())
            .build();
    }

    private Category getCategory(UUID categoryId) {
        return Optional.ofNullable(categoryId)
            .flatMap(categoryRepository::findById)
            .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
    }


    private Set<Tag> getTags(List<UUID> tagIds) {
        if (ObjectUtils.isEmpty(tagIds)) {
            return Collections.emptySet();
        }
        List<Tag> tags = tagRepository.findAllById(tagIds);
        if (tags.size() != tagIds.size()) {
            // TODO
        }
        return new HashSet<>(tags);
    }

    private User getCurrentUser() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin)
            .orElse(null);

    }

}
