package com.novablog.api.controller;

import com.novablog.api.common.ApiResponse;
import com.novablog.api.common.PageResponse;
import com.novablog.api.model.Category;
import com.novablog.api.model.Post;
import com.novablog.api.model.Tag;
import com.novablog.api.service.CategoryTagService;
import com.novablog.api.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {
    private final PostService postService;
    private final CategoryTagService categoryTagService;

    public PublicController(PostService postService, CategoryTagService categoryTagService) {
        this.postService = postService;
        this.categoryTagService = categoryTagService;
    }

    @GetMapping("/posts")
    public ApiResponse<PageResponse<Post>> posts(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId) {
        return ApiResponse.success(postService.publicList(page, size, keyword, categoryId));
    }

    @GetMapping("/posts/{slug}")
    public ApiResponse<Post> post(@PathVariable String slug) {
        return ApiResponse.success(postService.getPublicBySlug(slug));
    }

    @GetMapping("/categories")
    public ApiResponse<List<Category>> categories() {
        return ApiResponse.success(categoryTagService.categories());
    }

    @GetMapping("/tags")
    public ApiResponse<List<Tag>> tags() {
        return ApiResponse.success(categoryTagService.tags());
    }

    @GetMapping("/profile")
    public ApiResponse<Map<String, String>> profile() {
        return ApiResponse.success(Map.of(
                "name", "NovaBlog 作者",
                "bio", "用代码记录思考，用实践打磨产品。",
                "location", "China",
                "github", "https://github.com/"
        ));
    }
}
