package com.novablog.api.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.novablog.api.dto.DashboardStats;
import com.novablog.api.model.Comment;
import com.novablog.api.model.Post;
import com.novablog.api.repository.CommentMapper;
import com.novablog.api.repository.PostMapper;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    public DashboardService(PostMapper postMapper, CommentMapper commentMapper) {
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
    }

    public DashboardStats stats() {
        long posts = postMapper.selectCount(null);
        long published = postMapper.selectCount(Wrappers.<Post>query().eq("status", "PUBLISHED"));
        long drafts = postMapper.selectCount(Wrappers.<Post>query().eq("status", "DRAFT"));
        long comments = commentMapper.selectCount(null);
        long pending = commentMapper.selectCount(Wrappers.<Comment>query().eq("status", "PENDING"));
        long views = postMapper.selectList(null).stream().mapToLong(p -> p.getViewsCount() == null ? 0 : p.getViewsCount()).sum();
        return new DashboardStats(posts, published, drafts, comments, pending, views);
    }
}
