package com.novablog.api.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.novablog.api.model.Category;
import com.novablog.api.model.Post;
import com.novablog.api.model.User;
import com.novablog.api.repository.CategoryMapper;
import com.novablog.api.repository.PostMapper;
import com.novablog.api.repository.UserMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements ApplicationRunner {
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final PostMapper postMapper;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserMapper userMapper, CategoryMapper categoryMapper, PostMapper postMapper,
                           PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.categoryMapper = categoryMapper;
        this.postMapper = postMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        User admin = userMapper.selectOne(Wrappers.<User>query().eq("username", "admin"));
        if (admin == null) {
            admin = new User();
            admin.setUsername("admin");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setDisplayName("NovaBlog 管理员");
            admin.setRole("ADMIN");
            admin.setEnabled(true);
            admin.setCreatedAt(LocalDateTime.now());
            userMapper.insert(admin);
        }

        Category category = categoryMapper.selectOne(Wrappers.<Category>query().eq("slug", "engineering"));
        if (category == null) {
            category = new Category();
            category.setName("工程实践");
            category.setSlug("engineering");
            category.setDescription("Web 工程、架构与效率实践");
            category.setSortOrder(1);
            category.setCreatedAt(LocalDateTime.now());
            categoryMapper.insert(category);
        }

        if (postMapper.selectCount(null) == 0) {
            Post post = new Post();
            post.setTitle("欢迎来到 NovaBlog");
            post.setSlug("welcome-to-novablog");
            post.setSummary("这是一个由 Java 与 Vue 驱动的现代个人博客示例。");
            post.setContentMarkdown("# 欢迎来到 NovaBlog\n\n这是你的第一篇文章。登录管理后台即可开始创作。\n\n- Java 17 + Spring Boot\n- Vue 3 + Vite\n- MySQL / H2 双配置\n");
            post.setContentHtml(post.getContentMarkdown());
            post.setStatus("PUBLISHED");
            post.setFeatured(true);
            post.setCategoryId(category.getId());
            post.setAuthorId(admin.getId());
            post.setViewsCount(0L);
            post.setLikesCount(0L);
            post.setVersion(1);
            post.setPublishedAt(LocalDateTime.now());
            post.setCreatedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());
            postMapper.insert(post);
        }
    }
}
