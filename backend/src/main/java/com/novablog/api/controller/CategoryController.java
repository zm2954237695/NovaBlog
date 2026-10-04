package com.novablog.api.controller;

import com.novablog.api.common.ApiResponse;
import com.novablog.api.model.Category;
import com.novablog.api.service.CategoryTagService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/v1/admin/categories")
public class CategoryController {
    private final CategoryTagService categoryTagService;

    public CategoryController(CategoryTagService categoryTagService) {
        this.categoryTagService = categoryTagService;
    }

    @GetMapping
    public ApiResponse<List<Category>> list() {
        return ApiResponse.success(categoryTagService.categories());
    }

    @PostMapping
    public ApiResponse<Category> create(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("分类名称不能为空");
        String slug = request.getOrDefault("slug", name.toLowerCase().replaceAll("\\s+", "-"));
        return ApiResponse.success(categoryTagService.createCategory(name, slug, request.get("description")));
    }
}
