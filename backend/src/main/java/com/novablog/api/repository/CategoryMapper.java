package com.novablog.api.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novablog.api.model.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
