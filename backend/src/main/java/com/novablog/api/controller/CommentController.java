package com.novablog.api.controller;

import com.novablog.api.common.ApiResponse;
import com.novablog.api.common.PageResponse;
import com.novablog.api.dto.CommentRequest;
import com.novablog.api.dto.StatusRequest;
import com.novablog.api.model.Comment;
import com.novablog.api.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/public/posts/{postId}/comments")
    public ApiResponse<List<Comment>> publicComments(@PathVariable Long postId) {
        return ApiResponse.success(commentService.approved(postId));
    }

    @PostMapping("/comments/posts/{postId}")
    public ApiResponse<Comment> comment(@PathVariable Long postId, @Valid @RequestBody CommentRequest request,
                                        HttpServletRequest servletRequest) {
        return ApiResponse.success(commentService.create(postId, request, servletRequest.getRemoteAddr()));
    }

    @GetMapping("/admin/comments")
    public ApiResponse<PageResponse<Comment>> adminComments(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "20") long size,
            @RequestParam(required = false) String status) {
        return ApiResponse.success(commentService.adminList(page, size, status));
    }

    @PutMapping("/admin/comments/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest request) {
        commentService.updateStatus(id, request.status().toUpperCase());
        return ApiResponse.success();
    }
}
