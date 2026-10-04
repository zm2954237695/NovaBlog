package com.novablog.api.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.novablog.api.model.Category;
import com.novablog.api.model.Tag;
import com.novablog.api.repository.CategoryMapper;
import com.novablog.api.repository.TagMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryTagService {
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;

    public CategoryTagService(CategoryMapper categoryMapper, TagMapper tagMapper) {
        this.categoryMapper = categoryMapper;
        this.tagMapper = tagMapper;
    }

    public List<Category> categories() {
        return categoryMapper.selectList(Wrappers.<Category>query().orderByAsc("sort_order").orderByAsc("id"));
    }

    public List<Tag> tags() {
        return tagMapper.selectList(Wrappers.<Tag>query().orderByAsc("name"));
    }

    public Category createCategory(String name, String slug, String description) {
        Category category = new Category();
        category.setName(name);
        category.setSlug(slug);
        category.setDescription(description);
        category.setSortOrder(0);
        category.setCreatedAt(LocalDateTime.now());
        categoryMapper.insert(category);
        return category;
    }
}
