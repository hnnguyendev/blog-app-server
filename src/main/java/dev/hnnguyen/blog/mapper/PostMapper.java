package dev.hnnguyen.blog.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.hnnguyen.blog.domain.Post;
import dev.hnnguyen.blog.domain.Section;
import dev.hnnguyen.blog.domain.Tag;
import dev.hnnguyen.blog.domain.dto.category.ResponseCategory;
import dev.hnnguyen.blog.domain.dto.post.ResponseBlogPostDetails;
import dev.hnnguyen.blog.domain.dto.post.ResponsePostDetails;
import dev.hnnguyen.blog.domain.dto.post.ResponseSection;
import dev.hnnguyen.blog.domain.dto.tag.ResponseTag;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostMapper {

    private PostMapper() {
        // static utility class
    }

    public static ResponseBlogPostDetails toResponseBlogPostDetails(Post post) {
        ResponseBlogPostDetails postDetails = new ResponseBlogPostDetails(
            post.getId(),
            post.getTitle(),
            post.getStatus(),
            post.getDescription(),
            post.getHeroImage(),
            Objects.nonNull(post.getAuthor()) ? post.getAuthor().getFullName() : null,
            Objects.nonNull(post.getAuthor()) ? post.getAuthor().getImageUrl() : null,
            post.getLastModifiedDate()
        );

        ResponseCategory category = Optional.ofNullable(post.getCategory())
            .map(ResponseCategory::new)
            .orElse(null);

        List<ResponseTag> tags = Objects.nonNull(post.getTags())
            ? post.getTags().stream().map(ResponseTag::new).toList()
            : Collections.emptyList();

        List<ResponseSection> sections = Objects.nonNull(post.getSections())
            ? post.getSections().stream()
            .sorted(Comparator.comparing(Section::getPosition))
            .map(section -> convertJsonElementToDTO(section.getContent(), ResponseSection.class))
            .toList()
            : Collections.emptyList();

        postDetails.setCategory(category);
        postDetails.setTags(tags);
        postDetails.setSections(sections);

        return postDetails;
    }

    public static ResponsePostDetails toResponsePostDetails(Post post) {
        if (Objects.isNull(post)) {
            return null;
        }

        ResponsePostDetails postDetails = new ResponsePostDetails(
            post.getId(),
            post.getTitle(),
            post.getStatus(),
            post.getDescription(),
            post.getCategory().getId(),
            post.getHeroImage(),
            Objects.nonNull(post.getAuthor()) ? post.getAuthor().getFullName() : null,
            Objects.nonNull(post.getAuthor()) ? post.getAuthor().getImageUrl() : null,
            post.getLastModifiedDate()
        );

        List<UUID> tagIds = post.getTags() != null
            ? post.getTags().stream().map(Tag::getId).toList()
            : Collections.emptyList();

        List<ResponseSection> sections = post.getSections() != null
            ? post.getSections().stream()
            .sorted(Comparator.comparing(Section::getPosition))
            .map(section -> convertJsonElementToDTO(section.getContent(), ResponseSection.class))
            .toList()
            : Collections.emptyList();

        postDetails.setTags(tagIds);
        postDetails.setSections(sections);

        return postDetails;
    }

    public static <T> T convertJsonElementToDTO(JsonElement jsonElement, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(jsonElement, clazz);
    }
}
