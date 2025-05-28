package dev.hnnguyen.blog.repository.impl;

import dev.hnnguyen.blog.domain.dto.post.RequestSearchPost;
import dev.hnnguyen.blog.domain.dto.post.ResponsePost;
import dev.hnnguyen.blog.repository.PostRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<ResponsePost> getAllPostByFilterCondition(RequestSearchPost request, Pageable pageable) {
        StringBuffer sql = new StringBuffer();
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(DISTINCT p.id) ");
        StringBuilder sqlSelect = new StringBuilder("""
            SELECT DISTINCT p.id AS id, p.title AS title, p.hero_image AS heroImage,
            c.name AS category, u.full_name AS authorName,
            p.last_modified_date AS lastUpdated, p.status AS status
            """);
        sql.append("""
            FROM post p
            JOIN category c ON p.category_id = c.id
            JOIN jhi_user u ON p.author_id = u.id
            WHERE p.deleted IS false
            """);

        if (Objects.nonNull(request.getSearchKeyword())) {
            sql.append("""
                AND (p.title ILIKE concat('%', :searchKeyword, '%')
                OR c.name ILIKE concat('%', :searchKeyword, '%')
                OR u.full_name ILIKE concat('%', :searchKeyword, '%'))
                """);
        }
        if (!ObjectUtils.isEmpty(request.getSearchConditions())) {
            sql.append(" AND ");
            var sqlCondition = SqlQueryBuilder.generateSqlQuery(request.getSearchConditions());
            sql.append(sqlCondition);
        }
        sqlCount.append(sql);
        sqlSelect.append(sql);
        sqlSelect.append(" ORDER BY ").append(pageable.getSort().toString().replace(":", ""));

        Query querySelect = em.createNativeQuery(sqlSelect.toString(), "search_post");
        Query queryCount = em.createNativeQuery(sqlCount.toString());

        if (Objects.nonNull(request.getSearchKeyword())) {
            queryCount.setParameter("searchKeyword", request.getSearchKeyword());
            querySelect.setParameter("searchKeyword", request.getSearchKeyword());
        }

        querySelect.setFirstResult((pageable.getPageNumber()) * pageable.getPageSize());
        querySelect.setMaxResults(pageable.getPageSize());
        long total = (Long) queryCount.getSingleResult();
        querySelect.setFirstResult((pageable.getPageNumber()) * pageable.getPageSize());
        querySelect.setMaxResults(pageable.getPageSize());
        var results = querySelect.getResultList();
        return new PageImpl<>(results, pageable, total);
    }
}
