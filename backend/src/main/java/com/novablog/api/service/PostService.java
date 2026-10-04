package com.novablog.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novablog.api.common.BusinessException;
import com.novablog.api.common.PageResponse;
import com.novablog.api.dto.PostRequest;
import com.novablog.api.model.Post;
import com.novablog.api.model.PostStatus;
import com.novablog.api.repository.PostMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
public class PostService {
    private final PostMapper postMapper;

    public PostService(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    public PageResponse<Post> publicList(long page, long size, String keyword, Long categoryId) {
        Page<Post> pager = new Page<>(Math.max(page, 1), Math.min(Math.max(size, 1), 50));
        QueryWrapper<Post> wrapper = Wrappers.query();
        wrapper.eq("status", PostStatus.PUBLISHED.name());
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like("title", keyword).or().like("summary", keyword));
        }
        if (categoryId != null) {
            wrapper.eq("category_id", categoryId);
        }
        wrapper.orderByDesc("featured").orderByDesc("published_at");
        return PageResponse.from(postMapper.selectPage(pager, wrapper));
    }

    public PageResponse<Post> adminList(long page, long size, String status, String keyword) {
        Page<Post> pager = new Page<>(Math.max(page, 1), Math.min(Math.max(size, 1), 100));
        QueryWrapper<Post> wrapper = Wrappers.query();
        if (status != null && !status.isBlank()) wrapper.eq("status", status.toUpperCase(Locale.ROOT));
        if (keyword != null && !keyword.isBlank()) wrapper.and(w -> w.like("title", keyword).or().like("summary", keyword));
        wrapper.orderByDesc("updated_at");
        return PageResponse.from(postMapper.selectPage(pager, wrapper));
    }

    public Post getPublicBySlug(String slug) {
        Post post = postMapper.selectOne(Wrappers.<Post>query().eq("slug", slug).eq("status", PostStatus.PUBLISHED.name()));
        if (post == null) throw new BusinessException(404, "文章不存在");
        postMapper.update(null, Wrappers.<Post>update().eq("id", post.getId()).setSql("views_count = views_count + 1"));
        post.setViewsCount((post.getViewsCount() == null ? 0 : post.getViewsCount()) + 1);
        return post;
    }

    public Post getById(Long id) {
        Post post = postMapper.selectById(id);
        if (post == null) throw new BusinessException(404, "文章不存在");
        return post;
    }

    @Transactional
    public Post create(PostRequest request, Long authorId) {
        Post post = new Post();
        apply(post, request);
        post.setAuthorId(authorId);
        post.setViewsCount(0L);
        post.setLikesCount(0L);
        post.setVersion(1);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        postMapper.insert(post);
        return post;
    }

    @Transactional
    public Post update(Long id, PostRequest request) {
        Post post = getById(id);
        apply(post, request);
        post.setUpdatedAt(LocalDateTime.now());
        post.setVersion((post.getVersion() == null ? 0 : post.getVersion()) + 1);
        postMapper.updateById(post);
        return post;
    }

    @Transactional
    public void delete(Long id) {
        if (postMapper.deleteById(id) == 0) throw new BusinessException(404, "文章不存在");
    }

    private void apply(Post post, PostRequest request) {
        post.setTitle(request.title().trim());
        String slug = request.slug() == null || request.slug().isBlank()
                ? slugify(request.title()) : request.slug().trim();
        post.setSlug(slug);
        post.setSummary(request.summary());
        post.setContentMarkdown(request.contentMarkdown());
        post.setContentHtml(request.contentHtml() == null ? request.contentMarkdown() : request.contentHtml());
        post.setCoverUrl(request.coverUrl());
        String status = request.status() == null || request.status().isBlank()
                ? PostStatus.DRAFT.name() : request.status().toUpperCase(Locale.ROOT);
        if (!java.util.Arrays.stream(PostStatus.values()).map(Enum::name).toList().contains(status)) {
            throw new BusinessException("文章状态不合法");
        }
        post.setStatus(status);
        post.setFeatured(Boolean.TRUE.equals(request.featured()));
        post.setCategoryId(request.categoryId());
        if (PostStatus.PUBLISHED.name().equals(status) && post.getPublishedAt() == null) {
            post.setPublishedAt(LocalDateTime.now());
        }
    }

    private String slugify(String title) {
        String slug = title.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
        return slug.isBlank() ? "post-" + UUID.randomUUID().toString().substring(0, 8) : slug;
    }
}
