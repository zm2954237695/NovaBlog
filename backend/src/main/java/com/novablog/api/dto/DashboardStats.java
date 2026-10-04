package com.novablog.api.dto;

public record DashboardStats(long posts, long publishedPosts, long drafts, long comments, long pendingComments,
                             long totalViews) {
}
