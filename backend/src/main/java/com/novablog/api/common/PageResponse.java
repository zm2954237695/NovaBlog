package com.novablog.api.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public record PageResponse<T>(List<T> items, long page, long size, long total, long pages) {
    public static <T> PageResponse<T> from(Page<T> result) {
        return new PageResponse<>(result.getRecords(), result.getCurrent(), result.getSize(),
                result.getTotal(), result.getPages());
    }
}
