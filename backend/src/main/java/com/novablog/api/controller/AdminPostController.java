package com.novablog.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.novablog.api.common.ApiResponse;
import com.novablog.api.common.PageResponse;
import com.novablog.api.dto.PostRequest;
import com.novablog.api.model.Post;
import com.novablog.api.model.User;
import com.novablog.api.repository.UserMapper;
import com.novablog.api.service.PostService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/posts")
public class AdminPostController {
    private final PostService postService;
    private final UserMapper userMapper;

    public AdminPostController(PostService postService, UserMapper userMapper) {
        this.postService = postService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ApiResponse<PageResponse<Post>> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "20") long size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        return ApiResponse.success(postService.adminList(page, size, status, keyword));
    }

    @GetMapping("/{id}")
    public ApiResponse<Post> get(@PathVariable Long id) {
        return ApiResponse.success(postService.getById(id));
    }

    @PostMapping
    public ApiResponse<Post> create(@Valid @RequestBody PostRequest request, Authentication authentication) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", authentication.getName()));
        return ApiResponse.success(postService.create(request, user == null ? 1L : user.getId()));
    }

    @PutMapping("/{id}")
    public ApiResponse<Post> update(@PathVariable Long id, @Valid @RequestBody PostRequest request) {
        return ApiResponse.success(postService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ApiResponse.success();
    }
}
