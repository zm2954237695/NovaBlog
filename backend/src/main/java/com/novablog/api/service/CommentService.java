package com.novablog.api.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novablog.api.common.PageResponse;
import com.novablog.api.dto.CommentRequest;
import com.novablog.api.model.Comment;
import com.novablog.api.repository.CommentMapper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;

@Service
public class CommentService {
    private final CommentMapper commentMapper;

    public CommentService(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public List<Comment> approved(Long postId) {
        return commentMapper.selectList(Wrappers.<Comment>query().eq("post_id", postId)
                .eq("status", "APPROVED").orderByAsc("created_at"));
    }

    public Comment create(Long postId, CommentRequest request, String ip) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setParentId(request.parentId());
        comment.setNickname(request.nickname().trim());
        comment.setEmail(request.email());
        comment.setContent(request.content().trim());
        comment.setStatus("PENDING");
        comment.setIpHash(hash(ip));
        comment.setCreatedAt(LocalDateTime.now());
        commentMapper.insert(comment);
        return comment;
    }

    public PageResponse<Comment> adminList(long page, long size, String status) {
        Page<Comment> pager = new Page<>(Math.max(page, 1), Math.min(Math.max(size, 1), 100));
        var wrapper = Wrappers.<Comment>query();
        if (status != null && !status.isBlank()) wrapper.eq("status", status);
        wrapper.orderByDesc("created_at");
        return PageResponse.from(commentMapper.selectPage(pager, wrapper));
    }

    public void updateStatus(Long id, String status) {
        commentMapper.update(null, Wrappers.<Comment>update().eq("id", id).set("status", status));
    }

    private String hash(String value) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest((value == null ? "unknown" : value).getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            return "unknown";
        }
    }
}
